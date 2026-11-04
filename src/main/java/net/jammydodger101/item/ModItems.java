package net.jammydodger101.item;

import net.jammydodger101.PomEvents2;
import net.jammydodger101.item.custom.DragonWing;
import net.jammydodger101.item.custom.PrismarineAnchor;
import net.jammydodger101.item.custom.SculkTrident;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;

import java.util.function.Function;

public class ModItems {
    public static final Item CROCKSMARTER_BLADE = registerItem("crocksmarter_blade",
            properties -> new Item(properties.sword(ModToolMaterials.TEST_MATERIAL, 99999, 0).fireResistant()));
    public static final Item SCULK_TRIDENT = registerItem("sculk_trident",
            properties -> new SculkTrident(properties.stacksTo(1).attributes(ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (double)(6), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, (double)-2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()).rarity(Rarity.EPIC)));
    public static final Item DRAGON_WING = registerItem("dragon_wing",
            properties -> new DragonWing(properties.stacksTo(1).attributes(ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (double)(10), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, (double)-2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()).rarity(Rarity.EPIC)));
    public static final Item WARDEN_HEART = registerItem("warden_heart",
            properties -> new Item(properties.stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item PRISMARINE_ANCHOR = registerItem("prismarine_anchor",
            properties -> new PrismarineAnchor(properties.stacksTo(1).attributes(ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (double)(9), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, (double)-3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()).rarity(Rarity.EPIC)));

    public static ResourceKey<Item> getRK(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).get();
    }

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(PomEvents2.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(PomEvents2.MOD_ID, name)))));
    }

    public static void registerModItems() {
        PomEvents2.LOGGER.info("Registering items");
    }
}
