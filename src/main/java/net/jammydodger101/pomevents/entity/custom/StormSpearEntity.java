package net.jammydodger101.pomevents.entity.custom;

import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.entity.ModEntities;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StormSpearEntity extends PersistentProjectileEntity {
    private boolean dealtDamage;
    public int returnTimer;

    public StormSpearEntity(EntityType<StormSpearEntity> entityType, World world) {
        super(entityType, world);
    }

    public StormSpearEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, stack, weapon);
    }

    public StormSpearEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(type, owner, world, stack, shotFrom);
    }

    public StormSpearEntity(World world, PlayerEntity playerEntity, ItemStack stack) {
        super(EntityType.TRIDENT, playerEntity, world, stack, null);
        this.pickupType = PickupPermission.ALLOWED;
    }

    public StormSpearEntity(World world, double x, double y, double z, ItemStack shotFrom) {
        super(EntityType.TRIDENT, x, y, z, world, shotFrom, null);
        this.pickupType = PickupPermission.ALLOWED;
    }


    @Override
    protected ItemStack getDefaultItemStack() {
        return null;
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        //PomEvents.LOGGER.info(String.valueOf(this.dealtDamage));
        //PomEvents.LOGGER.info(String.valueOf(this.isNoClip()));

        Entity entity = this.getOwner();
        if (this.dealtDamage || this.isNoClip() && entity != null) {
            if (entity instanceof PlayerEntity playerEntity) {
                if (playerEntity.isCreative()) {
                    this.discard();
                }
            }

            int i = 3;
            if (!this.isOwnerAlive()) {
                this.discard();
            } else {
                this.setNoClip(true);
                Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
                this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * (double)i, this.getZ());
                if (this.getWorld().isClient) {
                    this.lastRenderY = this.getY();
                }

                double d = 0.05 * (double)i;
                this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
                if (this.returnTimer == 0) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returnTimer;
            }
        }

        super.tick();
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    protected @Nullable EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    private void decayBlocks(World world ,BlockPos blockPos, int power) {
        Iterable<BlockPos> affectedBlocksIterable = BlockPos.iterateOutwards(blockPos, 6, 4, 6);
        for (BlockPos block : affectedBlocksIterable) {
            if (world.getBlockState(block).isAir() || !block.isWithinDistance(blockPos, 4) || world.getBlockState(block).getBlock().getBlastResistance() > 200) {
                continue;
            }
            world.setBlockState(block, Blocks.AIR.getDefaultState());
        }
    }

    private void stormExplosion(World world, BlockPos blockPos, int radius) {
        // create hit box
        var bottomLeftPos = new BlockPos(blockPos.getX() - radius, -63, blockPos.getZ() - radius);
        var topRightPos = new BlockPos(blockPos.getX() + radius, 300, blockPos.getZ() + radius);

        var hitbox = Box.enclosing(bottomLeftPos, topRightPos);
        // get all entities within range
        var entities = world.getOtherEntities(null, hitbox, EntityPredicates.VALID_LIVING_ENTITY);
        // summon lightning at entities

        for (Entity entity : entities) {
            if (entity == this || entity == this.getOwner()) {
                return;
            }

            var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
            lightning.setPosition(entity.getPos());
            world.spawnEntity(lightning);

            entity.setOnFireFor(6);

            ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 360));
        }

        var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        lightning.setPosition(blockPos.toBottomCenterPos());
        world.spawnEntity(lightning);

        world.playSound(null, blockPos, SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.PLAYERS, 1f, 1.2f);

        var AOECloud = new AreaEffectCloudEntity(world, blockPos.getX(), blockPos.getY()+1, blockPos.getZ());
        world.spawnEntity(AOECloud);
        AOECloud.setDuration(30);
        AOECloud.setRadius(2f);
        AOECloud.setRadiusGrowth(0.3f);
        AOECloud.setPotionContents(PotionContentsComponent.DEFAULT.with(new StatusEffectInstance(StatusEffects.SLOWNESS, 200)));
    }

    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        super.onBlockHit(hitResult);

        World world = this.getWorld();
        stormExplosion(world, hitResult.getBlockPos(), 6);

        this.dealtDamage = true;
        this.setNoClip(true);
        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);

    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        if (hitResult.getEntity() != this.getOwner()) {
            World world = this.getWorld();
            stormExplosion(world, hitResult.getEntity().getBlockPos(), 6);

            this.dealtDamage = true;
            this.setNoClip(true);
            this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
            this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);
        }

        //super.onEntityHit(hitResult);
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
    }

    public boolean isEnchanted() {
        return false;
    }
}
