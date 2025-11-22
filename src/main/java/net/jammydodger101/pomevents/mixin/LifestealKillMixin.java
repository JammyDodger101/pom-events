package net.jammydodger101.pomevents.mixin;

import com.mojang.authlib.GameProfile;
import net.jammydodger101.pomevents.util.LifestealHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
Handles the exchange of hearts and banning of players when they are killed by another player
 */
@Mixin(ServerPlayerEntity.class)
public abstract class LifestealKillMixin extends PlayerEntity {
    public LifestealKillMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    // Sets players to spec and does lifesteal stuff
    @Inject(method = "onDeath", at = @At("TAIL"))
    private void lifesteal(DamageSource damageSource, CallbackInfo ci) {
        Entity attacker = damageSource.getAttacker();
        ServerPlayerEntity deadEntity = (ServerPlayerEntity)(Object)this;
        deadEntity.changeGameMode(GameMode.SPECTATOR);
        if (attacker instanceof ServerPlayerEntity) {
            exchangeHealth(attacker, deadEntity);
            banPlayerIfOutOfHealth(deadEntity);
        }
    }

    // Increases health of attacker and decreases health of victim
    @Unique
    private void exchangeHealth(Entity attacker, ServerPlayerEntity deadEntity) {
        LifestealHandler.increaseHealth((LivingEntity) attacker, 2);
        LifestealHandler.decreaseHealth(deadEntity, 2);
    }

    // Bans the dead player if removing a heart puts them on 0 health
    @Unique
    private void banPlayerIfOutOfHealth(ServerPlayerEntity deadEntity) {
        if (deadEntity.getMaxHealth() == 2) {
            BannedPlayerList bannedPlayerList = deadEntity.getServerWorld().getServer().getPlayerManager().getUserBanList();
            BannedPlayerEntry bannedPlayerEntry = new BannedPlayerEntry(deadEntity.getGameProfile(),
                    null,
                    deadEntity.getName().getString(),
                    null,
                    "You ran out of hearts!");
            bannedPlayerList.add(bannedPlayerEntry);
            deadEntity.networkHandler.disconnect(Text.translatable("multiplayer.disconnect.banned"));
        }
    }
}
