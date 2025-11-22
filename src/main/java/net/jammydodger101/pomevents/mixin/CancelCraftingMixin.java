package net.jammydodger101.pomevents.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.jammydodger101.pomevents.util.CraftingHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
Makes sure that items that are locked do not appear in crafting screen results
 */
@Mixin(CraftingScreenHandler.class)
public class CancelCraftingMixin {
    // Forces the function to cancel if the item is in the list of locked items
    @Inject(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"), cancellable = true)
    private static void cancelLegendaryCrafting(ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory, RecipeEntry<CraftingRecipe> recipe, CallbackInfo ci, @Local ItemStack itemStack) {
        if (CraftingHandler.craftedItems.contains(itemStack.getItem())) {
            ci.cancel();
        }
    }
}
