package net.jammydodger101.pomevents.mixin;

import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.item.ModItems;
import net.jammydodger101.pomevents.item.custom.GaleBow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class GaleFallDamageMixin {
	@Inject(at = @At("HEAD"), method = "handleFallDamage", cancellable = true)
	private void init(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		PlayerEntity entity = (PlayerEntity)(Object) this;
		Item stack = ModItems.GALE_BOW;
		Item heldItemMain = entity.getStackInHand(entity.getActiveHand()).getItem();
		Item heldItemOff = entity.getOffHandStack().getItem();
		boolean holdingGale = heldItemMain == stack || heldItemOff == stack;
		//PomEvents.LOGGER.info(String.valueOf(holdingGale));
		if (holdingGale) {
			cir.setReturnValue(false);
		}
	}
}