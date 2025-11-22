package net.jammydodger101.pomevents.item.custom;

import net.jammydodger101.pomevents.util.CraftingHandler;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
A legendary staff that spawns 12 copies of a stored mob
Mobs can be stored by left-clicking them with the staff
 */
public class SummonerStaff extends Item {

    public static EntityType<?> loadedEntityType;
    public static NbtCompound nbtCompound = new NbtCompound();

    public SummonerStaff(Settings settings) {
        super(settings);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    // Spawns 12 mobs around the player when right-clicked
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (loadedEntityType == null || world.isClient() || user.getItemCooldownManager().isCoolingDown(this)) return TypedActionResult.fail(user.getStackInHand(hand));
        List<BlockPos> spawningPositions = generateSpawnPositions(user.getBlockPos());
        for (BlockPos spawnPos:
                spawningPositions) {
            spawnMobAtPos(world, spawnPos);
        }

        if (!user.isInCreativeMode()) {
            user.getItemCooldownManager().set(this,1200);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    // Spawns the loaded mob at the given position
    private void spawnMobAtPos(World world, BlockPos spawnPos) {
        MobEntity entity = (MobEntity) loadedEntityType.create((ServerWorld) world, null, spawnPos, SpawnReason.EVENT, false, false);
        if (entity != null) {
            NbtComponent nbtComponent = NbtComponent.of(nbtCompound);
            nbtComponent.applyToEntity(entity);
            entity.setPosition(spawnPos.toBottomCenterPos());
            world.spawnEntity(entity);
        }
    }

    // Removes any unwanted NBT tags from the compound before storing
    private void setAsCleanNBT(NbtCompound copyNBT) {
        nbtCompound.copyFrom(copyNBT);
        nbtCompound.remove("UUID");
        nbtCompound.remove("Health");
        nbtCompound.remove("Pos");
        nbtCompound.remove("Air");
        nbtCompound.remove("Brain");
        nbtCompound.remove("FallDistance");
        nbtCompound.remove("HurtByTimestamp");
        nbtCompound.remove("LeftHanded");
        nbtCompound.remove("Motion");
        nbtCompound.remove("OnGround");
        if (nbtCompound.contains("Moistness")) {
            nbtCompound.putInt("Moistness", 2400);
        }
    }

    // Checks if the hit entity is a mob and stores it
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(target instanceof MobEntity)) {
            target.getWorld().playSound(null, attacker.getBlockPos(), SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS);
        } else {
            storeEntity((MobEntity) target);
            target.getWorld().playSound(null, attacker.getBlockPos(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.PLAYERS);
        }
        return super.postHit(stack, target, attacker);
    }

    // Stores the entity type and NBT data
    private void storeEntity(MobEntity target) {
        NbtCompound entityNBT = new NbtCompound();
        loadedEntityType = target.getType();
        target.saveNbt(entityNBT);
        setAsCleanNBT(entityNBT);
    }

    // Generates spawn positions (not amazing code, needs update)
    public static List<BlockPos> generateSpawnPositions(BlockPos playerPos) {
        BlockPos posNorth = playerPos.add(5,0,0);
        BlockPos posNorthEast = playerPos.add(4,0,2);
        BlockPos posEastNorth = playerPos.add(2,0,4);
        BlockPos posEast = playerPos.add(0,0,5);
        BlockPos posEastSouth = playerPos.add(-2,0,4);
        BlockPos posSouthEast = playerPos.add(-4,0,2);
        BlockPos posSouth = playerPos.add(-5,0,0);
        BlockPos posSouthWest = playerPos.add(-4,0,-2);
        BlockPos posWestSouth = playerPos.add(-2,0,-4);
        BlockPos posWest = playerPos.add(0,0,-5);
        BlockPos posWestNorth = playerPos.add(2,0,-4);
        BlockPos posNorthWest = playerPos.add(4,0,-2);

        BlockPos[] positions = new BlockPos[] {posNorth, posEast, posSouth, posWest, posNorthEast, posEastNorth,
                posEastSouth, posSouthEast, posSouthWest, posWestSouth, posWestNorth, posNorthWest};
        return new ArrayList<>(Arrays.asList(positions));
    }

    // Ensures that multiple cannot be crafted
    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        CraftingHandler.craftedItem(stack, world, player);
    }
}
