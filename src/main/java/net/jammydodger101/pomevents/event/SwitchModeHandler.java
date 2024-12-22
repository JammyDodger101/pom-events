package net.jammydodger101.pomevents.event;

import net.jammydodger101.pomevents.component.ModDataComponents;
import net.jammydodger101.pomevents.item.ModItems;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class SwitchModeHandler {

    public static void handleInputs(PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);

        if (!heldItem.isEmpty()) {
            if (heldItem.isOf(ModItems.GALE_BOW)) {
                switchModes(heldItem, (ClientPlayerEntity) player);
            }
        }
    }

    public static void switchModes(ItemStack stack, ClientPlayerEntity player) {
        if (stack.get(ModDataComponents.MODE) != null) {
            if (stack.get(ModDataComponents.MODE) == 1) {
                stack.set(ModDataComponents.MODE, 0);
                player.sendMessage(Text.of("Updated to arrow mode"), true);
            } else {
                player.sendMessage(Text.of("Updated to wind charge mode"), true);
                stack.set(ModDataComponents.MODE, 1);
            }
        } else {
            //player.sendMessage(Text.of("Updated to wind charge mode"));
            stack.set(ModDataComponents.MODE, 1);
        }
    }
}
