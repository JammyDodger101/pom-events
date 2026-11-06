package net.jammydodger101.mixin;

import net.jammydodger101.item.ModItems;
import net.jammydodger101.util.CraftingHandler;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStackTemplate.class)
public class CancelCraftingMixin {

    @Shadow Holder<Item> item;

    @Inject(method = "create", at = @At("HEAD"), cancellable = true)
    public void preventCreation(CallbackInfoReturnable<ItemStack> cir) {
        if (CraftingHandler.lockedItems.contains(item.value())) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}
