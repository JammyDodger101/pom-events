package net.jammydodger101pom.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.jammydodger101pom.util.CraftingHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class LockCraftingItemCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lock")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes((commandContext -> {return run(commandContext.getSource());})));
    }

    private static int run(CommandSourceStack context) throws CommandSyntaxException {
        ServerPlayer player = context.getPlayer();
        if (player != null) {
            return lockHeldItemFromCrafting(player, context);
        }
        return 0;
    }

    private static int lockHeldItemFromCrafting(ServerPlayer player, CommandSourceStack context) {
        ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (item != null) {
            CraftingHandler.lockedItems.add(item.getItem());
            context.sendSuccess(() -> {return Component.literal((item.getItemName().getString()) + " is locked from crafting.");}, true);
            return 1;
        }
        player.sendSystemMessage(Component.literal("An error occurred (are you holding an item?)"));
        return 0;
    }
}
