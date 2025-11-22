package net.jammydodger101.pomevents.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.jammydodger101.pomevents.util.PodiumsHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/*
CURRENTLY NOT IN USE
Adds the current position that the player is at to an array that holds positions of podiums
 */
public class NewPodiumLocationCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("podiums_add")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .executes(NewPodiumLocationCommand::run));

    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player != null) {
            return addPodiumAddPlayerPos(player);
        }
        return 0;
    }

    // Adds the current position to the podium locations array
    private static int addPodiumAddPlayerPos(ServerPlayerEntity player) {
        PodiumsHandler.addPodiumLocation(player.getBlockPos());
        Text message = Text.of(("podium added at " + player.getBlockPos().toShortString()));
        player.sendMessage(message);
        return 1;
    }
}
