package net.jammydodger101pom.item.custom;

import net.jammydodger101pom.entity.custom.ThrownSculkTrident;
import net.jammydodger101pom.util.CraftingHandler;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SculkTrident extends Item implements ProjectileItem {
    public static final int THROW_THRESHOLD_TIME = 10;
    public static final float BASE_DAMAGE = 8.0F;
    public static final float PROJECTILE_SHOOT_POWER = 2.5F;
    public static final float DETECTION_RANGE = 30f;

    private Map<LivingEntity, Float> entityMovement = new HashMap<>();
    private boolean isDetecting = false;

    public SculkTrident(Properties properties) {
        super(properties);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 8.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.9000000953674316, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2, false);
    }

    public ItemUseAnimation getUseAnimation(final ItemStack itemStack) {
        return ItemUseAnimation.TRIDENT;
    }

    public int getUseDuration(final ItemStack itemStack, final LivingEntity user) {
        return 72000;
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
        if (entity instanceof Player player) {
            if (this.getUseDuration(itemStack, entity) - remainingTime < 10) {
                return false;
            }

            Holder<SoundEvent> sound = (Holder)EnchantmentHelper.pickHighestLevel(itemStack, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);
            player.awardStat(Stats.ITEM_USED.get(this));

            if (level instanceof ServerLevel serverLevel) {
                itemStack.hurtWithoutBreaking(1, player);

                ItemStack thrownItemStack = itemStack.consumeAndReturn(1, player);
                ThrownSculkTrident trident = (ThrownSculkTrident)Projectile.spawnProjectileFromRotation(ThrownSculkTrident::new, serverLevel, thrownItemStack, player, 0.0F, 2.5F, 1.0F);
                if (player.hasInfiniteMaterials()) {
                    trident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    ((Player) entity).getCooldowns().addCooldown(itemStack, 300);
                }

                level.playSound((Entity)null, trident, (SoundEvent)sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(player.getItemInHand(hand))) {
            return InteractionResult.FAIL;
        }

        if (itemInHand.nextDamageWillBreak()) {
            return InteractionResult.FAIL;
        } else {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        ThrownSculkTrident trident = new ThrownSculkTrident(level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1));
        trident.pickup = AbstractArrow.Pickup.ALLOWED;
        return trident;
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, Player player) {
        super.onCraftedBy(itemStack, player);
        CraftingHandler.craftedItem(itemStack, player.level(), player);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(itemStack, level, owner, slot);

        if (owner instanceof Player player) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND) != itemStack || !player.isCrouching()) {
                entityMovement.clear();
                return;
            }

            List<LivingEntity> entities = level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, (LivingEntity) owner, AABB.ofSize(owner.position(), DETECTION_RANGE, DETECTION_RANGE, DETECTION_RANGE));
            Map<LivingEntity, Float> tempMap = new HashMap<>();
            for (LivingEntity targetPlayer : entities) {
                if (targetPlayer == owner) {
                    continue;
                }

                if (entityMovement.containsKey(targetPlayer)) {
                    if (targetPlayer.moveDist != entityMovement.get(targetPlayer)) {
                        targetPlayer.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1, 1, false, false, false));
                    }
                }

                tempMap.put(targetPlayer, targetPlayer.moveDist);
            }

            if (entityMovement.isEmpty() && !tempMap.isEmpty()) {
                //level.playLocalSound(owner.getX(), owner.getY(), owner.getZ(), SoundEvents.WARDEN_TENDRIL_CLICKS, SoundSource.NEUTRAL, 1.0f, 1.0f, false);
                level.playSound(null, owner.blockPosition(), SoundEvents.WARDEN_TENDRIL_CLICKS, SoundSource.PLAYERS);
            }

            entityMovement.clear();
            entityMovement.putAll(tempMap);
        }
    }
}
