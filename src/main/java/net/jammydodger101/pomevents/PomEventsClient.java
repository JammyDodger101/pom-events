package net.jammydodger101.pomevents;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.jammydodger101.pomevents.entity.ModEntities;
import net.jammydodger101.pomevents.entity.client.ModEntityModelLayers;
import net.jammydodger101.pomevents.entity.client.SculkTridentEntityRenderer;
import net.jammydodger101.pomevents.entity.client.SculkTridentProjectileModel;
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

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SCULK_TRIDENT, SculkTridentProjectileModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.SCULK_TRIDENT, SculkTridentEntityRenderer::new);

    }
}
