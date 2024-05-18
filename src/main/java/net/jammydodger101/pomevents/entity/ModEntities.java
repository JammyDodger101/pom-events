package net.jammydodger101.pomevents.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.entity.custom.ExplosiveSnowballEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<ExplosiveSnowballEntity> EXPLOSIVE_SNOWBALL = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(PomEvents.MOD_ID, "explosive_snowball"),
            FabricEntityTypeBuilder.<ExplosiveSnowballEntity>create(SpawnGroup.MISC, ExplosiveSnowballEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());
}
