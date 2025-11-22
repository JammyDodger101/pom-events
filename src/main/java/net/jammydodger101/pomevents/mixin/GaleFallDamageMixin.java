package net.jammydodger101.pomevents.mixin;

import net.jammydodger101.pomevents.item.ModItems;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
Ensures that the player does not take fall damage if holding the gale bow
Only works sometimes???
 */
@Mixin(PlayerEntity.class)
public class GaleFallDamageMixin {
	// Cancels the handle fall damage class if the player is holding gale bow
	@Inject(at = @At("HEAD"), method = "handleFallDamage", cancellable = true)
	private void init(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		PlayerEntity entity = (PlayerEntity)(Object) this;
		if (isHoldingGaleBow(entity)) {
			cir.cancel();
		}
	}

	// Checks if gale bow is in the main hand or off-hand
	@Unique
	private boolean isHoldingGaleBow(PlayerEntity entity) {
		return entity.getMainHandStack().getItem() == ModItems.GALE_BOW || entity.getOffHandStack().getItem() == ModItems.GALE_BOW;
	}
}