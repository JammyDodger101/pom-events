package net.jammydodger101.pomevents.item.custom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionImpl;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class BlazeShield extends ShieldItem {
    public static final float MIN_DAMAGE_AMOUNT_TO_BREAK = 3.0f;
    public BlazeShield(Settings settings, int nExplosionPower, int nExplosionRadius, int nFireRingSize) {
        super(settings);
        explosionPower = nExplosionPower;
        explosionRadius = nExplosionRadius;
        fireRingSize = nFireRingSize;
    }

    private int explosionPower;
    private int explosionRadius;
    private int fireRingSize;

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (entity instanceof PlayerEntity player) {
            Boolean isHolding = (player.getStackInHand(player.getActiveHand()) == stack);
            Boolean isInOffHand = (player.getStackInHand(Hand.OFF_HAND) == stack);
            if (isHolding || isInOffHand) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1));
            }
        }
        super.inventoryTick(stack, world, entity, slot);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!user.getItemCooldownManager().isCoolingDown(itemStack)) {
            if (world.isClient()) return ActionResult.FAIL;

            world.createExplosion(null, null, null, user.getBlockPos().toBottomCenterPos(), explosionPower, false, World.ExplosionSourceType.TNT);

            Iterable<BlockPos> affectedBlocks = BlockPos.iterateOutwards(user.getBlockPos(), explosionRadius, 2, explosionRadius);

            for (BlockPos blockPos2 : affectedBlocks) {
                if (!world.getBlockState(blockPos2).isAir() || !world.getBlockState(blockPos2.down()).isOpaqueFullCube() || !(blockPos2.isWithinDistance(user.getBlockPos(), explosionRadius-1) && !blockPos2.isWithinDistance(user.getBlockPos(), explosionRadius-1-fireRingSize))) continue;
                world.setBlockState(blockPos2, AbstractFireBlock.getState(world, blockPos2));
            }
            user.getItemCooldownManager().set(itemStack, 300);
            return ActionResult.CONSUME;
        }
        return ActionResult.FAIL;
    }
}
