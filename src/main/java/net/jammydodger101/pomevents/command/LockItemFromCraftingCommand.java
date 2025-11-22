package net.jammydodger101.pomevents.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.jammydodger101.pomevents.util.CraftingHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/*
Means that the item that the player executing the command is holding can no longer be crafted
 */
public class LockItemFromCraftingCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("lock_crafting")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .executes(LockItemFromCraftingCommand::run));

    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player != null) {
            return lockHeldItemFromCrafting(player);
        }
        return 0;
    }

    // Adds the item to the crafted items array
    private static int lockHeldItemFromCrafting(ServerPlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        if (stack != null) {
            CraftingHandler.craftedItems.add(stack.getItem());
            Text message = Text.of((stack.getName().getString() + " can no longer be crafted"));
            player.sendMessage(message);
            return 1;
        }
        player.sendMessage(Text.of("You are not holding an item."));
        return 0;
    }
}
