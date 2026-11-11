package net.jammydodger101pom.item.custom;

import net.jammydodger101pom.util.CraftingHandler;
import net.minecraft.advancements.predicates.NbtPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Scoreboard;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SummonerStaff extends Item {
    public static CompoundTag entityData;
    public static EntityType<?> loadedEntityType;
    public static List<Mob> spawnedMobs = new ArrayList<>();
    public static final long deathTime = 2400;
    public static int currentTime = 0;

    public SummonerStaff(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (loadedEntityType == null || level.isClientSide() || player.getCooldowns().isOnCooldown(player.getItemInHand(hand))) {
            return InteractionResult.FAIL;
        }

        List<BlockPos> spawningPositions = generateSpawnPositions(player.blockPosition());
        for (BlockPos blockPos : spawningPositions) {
            spawnMobAtPos((ServerLevel) level, blockPos, player);
        }

        if (!player.isCreative()) {
            player.getCooldowns().addCooldown(player.getItemInHand(hand), 3000);
        }

        return InteractionResult.SUCCESS;
    }

    private void spawnMobAtPos(ServerLevel level, BlockPos blockPos, Player player) {
        Entity entity = EntityType.loadEntityRecursive(loadedEntityType, entityData, level, EntitySpawnReason.MOB_SUMMONED, EntityProcessor.NOP);
        if (entity != null) {
            if (entity instanceof Mob mob) {
                mob.finalizeSpawn(((ServerLevel) level), ((ServerLevel) level).getCurrentDifficultyAt(player.blockPosition()), EntitySpawnReason.MOB_SUMMONED, null);
                mob.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                //loadData(mob, level, player);
                mob.postDataManipulated();
                level.tryAddFreshEntityWithPassengers(mob);

                Scoreboard scoreboard = level.getScoreboard();
                String teamName = "summonerstaff-" + player.getScoreboardName();
                if (scoreboard.getPlayerTeam(teamName) == null) {
                    scoreboard.addPlayerTeam(teamName);
                }
                level.getScoreboard().addPlayerToTeam(mob.getScoreboardName(), scoreboard.getPlayerTeam(teamName));
                level.getScoreboard().addPlayerToTeam(player.getScoreboardName(), scoreboard.getPlayerTeam(teamName));

                spawnedMobs.add(mob);
            }
        }
    }

    @Override
    public void hurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
        if (!(mob instanceof Mob)) {
            attacker.level().playSound(null, attacker.blockPosition(), SoundEvents.VILLAGER_NO, SoundSource.PLAYERS);
        } else {
            storeEntity((Mob) mob);
            attacker.level().playSound(null, attacker.blockPosition(), SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS);
        }
    }

    private void storeEntity(Mob mob) {
        CompoundTag data = NbtPredicate.getEntityTagToCompare(mob);
        loadedEntityType = mob.getType();
        entityData = cleanData(data);
    }

    private CompoundTag cleanData(CompoundTag data) {
        data.remove("Air");
        data.remove("fall_distance");
        data.remove("Fire");
        data.remove("Motion");
        data.remove("OnGround");
        data.remove("Pos");
        data.remove("UUID");
        data.remove("Brain");
        data.remove("current_explosion_impact_pos");
        data.remove("Health");
        data.remove("home_pos");
        data.remove("last_hurt_by_mob");
        data.remove("last_hurt_by_player");
        data.remove("leash");
        data.remove("ticks_since_last_hurt_by_mob");
        data.remove("last_hurt_by_player_memory_time");
        return data;
    }

    // Generates spawn positions (not amazing code, needs update)
    public static List<BlockPos> generateSpawnPositions(BlockPos playerPos) {
        BlockPos posNorth = playerPos.offset(5,0,0);
        BlockPos posNorthEast = playerPos.offset(4,0,2);
        BlockPos posEastNorth = playerPos.offset(2,0,4);
        BlockPos posEast = playerPos.offset(0,0,5);
        BlockPos posEastSouth = playerPos.offset(-2,0,4);
        BlockPos posSouthEast = playerPos.offset(-4,0,2);
        BlockPos posSouth = playerPos.offset(-5,0,0);
        BlockPos posSouthWest = playerPos.offset(-4,0,-2);
        BlockPos posWestSouth = playerPos.offset(-2,0,-4);
        BlockPos posWest = playerPos.offset(0,0,-5);
        BlockPos posWestNorth = playerPos.offset(2,0,-4);
        BlockPos posNorthWest = playerPos.offset(4,0,-2);

        BlockPos[] positions = new BlockPos[] {posNorth, posEast, posSouth, posWest, posNorthEast, posEastNorth,
                posEastSouth, posSouthEast, posSouthWest, posWestSouth, posWestNorth, posNorthWest};
        return new ArrayList<>(Arrays.asList(positions));
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, Player player) {
        super.onCraftedBy(itemStack, player);
        CraftingHandler.craftedItem(itemStack, player.level(), player);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        if (!spawnedMobs.isEmpty()) {
            currentTime += 1;
            if (currentTime >= deathTime) {
                for (Mob mob : spawnedMobs) {
                    mob.kill(level);
                }

                spawnedMobs.clear();
                currentTime = 0;
            }
        }
    }
}
