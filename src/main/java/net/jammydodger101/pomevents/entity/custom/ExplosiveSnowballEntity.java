package net.jammydodger101.pomevents.entity.custom;

import net.jammydodger101.pomevents.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class ExplosiveSnowballEntity extends PersistentProjectileEntity {

    public ExplosiveSnowballEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public ExplosiveSnowballEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.EXPLOSIVE_SNOWBALL, livingEntity, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return Items.SNOWBALL.getDefaultStack();
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, (byte)3);
            this.getWorld().createExplosion(null, this.getWorld().getDamageSources().freeze(), null, this.getPos(), 0.5f, false, World.ExplosionSourceType.TNT);
        }

        this.discard();
        super.onBlockHit(blockHitResult);
    }
}
