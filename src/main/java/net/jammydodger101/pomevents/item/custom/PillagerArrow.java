package net.jammydodger101.pomevents.item.custom;

import net.jammydodger101.pomevents.entity.custom.PillagerArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PillagerArrow extends ArrowItem {
    public PillagerArrow(Item.Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new PillagerArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom, true);
    }

    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        PillagerArrowEntity spectralArrowEntity = new PillagerArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), (ItemStack)null);
        spectralArrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        return spectralArrowEntity;
    }
}