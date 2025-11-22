package net.jammydodger101.pomevents.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.jammydodger101.pomevents.item.ModItems;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

/*
Withdraws a heart from the player (yes like lifesteal)
 */
public class WithdrawHeartCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("withdraw")
                .executes(WithdrawHeartCommand::run));

    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player != null) {
            return withdrawHeart(player);
        }
        return 0;
    }

    // Removes the heart from the player's health, gives them the item and updates any inconsistencies
    private static int withdrawHeart(ServerPlayerEntity player) {
        if (playerHasSufficientHealth(player)) {
            removeMaxHealthFromPlayer(player);
            attemptToGivePlayerHeartItem(player);
            removeResidualHealth(player);
            return 1;
        }
        return -1;
    }

    // Checks if the player will be on more than 0 health if they lose a heart
    private static boolean playerHasSufficientHealth(ServerPlayerEntity player) {
        if (player.getMaxHealth() > 0f) {
            return (player.getMaxHealth() - 2) > 0f;
        }
        return false;
    }

    // Removes a heart from the player (hearts are worth 2 health)
    private static void removeMaxHealthFromPlayer(ServerPlayerEntity player) {
        EntityAttributeInstance playerMaxHealthAttribute = player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (playerMaxHealthAttribute != null) {
            playerMaxHealthAttribute.setBaseValue(player.getMaxHealth() - 2);
        }
    }

    // Tries to give the player the heart item
    // If the player's inventory is full, the heart is dropped
    private static void attemptToGivePlayerHeartItem(ServerPlayerEntity player) {
        boolean wasAdded = player.getInventory().insertStack(ModItems.LIFESTEAL_HEART.getDefaultStack());
        if (!wasAdded) {
            player.dropItem(ModItems.LIFESTEAL_HEART.getDefaultStack(),false);
        }
    }

    // When max health is depleted and the player was at full health, the health bar does not update correctly
    // This removes that extra health to make sure that there are no visual inconsistencies
    private static void removeResidualHealth(ServerPlayerEntity player) {
        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }
}
