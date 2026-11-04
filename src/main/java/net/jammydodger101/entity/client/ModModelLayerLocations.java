package net.jammydodger101.entity.client;

import net.jammydodger101.PomEvents2;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModModelLayerLocations {

    public static final ModelLayerLocation SCULK_TRIDENT =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(PomEvents2.MOD_ID, "sculk_trident"), "main");
    public static final ModelLayerLocation PRISMARINE_ANCHOR =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(PomEvents2.MOD_ID, "prismarine_anchor"), "main");
}
