package net.jammydodger101.pomevents.item.custom;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity) {
            if (((PlayerEntity) entity).getStackInHand(((PlayerEntity) entity).getActiveHand()) == stack || ((PlayerEntity) entity).getStackInHand(Hand.OFF_HAND) == stack) {
                ((PlayerEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!user.getItemCooldownManager().isCoolingDown(this)) {

            if (world.isClient) return TypedActionResult.fail(itemStack);
            Explosion explosion = new Explosion(world, null, user.getX(), user.getY(), user.getZ(), 0.2f, false, Explosion.DestructionType.KEEP);
            user.setCurrentHand(hand);
            //BlockPos shieldPos = user.getBlockPos().add(user.getMovementDirection().getVector());
            world.createExplosion(null, null, null, user.getBlockPos().getX(), user.getBlockPos().getY(), user.getBlockPos().getZ(), 0f, false, World.ExplosionSourceType.TNT);

            int radius = 8;
            Iterable<BlockPos> set = BlockPos.iterateOutwards(user.getBlockPos(), radius, 2, radius);

            for (BlockPos blockPos2 : set) {
                if (!world.getBlockState(blockPos2).isAir() || !world.getBlockState(blockPos2.down()).isOpaqueFullCube(world, blockPos2.down()) || !(blockPos2.isWithinDistance(user.getBlockPos(), 5.0) && !blockPos2.isWithinDistance(user.getBlockPos(), 4.0))) continue;
                world.setBlockState(blockPos2, AbstractFireBlock.getState(world, blockPos2));
            }
            user.getItemCooldownManager().set(this, 300);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    public void shootFire() {

    }

}
