package net.jammydodger101.pomevents.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.*;

public class LuckBlade extends SwordItem {

    public List<RegistryEntry<StatusEffect>> attackingStatusEffectsList = new ArrayList<>(Arrays.asList(
            StatusEffects.BAD_OMEN,
            StatusEffects.BLINDNESS,
            StatusEffects.DARKNESS,
            StatusEffects.DOLPHINS_GRACE,
            StatusEffects.FIRE_RESISTANCE,
            StatusEffects.GLOWING,
            StatusEffects.HUNGER,
            StatusEffects.INFESTED,
            StatusEffects.INVISIBILITY,
            StatusEffects.LEVITATION,
            StatusEffects.MINING_FATIGUE,
            StatusEffects.NAUSEA,
            StatusEffects.NIGHT_VISION,
            StatusEffects.OOZING,
            StatusEffects.POISON,
            StatusEffects.REGENERATION,
            StatusEffects.RESISTANCE,
            StatusEffects.SLOW_FALLING,
            StatusEffects.SLOWNESS,
            StatusEffects.SPEED,
            StatusEffects.STRENGTH,
            StatusEffects.WEAKNESS,
            StatusEffects.WEAVING,
            StatusEffects.WIND_CHARGED,
            StatusEffects.WITHER));

    public List<RegistryEntry<StatusEffect>> holdingStatusEffectsList = new ArrayList<>(Arrays.asList(
            StatusEffects.ABSORPTION,
            StatusEffects.CONDUIT_POWER,
            StatusEffects.DOLPHINS_GRACE,
            StatusEffects.FIRE_RESISTANCE,
            StatusEffects.GLOWING,
            StatusEffects.HASTE,
            StatusEffects.HEALTH_BOOST,
            StatusEffects.HERO_OF_THE_VILLAGE,
            StatusEffects.INSTANT_HEALTH,
            StatusEffects.INVISIBILITY,
            StatusEffects.JUMP_BOOST,
            StatusEffects.NIGHT_VISION,
            StatusEffects.REGENERATION,
            StatusEffects.RESISTANCE,
            StatusEffects.SATURATION,
            StatusEffects.SLOW_FALLING,
            StatusEffects.SPEED,
            StatusEffects.STRENGTH,
            StatusEffects.WATER_BREATHING
    ));

    private int cooldownTimer = 0;
    private int cooldownMax = -1;
    private RegistryEntry<StatusEffect> currentHeldEffect;

    public LuckBlade(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!((PlayerEntity) attacker).getItemCooldownManager().isCoolingDown(this)) {
            Random random = new Random();
            target.addStatusEffect(new StatusEffectInstance((RegistryEntry<StatusEffect>) attackingStatusEffectsList.get(random.nextInt(0,attackingStatusEffectsList.size())), random.nextInt(50,80), random.nextInt(1,3)));
            ((PlayerEntity) attacker).getItemCooldownManager().set(this, 50);
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        PlayerEntity player = (PlayerEntity) entity;
        if (player.getStackInHand(player.getActiveHand()) == stack) {
            if (cooldownTimer > cooldownMax) {
                Random random = new Random();

                if (currentHeldEffect != null) {
                    player.removeStatusEffect(currentHeldEffect);
                }

                currentHeldEffect = holdingStatusEffectsList.get(random.nextInt(0,holdingStatusEffectsList.size()));
                int time = random.nextInt(20,80);
                StatusEffectInstance effectInstance = new StatusEffectInstance(currentHeldEffect, time, random.nextInt(1,3));
                player.addStatusEffect(effectInstance);

                cooldownTimer = 0;
                cooldownMax = time*2;
            }
            cooldownTimer++;

            super.inventoryTick(stack, world, entity, slot, selected);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // basically make the player god
        return super.use(world, user, hand);
    }
}
