package net.jammydodger101.pomevents.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;

public class CrocksmarterBlade extends SwordItem {
    public CrocksmarterBlade(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        BannedPlayerList bannedPlayerList = Objects.requireNonNull(target.getServer()).getPlayerManager().getUserBanList();
        if (target instanceof ServerPlayerEntity) {
            BannedPlayerEntry bannedPlayerEntry = new BannedPlayerEntry(((ServerPlayerEntity) target).getGameProfile());
            bannedPlayerList.add(bannedPlayerEntry);
            ((ServerPlayerEntity) target).networkHandler.disconnect(Text.of("Banned using crocksmarter blade... yikes..."));
            return true;
        }
        target.remove(Entity.RemovalReason.DISCARDED);
        return true;
    }


}
