package net.jammydodger101.pomevents.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.jammydodger101.pomevents.util.PodiumsHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

/*
CURRENTLY NOT IN USE
Teleports all players in turn to different locations (podiums)
 */
public class TeleportPodiumsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("podiums_tp")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .executes(TeleportPodiumsCommand::run));

    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerWorld world = context.getSource().getWorld();
        return teleportPlayersToPodiums(world);
    }

    // Teleports every player to a podium
    private static int teleportPlayersToPodiums(ServerWorld world) {
        List<BlockPos> locs = PodiumsHandler.getPodiumLocs();
        List<ServerPlayerEntity> players = world.getPlayers();

        if (players.size() > locs.size()) {
            return -1;
        } else {
            for (int i = 0; i < players.size(); i++) {
                BlockPos podiumBlockPos = locs.get(i);
                Vec3d podiumPos3D = podiumBlockPos.toBottomCenterPos();
                players.get(i).teleport(world, podiumPos3D.getX(), podiumPos3D.getY(), podiumPos3D.getZ(), 0f, 0f);
            }
            return 1;
        }
    }
}
