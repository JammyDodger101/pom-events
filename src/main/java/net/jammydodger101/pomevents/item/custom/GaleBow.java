package net.jammydodger101.pomevents.item.custom;

import net.jammydodger101.pomevents.component.ModDataComponents;
import net.jammydodger101.pomevents.entity.custom.GaleBowArrowEntity;
import net.jammydodger101.pomevents.entity.custom.PillagerArrowEntity;
import net.jammydodger101.pomevents.mixin.GaleFallDamageMixin;
import net.jammydodger101.pomevents.particle.ParticleSpawner;
import net.jammydodger101.pomevents.util.CraftingHandler;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.WindChargeItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class GaleBow extends RangedWeaponItem {
    public static boolean holdingBow = false;
    public int bowMode = 0;

    public static final Predicate<ItemStack> GALE_PROJECTILES = (itemStack -> {
        return itemStack.isOf(Items.WIND_CHARGE);
    });

    public GaleBow(Settings settings) {
        super(settings);
        // settings.component(ModDataComponents.MODE, 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity) {
            holdingBow = ((PlayerEntity) entity).getStackInHand(((PlayerEntity) entity).getActiveHand()) == stack || ((PlayerEntity) entity).getStackInHand(Hand.OFF_HAND) == stack;
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean bl = !user.getProjectileType(itemStack).isEmpty();
        if (!user.isInCreativeMode() && !bl) {
            return TypedActionResult.fail(itemStack);
        } else if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            ItemStack itemStack = playerEntity.getProjectileType(stack);
            if (!itemStack.isEmpty()) {
                int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
                float f = getPullProgress(i);
                if (!((double) f < 0.01)) {
                    List<ItemStack> list = load(stack, itemStack, playerEntity);
                    if (world instanceof ServerWorld serverWorld) {
                        if (!list.isEmpty()) {

                            launchLivingEntities((ServerWorld) world, user);
                            ((PlayerEntity) user).getItemCooldownManager().set(this, 100);
                            windEffect(playerEntity.getBlockPos(), serverWorld);
                            this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list, f * 3.0f, 1.0f, f == 1.0F, ((LivingEntity) null));
                            world.playSound((PlayerEntity)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        }
                    }

                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }


    private void windEffect(BlockPos center, ServerWorld world) {
        float speed = 10f;
        float radius = 5f;

        ParticleSpawner.spawnCircleCircumferenceParticles(ParticleTypes.GUST, 20, world, center.toCenterPos(), radius, speed, 0.4);
    }

    private void launchLivingEntities(ServerWorld world, LivingEntity user) {
        List<Entity> entities = world.getOtherEntities(user, user.getBoundingBox().expand(5, 0, 5));

        double d = (user.getBoundingBox().minX + user.getBoundingBox().maxX) / 2.0;
        double e = (user.getBoundingBox().minZ + user.getBoundingBox().maxZ) / 2.0;

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                double f = entity.getX() - d;
                double g = entity.getZ() - e;
                double h = Math.max(f * f + g * g, 0.05);
                entity.velocityModified = true;
                entity.addVelocity(f / h * 8.0, 0.8f, g / h * 8.0);
            }
        }
    }

    @Override
    protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        return new GaleBowArrowEntity(world, shooter, weaponStack, projectileStack);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;

    }

    @Override
    public int getRange() {
        return 0;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw() + yaw, 0.0f, speed, divergence);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        CraftingHandler.craftedItem(stack, world, player);
    }


}
