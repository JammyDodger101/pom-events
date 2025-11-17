package net.jammydodger101.pomevents.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.mob.HostileEntity;
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
import java.util.Collection;
import java.util.List;

public class SummonerStaff extends Item {

    public static LivingEntity loadedEntity;

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
        List<BlockPos> spawningPositions = generateSpawnPositions(user.getBlockPos());
        for (BlockPos spawnPos:
                spawningPositions) {
            LivingEntity entity = (LivingEntity) entityType.spawn((ServerWorld) user.getWorld(), spawnPos, SpawnReason.MOB_SUMMONED);

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
}
