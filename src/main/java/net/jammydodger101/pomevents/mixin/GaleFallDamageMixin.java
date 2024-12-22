package net.jammydodger101.pomevents.mixin;

import net.jammydodger101.pomevents.item.custom.GaleBow;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
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
		if (GaleBow.holdingBow) {
			cir.setReturnValue(false);
		}
	}
}