package net.jammydodger101.pomevents.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class DragonWing extends AxeItem {


    public DragonWing(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        use(context.getWorld(),context.getPlayer(),context.getHand());
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            for (int i = 0; i < 16; ++i) {
                SoundCategory soundCategory;
                SoundEvent soundEvent;
                double d = user.getX() + (user.getRandom().nextDouble() - 0.5) * 16.0;
                double e = MathHelper.clamp(user.getY() + (double)(user.getRandom().nextInt(16) - 8), (double)world.getBottomY(), (double)(world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1));
                double f = user.getZ() + (user.getRandom().nextDouble() - 0.5) * 16.0;
                if (user.hasVehicle()) {
                    user.stopRiding();
                }
                Vec3d vec3d = user.getPos();
                if (!user.teleport(d, e, f, true)) continue;
                world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(user));
                soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                soundCategory = SoundCategory.PLAYERS;

                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, soundCategory, 1f, 1f);
                user.onLanding();
                break;
            }
            if (user instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity)user;
                playerEntity.getItemCooldownManager().set(this, 50);
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Vec3d vec3d = attacker.getPos().subtract(target.getPos());
        double d = -1f;
        Vec3d vec3d2 = vec3d.normalize().multiply(d);
        target.velocityModified = true;
        target.addVelocity(vec3d2.x, 0.5f, vec3d2.z);
        return super.postHit(stack, target, attacker);
    }
}
