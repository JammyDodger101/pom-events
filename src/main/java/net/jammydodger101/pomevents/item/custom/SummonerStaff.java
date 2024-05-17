package net.jammydodger101.pomevents.item.custom;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class SummonerStaff extends Item {

    public static LivingEntity loadedEntity;
    public static List<BlockPos> spawnPositions = new ArrayList<>();

    public SummonerStaff(Settings settings) {
        super(settings);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (loadedEntity == null || world.isClient() || user.getItemCooldownManager().isCoolingDown(this)) return TypedActionResult.fail(user.getStackInHand(hand));
        EntityType<?> entityType = loadedEntity.getType();
        generateSpawnPositions(user.getBlockPos());
        for (BlockPos spawnPos:
             spawnPositions) {
            entityType.spawn((ServerWorld) user.getWorld(), null ,null, spawnPos, SpawnReason.MOB_SUMMONED, false, false);
        }
        user.getItemCooldownManager().set(this,1200);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof PlayerEntity) {
            target.getWorld().playSound(null, attacker.getBlockPos(), SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS);
        } else {
            loadedEntity = target;
            target.getWorld().playSound(null, attacker.getBlockPos(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.PLAYERS);
        }
        return super.postHit(stack, target, attacker);
    }

    public static void generateSpawnPositions(BlockPos playerPos) {
        if (!spawnPositions.isEmpty()) {
            spawnPositions.clear();
        }
        BlockPos posNorth = playerPos.add(5,0,0);
        spawnPositions.add(posNorth);
        BlockPos posNorthEast = playerPos.add(4,0,2);
        spawnPositions.add(posNorthEast);
        BlockPos posEastNorth = playerPos.add(2,0,4);
        spawnPositions.add(posEastNorth);
        BlockPos posEast = playerPos.add(0,0,5);
        spawnPositions.add(posEast);
        BlockPos posEastSouth = playerPos.add(-2,0,4);
        spawnPositions.add(posEastSouth);
        BlockPos posSouthEast = playerPos.add(-4,0,2);
        spawnPositions.add(posSouthEast);
        BlockPos posSouth = playerPos.add(-5,0,0);
        spawnPositions.add(posSouth);
        BlockPos posSouthWest = playerPos.add(-4,0,-2);
        spawnPositions.add(posSouthWest);
        BlockPos posWestSouth = playerPos.add(-2,0,-4);
        spawnPositions.add(posWestSouth);
        BlockPos posWest = playerPos.add(0,0,-5);
        spawnPositions.add(posWest);
        BlockPos posWestNorth = playerPos.add(2,0,-4);
        spawnPositions.add(posWestNorth);
        BlockPos posNorthWest = playerPos.add(4,0,-2);
        spawnPositions.add(posNorthWest);
    }
}
