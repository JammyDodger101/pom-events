package net.jammydodger101.pomevents.entity.client;

import net.jammydodger101.pomevents.PomEvents;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModEntityModelLayers {

    public static final EntityModelLayer SCULK_TRIDENT =
            new EntityModelLayer(Identifier.of(PomEvents.MOD_ID, "sculk_trident"), "main");
}
