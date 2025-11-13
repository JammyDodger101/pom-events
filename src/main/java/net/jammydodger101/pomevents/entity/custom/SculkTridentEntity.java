package net.jammydodger101.pomevents.entity.custom;

import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.entity.ModEntities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SculkTridentEntity extends PersistentProjectileEntity {
    private boolean dealtDamage;
    public int returnTimer;

    public SculkTridentEntity(EntityType<SculkTridentEntity> entityType, World world) {
        super(entityType, world);
    }

    public SculkTridentEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, stack, weapon);
    }

    public SculkTridentEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(type, owner, world, stack, shotFrom);
    }

    public SculkTridentEntity(World world, PlayerEntity playerEntity, ItemStack stack) {
        super(EntityType.TRIDENT, playerEntity, world, stack, null);
    }

    public SculkTridentEntity(World world, double x, double y, double z, ItemStack shotFrom) {
        super(EntityType.TRIDENT, x, y, z, world, shotFrom, null);
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

    private void sculkExplosion(World world, BlockPos blockPos, int radius) {
        BlockPos sculkChangePos = blockPos.add(0, -3, 0);
        // List<BlockPos> affectedBlocks = new ArrayList<>();
        // affectedBlocksIterable.forEach(affectedBlocks::add);
        // Explosion explosion = new Explosion(world, null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), power, affectedBlocks, Explosion.DestructionType.DESTROY_WITH_DECAY, ParticleTypes.SCULK_SOUL, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE);
        world.createExplosion(null, null, null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 2f, false, World.ExplosionSourceType.BLOCK, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE);
        decayBlocks(world, blockPos, 6);
        Iterable<BlockPos> affectedBlocksIterable = BlockPos.iterateRandomly(Random.create(), 2000, sculkChangePos, radius);
        for (BlockPos affBlock : affectedBlocksIterable) {
            // PomEvents.LOGGER.info(String.valueOf(affBlock));
            if (world.getBlockState(affBlock).isAir() || !affBlock.isWithinDistance(blockPos, 6.5) || world.getBlockState(affBlock).getBlock().getBlastResistance() > 200) {
                continue;
            }
            if (world instanceof ServerWorld serverWorld) {
                if (world.getBlockState(affBlock.up(2)).isAir()) {
                    serverWorld.spawnParticles(ParticleTypes.SCULK_CHARGE_POP, affBlock.getX() + 0.5, affBlock.getY() + 0.5, affBlock.getZ() + 0.5, 2, 0.2, 0.0, 0.2, 0.0);
                }
            }
            world.setBlockState(affBlock, Blocks.SCULK.getDefaultState());
            world.playSound((PlayerEntity)null, affBlock, SoundEvents.BLOCK_SCULK_CATALYST_BLOOM, SoundCategory.BLOCKS, 1.0F, 0.6F + random.nextFloat() * 0.4F);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        super.onBlockHit(hitResult);

        World world = this.getWorld();
        sculkExplosion(world, hitResult.getBlockPos(), 6);

        this.dealtDamage = true;
        this.setNoClip(true);
        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);

    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        if (hitResult.getEntity() != this.getOwner()) {
            World world = this.getWorld();
            sculkExplosion(world, hitResult.getEntity().getBlockPos(), 6);

            this.dealtDamage = true;
            this.setNoClip(true);
            this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
            this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);
        }

        super.onEntityHit(hitResult);
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
    }

    public boolean isEnchanted() {
        return false;
    }
}
