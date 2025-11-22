package net.jammydodger101.pomevents.entity;

import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.entity.custom.SculkTridentEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/*
Registers entities
 */
public class ModEntities {
    public static final EntityType<SculkTridentEntity> SCULK_TRIDENT = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(PomEvents.MOD_ID, "sculk_trident"),
            EntityType.Builder.<SculkTridentEntity>create(SculkTridentEntity::new, SpawnGroup.MISC)
                    .dimensions(1f, 1f).build());
}

