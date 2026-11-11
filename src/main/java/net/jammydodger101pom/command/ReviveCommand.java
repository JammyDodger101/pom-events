package net.jammydodger101pom.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.LevelData;

import java.util.Collection;
import java.util.Iterator;

public class ReviveCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("revive")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.players())).executes(commandContext -> {
                    return run(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"));
                }));
    }

    private static int run(CommandSourceStack context, Collection<? extends Player> players) throws CommandSyntaxException {
        Iterator playerIterator = players.iterator();

        ServerLevel level = context.getLevel();
        LevelData.RespawnData respawnData = level.getRespawnData();
        BlockPos respawnPos = respawnData.pos();

        while (playerIterator.hasNext()) {
            ServerPlayer player = (ServerPlayer) playerIterator.next();
            if (player.isSpectator()) {
                player.teleportTo(respawnPos.getX(), respawnPos.getY(), respawnPos.getZ());
                player.setGameMode(GameType.SURVIVAL);
            }
        }

        return 1;
    }
}
