package net.jammydodger101.entity.custom;

import net.jammydodger101.PomEvents2;
import net.jammydodger101.entity.ModEntities;
import net.jammydodger101.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ThrownSculkTrident extends AbstractArrow {
    private static final float WATER_INERTIA = 0.99F;
    private static final boolean DEFAULT_DEALT_DAMAGE = false;
    private boolean dealtDamage = false;
    public int clientSideReturnTridentTickCount;

    public ThrownSculkTrident(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public ThrownSculkTrident(ServerLevel level, LivingEntity shooter, ItemStack stack) {
        super(ModEntities.SCULK_TRIDENT, shooter, level, new ItemStack(ModItems.SCULK_TRIDENT), null);
    }

    public ThrownSculkTrident(Level level, double x, double y, double z, ItemStack itemStack) {
        super(ModEntities.SCULK_TRIDENT, x, y, z, level, itemStack, null);
    }


    @Override
    protected ItemStack getDefaultPickupItem() {
        return ModItems.SCULK_TRIDENT.getDefaultInstance();
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity currentOwner = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && currentOwner != null) {
            if (!this.isAcceptibleReturnOwner()) {
                Level var4 = this.level();
                if (var4 instanceof ServerLevel) {
                    ServerLevel level = (ServerLevel)var4;
                    if (this.pickup == Pickup.ALLOWED) {
                        this.spawnAtLocation(level, this.getPickupItem(), 0.1F);
                    }
                }

                this.discard();
            } else {
                if (!(currentOwner instanceof Player) && this.position().distanceTo(currentOwner.getEyePosition()) < (double)currentOwner.getBbWidth() + 1.0) {
                    this.discard();
                    return;
                }

                this.setNoPhysics(true);
                Vec3 vec = currentOwner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec.y * 0.015 * 3, this.getZ());
                double accel = 0.05 * (double)3;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec.normalize().scale(accel)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity currentOwner = this.getOwner();
        if (currentOwner != null && currentOwner.isAlive()) {
            return !(currentOwner instanceof ServerPlayer) || !currentOwner.isSpectator();
        } else {
            return false;
        }
    }

    protected @Nullable EntityHitResult findHitEntity(final Vec3 from, final Vec3 to) {
        return this.dealtDamage ? null : super.findHitEntity(from, to);
    }

    protected Collection<EntityHitResult> findHitEntities(final Vec3 from, final Vec3 to) {
        EntityHitResult e = this.findHitEntity(from, to);
        return e != null ? List.of(e) : List.of();
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        float damage = 8.0f;
        Entity currentOwner = this.getOwner();
        DamageSource damageSource = this.damageSources().trident(this, (Entity)(currentOwner == null ? this : currentOwner));
        Level level = this.level();
        this.dealtDamage = true;
        boolean wasHurt = entity.hurtOrSimulate(damageSource, damage);
        if (wasHurt) {
            if (level instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(serverLevel, entity, damageSource, this.getWeaponItem(), (weapon) -> {
                    this.kill(serverLevel);
                });
            }

            if (entity instanceof LivingEntity mob) {
                this.doKnockback(mob, damageSource);
                this.doPostHurtEffects(mob);
            }
        }

        if (entity.projectileReceivesSideEffectsOnHit(wasHurt)) {
            this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
            this.deflect(ProjectileDeflection.REVERSE, entity, this.owner, false, new Vec3(0.02, 0.2, 0.02));
        }

        sculkExplosion(level, hitResult.getEntity().blockPosition(), 6);

    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        sculkExplosion(this.level(), hitResult.getBlockPos(), 6);
    }

    private void sculkExplosion(Level world, BlockPos blockPos, float radius) {
        BlockPos sculkCentre = blockPos.offset(0, -3, 0);
        world.explode(null, null, null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), radius, false, Level.ExplosionInteraction.MOB);
        decayBlocks(world, blockPos, radius - 1);
        changeRandomBlocksToSculk(world, sculkCentre, blockPos, (int) radius);
        this.dealtDamage = true;
    }

    private void decayBlocks(Level world, BlockPos blockPos, float size) {
        Iterable<BlockPos> affectedBlocks = BlockPos.withinBoxByManhattanDistance(blockPos, ((int) size),(int) size,(int) size);
        for (BlockPos block : affectedBlocks) {
            if (isValidBlockToDecay(world, blockPos, block, (int) size)) {
                world.setBlock(block, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    private boolean isValidBlockToDecay(Level world, BlockPos centre, BlockPos blockPos, int size) {
        return !(world.getBlockState(blockPos).isAir() || (Mth.length(centre.getX() - blockPos.getX(), centre.getY() - blockPos.getY(), centre.getZ() - blockPos.getZ()) > size) || world.getBlockState(blockPos).getBlock().getExplosionResistance() > 200);
    }

    private void changeRandomBlocksToSculk(Level world, BlockPos sculkCentre, BlockPos explosionCentre, int radius) {
        Iterable<BlockPos> affectedBlocks = BlockPos.withinBoxByManhattanDistance(explosionCentre, ((int) radius),(int) radius,(int) radius);
        Random random1 = new Random();
        float skipChance = 0.5f;
        for (BlockPos block : affectedBlocks) {
            if (random1.nextFloat() > skipChance) {
                if (isValidBlockToDecay(world, sculkCentre, block, radius)) {
                    spawnSculkParticles(world, block);
                    world.setBlock(block, Blocks.SCULK.defaultBlockState(), 3);
                    world.playSound(null, block, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 1.0f, (0.6f + random.nextFloat() * 0.4f));
                }
            }
        }
    }

    private void spawnSculkParticles(Level world, BlockPos blockPos) {
        if (world instanceof ServerLevel serverLevel) {
            if (world.getBlockState(blockPos.offset(0,2,0)).isAir()) {
                serverLevel.sendParticles(ParticleTypes.SCULK_SOUL, blockPos.getX(), blockPos.getY()+1, blockPos.getZ(), 2, 0.0, 0.0, 0.0, 0.2);
            }
        }
    }

    protected void hitBlockEnchantmentEffects(final ServerLevel level, final BlockHitResult hitResult, final ItemStack weapon) {
        Vec3 compensatedHitPosition = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
        Entity var6 = this.getOwner();
        LivingEntity var10002;
        if (var6 instanceof LivingEntity livingOwner) {
            var10002 = livingOwner;
        } else {
            var10002 = null;
        }

        EnchantmentHelper.onHitBlock(level, weapon, var10002, this, (EquipmentSlot)null, compensatedHitPosition, level.getBlockState(hitResult.getBlockPos()), (item) -> {
            this.kill(level);
        });
    }

    @Override
    public @Nullable ItemStack getWeaponItem() {
        return null;
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    public void playerTouch(final Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }

    }

    @Override
    protected void tickDespawn() {
        return;
    }

    protected float getWaterInertia() {
        return WATER_INERTIA;
    }
}
