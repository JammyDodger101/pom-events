package net.jammydodger101.pomevents.item.custom;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LuckBlade extends SwordItem {

    public List<StatusEffect> statusEffectsList = new ArrayList<>();

    public LuckBlade(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }


    public void addEffectsToList() {
        statusEffectsList.add(StatusEffects.ABSORPTION);
        statusEffectsList.add(StatusEffects.BAD_OMEN);
        statusEffectsList.add(StatusEffects.BLINDNESS);
    //    statusEffectsList.add(StatusEffects.CONDUIT_POWER);
        statusEffectsList.add(StatusEffects.DARKNESS);
        statusEffectsList.add(StatusEffects.DOLPHINS_GRACE);
        statusEffectsList.add(StatusEffects.FIRE_RESISTANCE);
        statusEffectsList.add(StatusEffects.GLOWING);
    //    statusEffectsList.add(StatusEffects.HASTE);
    //    statusEffectsList.add(StatusEffects.HEALTH_BOOST);
    //    statusEffectsList.add(StatusEffects.HERO_OF_THE_VILLAGE);
        statusEffectsList.add(StatusEffects.HUNGER);
    //    statusEffectsList.add(StatusEffects.INFESTED);
        statusEffectsList.add(StatusEffects.INSTANT_DAMAGE);
    //    statusEffectsList.add(StatusEffects.INSTANT_HEALTH);
        statusEffectsList.add(StatusEffects.INVISIBILITY);
        statusEffectsList.add(StatusEffects.JUMP_BOOST);
        statusEffectsList.add(StatusEffects.LEVITATION);
    //    statusEffectsList.add(StatusEffects.LUCK);
        statusEffectsList.add(StatusEffects.MINING_FATIGUE);
        statusEffectsList.add(StatusEffects.NAUSEA);
        statusEffectsList.add(StatusEffects.NIGHT_VISION);
    //    statusEffectsList.add(StatusEffects.OOZING);
        statusEffectsList.add(StatusEffects.POISON);
    //    statusEffectsList.add(StatusEffects.RAID_OMEN);
        statusEffectsList.add(StatusEffects.REGENERATION);
        statusEffectsList.add(StatusEffects.RESISTANCE);
    //    statusEffectsList.add(StatusEffects.SATURATION);
        statusEffectsList.add(StatusEffects.SLOW_FALLING);
        statusEffectsList.add(StatusEffects.SLOWNESS);
        statusEffectsList.add(StatusEffects.SPEED);
        statusEffectsList.add(StatusEffects.STRENGTH);
    //    statusEffectsList.add(StatusEffects.TRIAL_OMEN);
        statusEffectsList.add(StatusEffects.WATER_BREATHING);
        statusEffectsList.add(StatusEffects.WEAKNESS);
    //    statusEffectsList.add(StatusEffects.WIND_CHARGED);
        statusEffectsList.add(StatusEffects.WITHER);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!((PlayerEntity) attacker).getItemCooldownManager().isCoolingDown(this)) {
            addEffectsToList();
            //stack.damage(1, attacker, EquipmentSlot.MAINHAND);
            Random random = new Random();
            target.addStatusEffect(new StatusEffectInstance((StatusEffect) statusEffectsList.get(random.nextInt(0,statusEffectsList.size())), random.nextInt(50,80), random.nextInt(1,3)));
            ((PlayerEntity) attacker).getItemCooldownManager().set(this, 50);
        }
        return true;
    }


}
