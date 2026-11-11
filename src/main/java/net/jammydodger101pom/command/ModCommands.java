package net.jammydodger101pom.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void init() {
        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandBuildContext, commandSelection) -> {
            LockCraftingItemCommand.register(commandDispatcher);
            ReviveCommand.register(commandDispatcher);
            UnlockCraftingItemCommand.register(commandDispatcher);
        }));
    }
}
