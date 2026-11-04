package net.jammydodger101.item.custom;

import net.jammydodger101.entity.custom.ThrownPrismarineAnchor;
import net.jammydodger101.particle.ParticleSpawner;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class PrismarineAnchor extends Item {
    public static @Nullable ThrownPrismarineAnchor thrownAnchor;

    public PrismarineAnchor(Properties properties) {
        super(properties);
    }

    // MACE STUFF
    @Override
    public void hurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
        if (canSmashAttack(attacker)) {
            ServerLevel level = (ServerLevel)attacker.level();
            attacker.setDeltaMovement(attacker.getDeltaMovement().with(Direction.Axis.Y, 0.009999999776482582));
            attacker.setIgnoreFallDamageFromCurrentImpulse(true, this.calculateImpactPosition(attacker));
            if (attacker instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
            }

            if (mob.onGround()) {
                if (attacker instanceof ServerPlayer serverPlayer) {
                    serverPlayer.setSpawnExtraParticlesOnFall(true);
                }

                SoundEvent sound = attacker.fallDistance > 5.0 ? SoundEvents.MACE_SMASH_GROUND_HEAVY : SoundEvents.MACE_SMASH_GROUND;
                level.playSound((Entity)null, attacker.getX(), attacker.getY(), attacker.getZ(), sound, attacker.getSoundSource(), 1.0F, 1.0F);
            } else {
                level.playSound((Entity)null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.MACE_SMASH_AIR, attacker.getSoundSource(), 1.0F, 1.0F);
            }

            ParticleSpawner.createDustPillar(Blocks.PRISMARINE.defaultBlockState(), level, mob.position(), 3, 50, 10);
            ParticleSpawner.createDustPillar(Blocks.PRISMARINE.defaultBlockState(), level, mob.position(), 1, 30, 6);
            knockback(level, attacker, mob);
        }
    }

    @Override
    public void postHurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
        if (canSmashAttack(attacker)) {
            attacker.resetFallDistance();
        }
    }

    private static void knockback(final Level level, final Entity attacker, final Entity entity) {
        level.levelEvent(2013, entity.getOnPos(), 750);
        level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(3.5), knockbackPredicate(attacker, entity)).forEach((nearby) -> {
            Vec3 direction = nearby.position().subtract(entity.position());
            double knockbackPower = getKnockbackPower(attacker, nearby, direction);
            Vec3 knockbackVector = direction.normalize().scale(knockbackPower);
            if (knockbackPower > 0.0) {
                nearby.push(knockbackVector.x, 0.699999988079071, knockbackVector.z);
                if (nearby instanceof ServerPlayer) {
                    ServerPlayer otherPlayer = (ServerPlayer)nearby;
                    otherPlayer.connection.send(new ClientboundSetEntityMotionPacket(otherPlayer));
                }
            }

        });
    }

    public float getAttackDamageBonus(final Entity victim, final float ignoredDamage, final DamageSource damageSource) {
        Entity var5 = damageSource.getDirectEntity();
        if (var5 instanceof LivingEntity attacker) {
            if (!canSmashAttack(attacker)) {
                return 0.0F;
            } else {
                double fallHeightThreshold1 = 3.0;
                double fallHeightThreshold2 = 8.0;
                double fallDistance = attacker.fallDistance;
                double damage;
                if (fallDistance <= 3.0) {
                    damage = 4.0 * fallDistance;
                } else if (fallDistance <= 8.0) {
                    damage = 12.0 + 2.0 * (fallDistance - 3.0);
                } else {
                    damage = 22.0 + fallDistance - 8.0;
                }

                Level var14 = attacker.level();
                if (var14 instanceof ServerLevel) {
                    ServerLevel level = (ServerLevel)var14;
                    return (float)(damage + (double) EnchantmentHelper.modifyFallBasedDamage(level, attacker.getWeaponItem(), victim, damageSource, 0.0F) * fallDistance);
                } else {
                    return (float)damage;
                }
            }
        } else {
            return 0.0F;
        }
    }

    public static boolean canSmashAttack(final LivingEntity attacker) {
        return attacker.fallDistance > 1.5 && !attacker.isFallFlying() && !hasAnchorBeenThrown();
    }

    private Vec3 calculateImpactPosition(final LivingEntity attacker) {
        return attacker.isIgnoringFallDamageFromCurrentImpulse() && attacker.currentImpulseImpactPos.y <= attacker.position().y ? attacker.currentImpulseImpactPos : attacker.position();
    }

    private static double getKnockbackPower(final Entity attacker, final LivingEntity nearby, final Vec3 direction) {
        return (3.5 - direction.length()) * 0.699999988079071 * (double)(attacker.fallDistance > 5.0 ? 2 : 1) * (1.0 - nearby.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
    }

    private static Predicate<LivingEntity> knockbackPredicate(final Entity attacker, final Entity entity) {
        return (nearby) -> {
            boolean notSpectator;
            boolean notPlayer;
            boolean notAlliedToPlayer;
            boolean var10000;
            label82: {
                notSpectator = !nearby.isSpectator();
                notPlayer = nearby != attacker && nearby != entity;
                notAlliedToPlayer = !attacker.isAlliedTo(nearby);
                if (nearby instanceof TamableAnimal animal) {
                    if (entity instanceof LivingEntity livingAttacker) {
                        if (animal.isTame() && animal.isOwnedBy(livingAttacker)) {
                            var10000 = true;
                            break label82;
                        }
                    }
                }

                var10000 = false;
            }

            boolean notTamedByPlayer;
            label74: {
                notTamedByPlayer = !var10000;
                if (nearby instanceof ArmorStand armorStand) {
                    if (armorStand.isMarker()) {
                        var10000 = false;
                        break label74;
                    }
                }

                var10000 = true;
            }

            boolean notArmorStand;
            boolean withinRange;
            label68: {
                notArmorStand = var10000;
                withinRange = entity.distanceToSqr(nearby) <= Math.pow(3.5, 2.0);
                if (nearby instanceof Player player) {
                    if (player.isCreative() && player.getAbilities().flying) {
                        var10000 = true;
                        break label68;
                    }
                }

                var10000 = false;
            }

            boolean notFlyingInCreative = !var10000;
            return notSpectator && notPlayer && notAlliedToPlayer && notTamedByPlayer && notArmorStand && withinRange && notFlyingInCreative;
        };
    }




    // HARPOON STUFF


    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
        if (entity instanceof Player player) {
            if (thrownAnchor != null) {
                return false;
            }

            int timeHeld = this.getUseDuration(itemStack, entity) - remainingTime;
            float power = getPowerForTime(timeHeld);
            if (power < 0.3) {
                return false;
            }

            // fire anchor
            if (level instanceof ServerLevel serverLevel) {
                //ItemStack thrownItemStack = itemStack.consumeAndReturn(1, player);
                ThrownPrismarineAnchor anchor = Projectile.spawnProjectileFromRotation(ThrownPrismarineAnchor::new, serverLevel, itemStack, player, 0.0f, power*2, 0.0f);
                thrownAnchor = anchor;

                level.playSound((Entity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
                player.awardStat(Stats.ITEM_USED.get(this));
                return true;
            }
        }
        return false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (thrownAnchor == null) {
            player.startUsingItem(hand);
            return InteractionResult.PASS;
        } else {
            ItemStack itemStack = player.getItemInHand(hand);
            if (!level.isClientSide()) {
                int dmg = thrownAnchor.retrieve(itemStack);
                itemStack.hurtAndBreak(dmg, player, hand.asEquipmentSlot());
                thrownAnchor = null;
            }

            level.playSound((Entity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_FINISH);
            player.getCooldowns().addCooldown(itemStack, 30);
            //thrownAnchor = null;

            return InteractionResult.SUCCESS;
        }
    }

    public static float getPowerForTime(final int timeHeld) {
        float pow = (float)timeHeld / 20.0F;
        pow = (pow * pow + pow * 2.0F) / 3.0F;
        if (pow > 1.0F) {
            pow = 1.0F;
        }

        return pow;
    }

    public static boolean hasAnchorBeenThrown() {
        return thrownAnchor != null;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.SPEAR;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(itemStack, level, owner, slot);
        if (thrownAnchor != null) {
            Player player = ((Player) owner);
            if (thrownAnchor.shouldStopFishing(player)) {
                thrownAnchor = null;
                itemStack.hurtAndBreak(3, ((Player) owner), slot);
                level.playSound((Entity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_FINISH);
                player.getCooldowns().addCooldown(itemStack, 30);
            }
        }
    }

}
