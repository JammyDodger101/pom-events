package net.jammydodger101.pomevents.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            WithdrawHeartCommand.register(dispatcher);
            ReviveCommand.register(dispatcher);
            LockItemFromCraftingCommand.register(dispatcher);
            UnlockItemFromCraftingCommand.register(dispatcher);
        });
    }
}
