package net.jammydodger101.pomevents.mixin;

import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.item.ModItems;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "getArmPose", at = @At(value = "HEAD"), cancellable = true)
    private static void pillagerCrossbowHolding(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isEmpty()) {
            // PomEvents.LOGGER.info("a");
            if (player.getActiveHand() == hand) {
                // PomEvents.LOGGER.info(String.valueOf(player.getItemUseTimeLeft()));
                if (player.getItemUseTimeLeft() == 0) {
                    // PomEvents.LOGGER.info("e???");
                    if (!player.handSwinging && itemStack.isOf(ModItems.PILLAGER_CROSSBOW) && CrossbowItem.isCharged(itemStack)) {
                        // PomEvents.LOGGER.info("houston");
                        cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
                    }
                } else if (player.getItemUseTime() > 0) {
                    //PomEvents.LOGGER.info("c");
                    if (itemStack.isOf(ModItems.PILLAGER_CROSSBOW)) {
                        cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_CHARGE);
                    } else if (itemStack.isOf(ModItems.GALE_BOW)) {
                        cir.setReturnValue(BipedEntityModel.ArmPose.BOW_AND_ARROW);
                    }
                }
            }
        }
    }

}
