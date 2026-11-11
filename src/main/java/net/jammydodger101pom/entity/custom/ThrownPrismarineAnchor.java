package net.jammydodger101pom.entity.custom;

import net.jammydodger101pom.entity.ModEntities;
import net.jammydodger101pom.item.ModItems;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ThrownPrismarineAnchor extends AbstractArrow {
    private @Nullable LivingEntity hookedIn;

    public ThrownPrismarineAnchor(ServerLevel level, LivingEntity shooter, ItemStack stack) {
        super(ModEntities.PRISMARINE_ANCHOR, shooter, level, new ItemStack(ModItems.PRISMARINE_ANCHOR), null);
    }

    public ThrownPrismarineAnchor(Level level, double x, double y, double z, ItemStack itemStack) {
        super(ModEntities.PRISMARINE_ANCHOR, x, y, z, level, itemStack, new ItemStack(ModItems.PRISMARINE_ANCHOR));
    }

    public ThrownPrismarineAnchor(EntityType<ThrownPrismarineAnchor> thrownPrismarineAnchorEntityType, Level level) {
        super(thrownPrismarineAnchorEntityType, level);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ModItems.PRISMARINE_ANCHOR.getDefaultInstance();
    }

    @Override
    public void tick() {
        if (this.hookedIn != null) {
            if (!this.hookedIn.isRemoved() && this.hookedIn.canInteractWithLevel() && this.hookedIn.level().dimension() == this.level().dimension()) {
                this.setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8), this.hookedIn.getZ());
                this.hookedIn.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 10, 5, false, true, false));
            } else {
                this.hookedIn = null;
            }
        }

        if (getPlayerOwner() == null) {
            this.discard();
        }

        super.tick();
    }

    public int retrieve(final ItemStack anchor) {
        Player owner = this.getPlayerOwner();
        if (!this.level().isClientSide() && owner != null && !this.shouldStopFishing(owner)) {
            int dmg = 0;
            if (this.hookedIn != null) {
                this.pullEntity(this.hookedIn);
                //CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer)owner, anchor, this, Collections.emptyList());
                //this.level().broadcastEntityEvent(this, (byte)31);
            }

            if (this.onGround()) {
                dmg = 2;
            }

            this.discard();


            return dmg;
        } else {
            this.discard();
            return 0;
        }


    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Player owner = this.getPlayerOwner();
        Entity entity = hitResult.getEntity();
        DamageSource damageSource = this.damageSources().arrow(this, (Entity)(owner != null ? owner : this));
        owner.setLastHurtMob(entity);
        this.level().playSound(null, hitResult.getEntity().blockPosition(), SoundEvents.TRIDENT_HIT, SoundSource.PLAYERS);

        if (entity.hurtOrSimulate(damageSource, 10)) {
            if (entity instanceof LivingEntity mob) {
                if (!this.level().isClientSide() && this.getPierceLevel() <= 0) {
                    mob.setArrowCount(mob.getArrowCount() + 1);
                }

                this.doPostHurtEffects(mob);
                ServerPlayer player;
                if (mob instanceof Player && owner instanceof ServerPlayer) {
                    player = (ServerPlayer)owner;
                    if (!this.isSilent() && mob != player) {
                        player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.PLAY_ARROW_HIT_SOUND, 0.0F));
                    }
                }


            }
        }

        if (!this.level().isClientSide()) {
            if (hitResult.getEntity() instanceof LivingEntity livingEntity) {
                this.hookedIn = livingEntity;
                this.setNoGravity(true);
                this.setNoPhysics(true);
                this.setDeltaMovement(this.getDeltaMovement().normalize().scale(hitResult.distanceTo(this)));

            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(hitResult.distanceTo(this)));
        this.level().playSound(null, hitResult.getBlockPos(), SoundEvents.TRIDENT_HIT_GROUND, SoundSource.PLAYERS);
    }

    public @Nullable Player getPlayerOwner() {
        Entity owner = this.getOwner();
        Player var10000;
        if (owner instanceof Player player) {
            var10000 = player;
        } else {
            var10000 = null;
        }

        return var10000;
    }

    public boolean shouldStopFishing(final Player owner) {
        if (owner.canInteractWithLevel()) {
            ItemStack selectedItem = owner.getMainHandItem();
            ItemStack selectedItemOffHand = owner.getOffhandItem();
            boolean mainHandIsFishing = selectedItem.is(ModItems.PRISMARINE_ANCHOR);
            boolean offHandIsFishing = selectedItemOffHand.is(ModItems.PRISMARINE_ANCHOR);
            if ((mainHandIsFishing || offHandIsFishing) && this.distanceToSqr(owner) <= 2048.0) {
                return false;
            }
        }

        this.discard();
        return true;
    }

    protected void pullEntity(final Entity entity) {
        Entity owner = this.getOwner();
        if (owner != null && entity.canSimulateMovement()) {
            Vec3 delta = (new Vec3(owner.getX() - this.getX(), (owner.getY() - this.getY()) / 2, owner.getZ() - this.getZ())).scale(0.4);
            entity.setDeltaMovement(entity.getDeltaMovement().add(delta));
        }
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

}
