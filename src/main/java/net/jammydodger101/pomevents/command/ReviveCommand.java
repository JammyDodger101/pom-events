package net.jammydodger101.pomevents.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.util.PodiumsHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.List;

public class ReviveCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("revive")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .executes(ReviveCommand::run));

    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        List<ServerPlayerEntity> players = context.getSource().getWorld().getPlayers();
        ServerWorld world = context.getSource().getWorld();
        BlockPos revivePos = new BlockPos(0, 80, 0);

        for (ServerPlayerEntity player : players) {
            if (player.isSpectator()) {
                player.teleport(world, revivePos.getX(), revivePos.getY(), revivePos.getZ(), 0f, 0f);
                player.changeGameMode(GameMode.SURVIVAL);
            }

        }

        return 1;

    }
}
