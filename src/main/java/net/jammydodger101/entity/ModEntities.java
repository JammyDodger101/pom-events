package net.jammydodger101.entity;

import net.jammydodger101.PomEvents2;
import net.jammydodger101.entity.custom.ThrownPrismarineAnchor;
import net.jammydodger101.entity.custom.ThrownSculkTrident;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final ResourceKey<EntityType<?>> SCULK_TRIDENT_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(PomEvents2.MOD_ID, "sculk_trident"));

    public static final EntityType<ThrownSculkTrident> SCULK_TRIDENT = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(PomEvents2.MOD_ID, "sculk_trident"),
            EntityType.Builder.<ThrownSculkTrident>of(ThrownSculkTrident::new, MobCategory.MISC).noLootTable()
                    .build(SCULK_TRIDENT_KEY));

    public static final ResourceKey<EntityType<?>> PRISMARINE_ANCHOR_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(PomEvents2.MOD_ID, "prismarine_anchor"));

    public static final EntityType<ThrownPrismarineAnchor> PRISMARINE_ANCHOR = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(PomEvents2.MOD_ID, "prismarine_anchor"),
            EntityType.Builder.<ThrownPrismarineAnchor>of(ThrownPrismarineAnchor::new, MobCategory.MISC).noLootTable()
                    .build(PRISMARINE_ANCHOR_KEY));

    public static void registerModEntities() {
        PomEvents2.LOGGER.info("Registering mod entities");
    }
}

