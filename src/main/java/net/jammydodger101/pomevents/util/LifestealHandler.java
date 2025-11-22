package net.jammydodger101.pomevents.util;

import net.jammydodger101.pomevents.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/*
Handles gaining and losing lifesteal hearts
 */
public class LifestealHandler {
    static double maxHealth = 40;

    // Increases the player's health unless they are at max health, where the heart item is given to the player instead
    public static void increaseHealth(LivingEntity user, Integer amount) {
        if (user.getMaxHealth() < maxHealth) {
            if ((user.getMaxHealth() + amount) <= maxHealth) {
                user.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(user.getMaxHealth()+amount);
            }
        } else {
            if (user instanceof ServerPlayerEntity player) {
                boolean wasAdded = player.getInventory().insertStack(ModItems.LIFESTEAL_HEART.getDefaultStack());
                if (!wasAdded) {
                    player.dropItem(ModItems.LIFESTEAL_HEART.getDefaultStack(),false);
                }
            }
        }

    }

    // Decreases the player's health
    // No checks are done as that should be handled by the mixin
    public static void decreaseHealth(LivingEntity user, Integer amount) {
        if (user.getMaxHealth() > 0f) {
            if ((user.getMaxHealth() - amount) > 0f) {
                user.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(user.getMaxHealth() - amount);
            }
        } else {
            // Code should really never reach here
            user.sendMessage(Text.literal("banned"));
        }
    }
}
