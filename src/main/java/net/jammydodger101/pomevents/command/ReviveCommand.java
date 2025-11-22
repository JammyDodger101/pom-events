package net.jammydodger101.pomevents.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

import java.util.List;

/*
Revives all "dead" players (players in spectator) by setting their gamemode to survival and putting them at the world spawn
 */
public class ReviveCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("revive")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .executes(ReviveCommand::run));

    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerWorld world = context.getSource().getWorld();
        reviveSpectatorPlayersAtWorldSpawn(world);
        return 1;
    }

    // Teleports every spectator to the world spawn and sets them to survival
    private static void reviveSpectatorPlayersAtWorldSpawn(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers();
        BlockPos revivePos = world.getSpawnPos();
        float yaw = world.getSpawnAngle();

        for (ServerPlayerEntity player : players) {
            if (player.isSpectator()) {
                player.teleport(world, revivePos.getX(), revivePos.getY(), revivePos.getZ(), yaw, 0f);
                player.changeGameMode(GameMode.SURVIVAL);
            }
        }
    }
}
