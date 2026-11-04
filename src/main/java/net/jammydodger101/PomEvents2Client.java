package net.jammydodger101;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.jammydodger101.entity.ModEntities;
import net.jammydodger101.entity.client.ModModelLayerLocations;
import net.jammydodger101.entity.client.prismarine_anchor.PrismarineAnchorModel;
import net.jammydodger101.entity.client.prismarine_anchor.PrismarineAnchorRenderer;
import net.jammydodger101.entity.client.sculk_trident.SculkTridentModel;
import net.jammydodger101.entity.client.sculk_trident.SculkTridentRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class PomEvents2Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLayerRegistry.registerModelLayer(ModModelLayerLocations.SCULK_TRIDENT, SculkTridentModel::createBodyLayer);
        EntityRenderers.register(ModEntities.SCULK_TRIDENT, SculkTridentRenderer::new);

        ModelLayerRegistry.registerModelLayer(ModModelLayerLocations.PRISMARINE_ANCHOR, PrismarineAnchorModel::createBodyLayer);
        EntityRenderers.register(ModEntities.PRISMARINE_ANCHOR, PrismarineAnchorRenderer::new);
    }
}
