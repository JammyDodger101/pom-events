package net.jammydodger101.pomevents.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.jammydodger101.pomevents.util.PodiumsHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class NewPodiumLocationCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("podiums_add")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .executes(NewPodiumLocationCommand::run));

    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        PodiumsHandler.addPodiumLocation(player.getBlockPos());
        Text message = Text.of(("podium added at " + player.getBlockPos().toShortString()));
        player.sendMessage(message);
        return 1;
    }
}
