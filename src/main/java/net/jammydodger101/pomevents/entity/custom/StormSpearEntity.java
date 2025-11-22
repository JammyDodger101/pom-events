package net.jammydodger101.pomevents.entity.custom;

import net.jammydodger101.pomevents.item.ModItems;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/*
The entity thrown when the player releases the storm spear
 */
public class StormSpearEntity extends PersistentProjectileEntity {
    private boolean dealtDamage;
    public int returnTimer;

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
        return ModItems.STORM_SPEAR.getDefaultStack();
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

    //this is not my code it just does ts and it works
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

    // Affects the nearby entities
    private void stormExplosion(World world, BlockPos blockPos, int radius) {
        Box hitbox = createHitBox(blockPos, radius);
        List<Entity> entities = world.getOtherEntities(null, hitbox, EntityPredicates.VALID_LIVING_ENTITY);
        affectEntities(entities, world);
        summonLightningAtStrikePoint(world, blockPos);
        world.playSound(null, blockPos, SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.PLAYERS, 1f, 1.2f);
        spawnAOECloud(world, blockPos, StatusEffects.SLOWNESS, 200, 2f, 0.3f);
    }

    // Creates a bounding box from the world height limits to hit every entity
    private Box createHitBox(BlockPos blockPos, int radius) {
        BlockPos bottomLeftPos = new BlockPos(blockPos.getX() - radius, -63, blockPos.getZ() - radius);
        BlockPos topRightPos = new BlockPos(blockPos.getX() + radius, 300, blockPos.getZ() + radius);

        return Box.enclosing(bottomLeftPos, topRightPos);
    }

    // For every entity it summons lightning, adds glowing and sets them on fire
    private void affectEntities(List<Entity> entities, World world) {
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
    }

    // Summons lightning where the trident hit
    private void summonLightningAtStrikePoint(World world, BlockPos blockPos) {
        var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        lightning.setPosition(blockPos.toBottomCenterPos());
        world.spawnEntity(lightning);
    }

    // Spawns an area cloud entity
    private void spawnAOECloud(World world, BlockPos blockPos, RegistryEntry<StatusEffect> effect, int effectDuration, float radius, float radiusGrowth) {
        var AOECloud = new AreaEffectCloudEntity(world, blockPos.getX(), blockPos.getY()+1, blockPos.getZ());
        world.spawnEntity(AOECloud);
        AOECloud.setDuration(30);
        AOECloud.setRadius(radius);
        AOECloud.setRadiusGrowth(radiusGrowth);
        AOECloud.setPotionContents(PotionContentsComponent.DEFAULT.with(new StatusEffectInstance(effect, effectDuration)));
    }

    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        super.onBlockHit(hitResult);

        World world = this.getWorld();
        stormExplosion(world, hitResult.getBlockPos(), 6);
        tridentHit();
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        if (hitResult.getEntity() != this.getOwner()) {
            World world = this.getWorld();
            stormExplosion(world, hitResult.getEntity().getBlockPos(), 6);
            tridentHit();
        }
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
