package net.jammydodger101.pomevents.entity.custom;

import net.jammydodger101.pomevents.item.ModItems;
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

/*
The entity that is shot when releasing the sculk trident
 */
public class SculkTridentEntity extends PersistentProjectileEntity {
    private boolean dealtDamage;
    public int returnTimer;

    public SculkTridentEntity(EntityType<SculkTridentEntity> entityType, World world) {
        super(entityType, world);
    }

    public SculkTridentEntity(World world, PlayerEntity playerEntity, ItemStack stack) {
        super(EntityType.TRIDENT, playerEntity, world, stack, null);
    }

    public SculkTridentEntity(World world, double x, double y, double z, ItemStack shotFrom) {
        super(EntityType.TRIDENT, x, y, z, world, shotFrom, null);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ModItems.SCULK_TRIDENT.getDefaultStack();
    }

    // Updates the trident every tick
    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity owner = this.getOwner();
        if (isReturning(owner)) {
            double speed = 3;
            if (!discardIfCreativeOrDead(owner)) {
                this.setNoClip(true);
                returnToOwner(owner, speed);
            }
        }

        super.tick();
    }

    // If the trident has dealt damage, or it has been set to no-clip
    private boolean isReturning(Entity owner) {
        return this.dealtDamage || this.isNoClip() && owner != null;
    }

    // Removes the trident if the player is in creative or has died
    private boolean discardIfCreativeOrDead(Entity owner) {
        if (owner instanceof PlayerEntity playerOwner) {
            if (playerOwner.isCreative()) {
                this.discard();
            }
        }
        if (!this.isOwnerAlive()) {
            this.discard();
            return true;
        }
        return false;
    }

    // this is not my code it just does ts and it works
    private void returnToOwner(Entity owner, double returnSpeed) {
        Vec3d displacementToOwner = owner.getEyePos().subtract(this.getPos());
        this.setPos(this.getX(), this.getY() + displacementToOwner.y * 0.015 * returnSpeed, this.getZ());
        if (this.getWorld().isClient) {
            this.lastRenderY = this.getY();
        }

        double velSpeed = 0.05 * returnSpeed;
        this.setVelocity(this.getVelocity().multiply(0.95).add(displacementToOwner.normalize().multiply(velSpeed)));
        if (this.returnTimer == 0) {
            this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
        }

        ++this.returnTimer;
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

    // Sets nearby blocks to air
    private void decayBlocks(World world, BlockPos blockPos, int size) {
        Iterable<BlockPos> affectedBlocksIterable = BlockPos.iterateOutwards(blockPos, size, size, size);
        for (BlockPos block : affectedBlocksIterable) {
            if (isValidBlockToDecay(world, blockPos, block, size)) {
                world.setBlockState(block, Blocks.AIR.getDefaultState());
            }
        }
    }

    // Checks to ensure that the block that is being removed is not air, is within range, and doesn't have a crazy blast resistance (e.g. bedrock)
    private boolean isValidBlockToDecay(World world, BlockPos centre, BlockPos blockPos, int size) {
        return !(world.getBlockState(blockPos).isAir() || !blockPos.isWithinDistance(centre, size) || world.getBlockState(blockPos).getBlock().getBlastResistance() > 200);
    }

    // Explodes, removing blocks and randomly setting the remaining blocks to sculk
    private void sculkExplosion(World world, BlockPos blockPos, int radius) {
        BlockPos sculkCentre = blockPos.add(0, -3, 0);
        world.createExplosion(null, null, null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 2f, false, World.ExplosionSourceType.BLOCK, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE);
        decayBlocks(world, blockPos, radius-1);
        changeRandomBlocksToSculk(world, sculkCentre, blockPos, radius);
    }

    // Gets random blocks within range and sets them to sculk
    private void changeRandomBlocksToSculk(World world, BlockPos sculkCentre, BlockPos explosionCentre, int radius) {
        Iterable<BlockPos> affectedBlocksIterable = BlockPos.iterateRandomly(Random.create(), 2000, sculkCentre, radius);
        for (BlockPos affectedBlock : affectedBlocksIterable) {
            if (isValidBlockToDecay(world, explosionCentre, affectedBlock, radius)) {
                spawnSculkParticles(world, affectedBlock);
                world.setBlockState(affectedBlock, Blocks.SCULK.getDefaultState());
                world.playSound(null, affectedBlock, SoundEvents.BLOCK_SCULK_CATALYST_BLOOM, SoundCategory.BLOCKS, 1.0F, 0.6F + random.nextFloat() * 0.4F);
            }
        }
    }

    // Spawns the sculk soul particles
    private void spawnSculkParticles(World world, BlockPos blockPos) {
        if (world instanceof ServerWorld serverWorld) {
            if (world.getBlockState(blockPos.up(2)).isAir()) {
                Vec3d pos = blockPos.toBottomCenterPos();
                serverWorld.spawnParticles(ParticleTypes.SCULK_SOUL, pos.x, pos.y+1, pos.z, 2, 0.2, 0.0, 0.0, 0.2);
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        super.onBlockHit(hitResult);

        World world = this.getWorld();
        sculkExplosion(world, hitResult.getBlockPos(), 6);
        tridentHit();
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        if (hitResult.getEntity() != this.getOwner()) {
            World world = this.getWorld();
            sculkExplosion(world, hitResult.getEntity().getBlockPos(), 6);
            tridentHit();
        }

        super.onEntityHit(hitResult);
    }

    private void tridentHit() {
        this.dealtDamage = true;
        this.setNoClip(true);
        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
    }

    public boolean isEnchanted() {
        return false;
    }
}
