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

public class UnlockItemFromCraftingCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("unlock_crafting")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .executes(UnlockItemFromCraftingCommand::run));

    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        ItemStack stack = player.getMainHandStack();
        if (stack != null) {
            CraftingHandler.craftedItems.remove(stack.getItem());
            Text message = Text.of((stack.getName().getString() + " can now be crafted"));
            player.sendMessage(message);
            return 1;
        }
        player.sendMessage(Text.of("you aint holdin an item"));
        return 0;
    }
}
