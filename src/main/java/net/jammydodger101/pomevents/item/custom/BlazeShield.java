package net.jammydodger101.pomevents.item.custom;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.jammydodger101.pomevents.util.CraftingHandler;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.Collection;
import java.util.HashSet;

public class BlazeShield extends ShieldItem implements Equipment {
    public static final float MIN_DAMAGE_AMOUNT_TO_BREAK = 3.0f;
    public BlazeShield(Settings settings) {
        super(settings);
    }

    private final ObjectArrayList<BlockPos> affectedBlocks = new ObjectArrayList();

    // Gives the player fire resistance when holding in main or offhand
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity) {
            if (isWeaponInAHand(((PlayerEntity) entity), stack)) {
                ((PlayerEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    // Creates explosion when the player uses the item
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!user.getItemCooldownManager().isCoolingDown(this)) {

            if (world.isClient()) return TypedActionResult.fail(itemStack);
            //user.setCurrentHand(hand);
            world.createExplosion(null, null, null, user.getBlockPos().getX(), user.getBlockPos().getY(), user.getBlockPos().getZ(), 0f, false, World.ExplosionSourceType.TNT);

            createFireRing(8, user.getBlockPos(), ((ServerWorld) world));

            user.getItemCooldownManager().set(this, 300);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    public boolean isWeaponInAHand(PlayerEntity player, ItemStack stack) {
        return player.getStackInHand(player.getActiveHand()) == stack || player.getStackInHand(Hand.OFF_HAND) == stack;
    }

    public void createFireRing(int radius, BlockPos centre, ServerWorld world) {
        Iterable<BlockPos> set = BlockPos.iterateOutwards(centre, radius, 2, radius);

        for (BlockPos blockPos2 : set) {
            if (isValidBlock(blockPos2, world) || isWithinRange(4.0, 5.0, blockPos2, centre)) continue;
            world.setBlockState(blockPos2, AbstractFireBlock.getState(world, blockPos2));
        }
    }

    public boolean isValidBlock(BlockPos block, ServerWorld world) {
        return !world.getBlockState(block).isAir() || !world.getBlockState(block.down()).isOpaqueFullCube(world, block.down());
    }

    public boolean isWithinRange(double minRange, double maxRange, BlockPos block, BlockPos centre) {
        return !(block.isWithinDistance(centre, maxRange) && !block.isWithinDistance(centre, minRange));
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        CraftingHandler.craftedItem(stack, world, player);
    }
}
