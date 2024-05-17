package net.jammydodger101.pomevents.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.item.custom.*;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {
    //public static final Item APPLE_JUICE = registerItem("apple_juice", new Item(new Item.Settings()));

    //public static final Item PORCUPINE_SPAWN_EGG = registerItem("porcupine_spawn_egg", new SpawnEggItem(ModEntities.PORCUPINE, 0xaaaaaa, 0x3b7f8f, new Item.Settings()));
    public static final Item CROCKSMARTER_BLADE = registerItem("crocksmarter_blade", new CrocksmarterBlade(ToolMaterials.DIAMOND, 2, -4f, new Item.Settings().rarity(Rarity.EPIC)));
    public static final Item LUCK_BLADE = registerItem("luck_blade", new LuckBlade(ToolMaterials.DIAMOND,3,-2.4f, new Item.Settings().rarity(Rarity.EPIC)));
    public static final Item DRAGON_WING = registerItem("dragon_wing", new DragonWing(ToolMaterials.IRON,5.0f,-3.0f, new Item.Settings().rarity(Rarity.EPIC)));
    public static final Item BLAZE_SHIELD = registerItem("blaze_shield", new BlazeShield(new Item.Settings().rarity(Rarity.EPIC).maxCount(1)));
    public static final Item SUMMONER_STAFF = registerItem("summoner_staff", new SummonerStaff(new Item.Settings().rarity(Rarity.EPIC).maxCount(1)));
    //public static final Item SCULK_TRIDENT = registerItem("sculk_trident", new SculkTrident(new Item.Settings().rarity(Rarity.EPIC).maxCount(1)));

    public static final Item LIFESTEAL_HEART = registerItem("lifesteal_heart", new LifestealHeartItem(new FabricItemSettings().rarity(Rarity.EPIC)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(PomEvents.MOD_ID, name), item);
    }

    public static void registerModItems() {
        PomEvents.LOGGER.info("Registering Mod Items for " + PomEvents.MOD_ID);
    }
}
