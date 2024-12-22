package net.jammydodger101.pomevents;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.jammydodger101.pomevents.event.SwitchModeHandler;
import net.minecraft.util.Hand;

public class PomEventsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // KeyInputHandler.register();
        ClientPreAttackCallback.EVENT.register((client, player, clickCount) -> {
            Hand hand = player.getActiveHand();
            if (clickCount != 0) {
                SwitchModeHandler.handleInputs(player, hand);
            }

            return false;
        });
    }
}
