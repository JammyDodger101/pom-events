package net.jammydodger101.pomevents.item.custom;

import net.jammydodger101.pomevents.util.CraftingHandler;
import net.minecraft.entity.Entity;
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

/*
A legendary weapon that applies a random effect when attacking and when held
When used, turns the player into essentially god
 */
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
            StatusEffects.SPEED,
            StatusEffects.STRENGTH,
            StatusEffects.WATER_BREATHING
    ));

    public List<RegistryEntry<StatusEffect>> godStatusEffectsList = new ArrayList<>(Arrays.asList(
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
            StatusEffects.NIGHT_VISION,
            StatusEffects.REGENERATION,
            StatusEffects.RESISTANCE,
            StatusEffects.SATURATION,
            StatusEffects.STRENGTH,
            StatusEffects.WATER_BREATHING
    ));

    private int cooldownTimer = 0;
    private int cooldownMax = -1;
    private RegistryEntry<StatusEffect> currentHeldEffect;

    public LuckBlade(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    // Adds a random effect to the target
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!((PlayerEntity) attacker).getItemCooldownManager().isCoolingDown(this)) {
            Random random = new Random();
            target.addStatusEffect(new StatusEffectInstance(attackingStatusEffectsList.get(random.nextInt(0,attackingStatusEffectsList.size())), random.nextInt(50,80), random.nextInt(1,3)));
            ((PlayerEntity) attacker).getItemCooldownManager().set(this, 50);
        }
        return true;
    }

    // Removes any bugged effects and gives an effect
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        PlayerEntity player = (PlayerEntity) entity;
        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return;
        }
        removeZeroedEffects(player);
        giveEffectIfHeld(player, stack);
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    // Applies a random effect to the player when held
    private void giveEffectIfHeld(PlayerEntity player, ItemStack stack) {
        if (player.getStackInHand(player.getActiveHand()) == stack) {
            if (cooldownTimer > cooldownMax) {
                Random random = new Random();

                if (currentHeldEffect != null) {
                    player.removeStatusEffect(currentHeldEffect);
                }

                currentHeldEffect = holdingStatusEffectsList.get(random.nextInt(0,holdingStatusEffectsList.size()));
                int time = random.nextInt(300,400);
                StatusEffectInstance effectInstance = new StatusEffectInstance(currentHeldEffect, time, random.nextInt(1,3));
                player.addStatusEffect(effectInstance);

                cooldownTimer = 0;
                cooldownMax = time*2;
            }
            cooldownTimer++;
        }
    }

    // Gives the player every positive effect for 15 seconds
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.getItemCooldownManager().isCoolingDown(this)) {
            Random random = new Random();
            StatusEffectInstance effectInstance;
            for (RegistryEntry<StatusEffect> effect :
                    godStatusEffectsList) {
                int godTime = 15;
                effectInstance = new StatusEffectInstance(effect, (godTime *20), random.nextInt(1,2));
                user.addStatusEffect(effectInstance);
            }
            user.getItemCooldownManager().set(this, (4*60)*20);
        }
        return super.use(world, user, hand);
    }

    // Removes any bugged effects that are just stuck on 0 duration for whatever reason
    private void removeZeroedEffects(PlayerEntity player) {
        try {
            for (StatusEffectInstance effect:
                    player.getStatusEffects()) {
                if (effect.getDuration() < 2) {
                    player.removeStatusEffect(effect.getEffectType());
                }
            }
        } catch (ConcurrentModificationException e) {
            cooldownTimer = cooldownMax + 1;
        }

    }

    // Ensures that multiple cannot be crafted
    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        CraftingHandler.craftedItem(stack, world, player);
    }
}
