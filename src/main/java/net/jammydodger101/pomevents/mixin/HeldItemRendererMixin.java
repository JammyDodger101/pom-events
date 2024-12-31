package net.jammydodger101.pomevents.mixin;

import net.jammydodger101.pomevents.item.ModItems;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @Inject(method = "isChargedCrossbow", at = @At("HEAD"), cancellable = true)
    private static void withPillagerCrossbow(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        boolean isCrossbow = stack.isOf(Items.CROSSBOW) || stack.isOf(ModItems.PILLAGER_CROSSBOW);
        cir.setReturnValue(isCrossbow && CrossbowItem.isCharged(stack));
    }

    @Redirect(method = "renderFirstPersonItem",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z")
    , slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderMapInOneHand(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IFLnet/minecraft/util/Arm;FLnet/minecraft/item/ItemStack;)V"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;isCharged(Lnet/minecraft/item/ItemStack;)Z")
    )
    )
    private boolean changeIfPillagerCrossbow(ItemStack instance, Item item) {

        return instance.isOf(Items.CROSSBOW) || instance.isOf(ModItems.PILLAGER_CROSSBOW);
    }
}
