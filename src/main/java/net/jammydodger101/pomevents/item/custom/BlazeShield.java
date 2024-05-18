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
            Explosion explosion = new Explosion(world, null, null, null, user.getX(), user.getY(), user.getZ(), 0.2f, false, Explosion.DestructionType.KEEP);
            user.setCurrentHand(hand);
            //BlockPos shieldPos = user.getBlockPos().add(user.getMovementDirection().getVector());
            world.createExplosion(null, null, null, user.getBlockPos().getX(), user.getBlockPos().getY(), user.getBlockPos().getZ(), 0f, false, World.ExplosionSourceType.TNT);
            double x = user.getX();
            double y = user.getY();
            double z = user.getZ();
            DamageSource damageSource = world.getDamageSources().explosion(explosion);
            ExplosionBehavior behavior = new EntityExplosionBehavior(user);
            float power = 2.4f;
            int l;
            int k;
            HashSet<BlockPos> set = Sets.newHashSet();
            int i = 16;
            for (int j = 0; j < 16; ++j) {
                for (k = 0; k < 16; ++k) {
                    block2: for (l = 0; l < 16; ++l) {
                        if (j != 0 && j != 15 && k != 0 && k != 15 && l != 0 && l != 15) continue;
                        double d = (float)j / 15.0f * 2.0f - 1.0f;
                        double e = (float)k / 15.0f * 2.0f - 1.0f;
                        double f = (float)l / 15.0f * 2.0f - 1.0f;
                        double g = Math.sqrt(d * d + e * e + f * f);
                        d /= g;
                        e /= g;
                        f /= g;
                        double m = x;
                        double n = y;
                        double o = z;
                        float p = 0.3f;
                        for (float h = power * (0.7f + world.random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {
                            BlockPos blockPos = BlockPos.ofFloored(m, n, o);
                            BlockState blockState = world.getBlockState(blockPos);
                            FluidState fluidState = world.getFluidState(blockPos);
                            if (!world.isInBuildLimit(blockPos)) continue block2;
                            if (behavior.canDestroyBlock(explosion, world, blockPos, blockState, h)) {
                                set.add(blockPos);
                            }
                            m += d * (double)0.3f;
                            n += e * (double)0.3f;
                            o += f * (double)0.3f;
                        }
                    }
                }
            }
            this.affectedBlocks.addAll((Collection<BlockPos>)set);
            for (BlockPos blockPos2 : this.affectedBlocks) {
                if (!world.getBlockState(blockPos2).isAir() || !world.getBlockState(blockPos2.down()).isOpaqueFullCube(world, blockPos2.down())) continue;
                world.setBlockState(blockPos2, AbstractFireBlock.getState(world, blockPos2));
            }
            user.getItemCooldownManager().set(this, 300);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

}
