package net.jammydodger101.pomevents.item.custom;

import net.jammydodger101.pomevents.particle.ParticleSpawner;
import net.jammydodger101.pomevents.util.CraftingHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Collections;
import java.util.Set;

/*
A legendary axe that launches hit players and causes the player to teleport when used
 */
public class DragonWing extends AxeItem {
    double largeHPower = -2f;
    double largeVPower = 1f;
    int maxTpRange = 500;

    public DragonWing(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        use(context.getWorld(),context.getPlayer(),context.getHand());
        return ActionResult.PASS;
    }

    // Teleports the player when used
    // If there are players nearby, they teleport to the nearest player
    // If there are not players nearby, they teleport like eating a chorus fruit
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            if (!teleportToClosestPlayer(user, ((ServerWorld) world))) {
                chorusTeleport(user, ((ServerWorld) world));
            }
            ((ServerWorld) world).spawnParticles(ParticleTypes.PORTAL, user.getX(), user.getY(), user.getZ(), 10, 0,0,0,3f);
            user.getItemCooldownManager().set(user.getStackInHand(hand).getItem(), 30);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    // Sends the target flying when hit with no cooldown
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = attacker.getWorld();

        ServerWorld serverWorld = ((ServerWorld) world);

        if (!((PlayerEntity) attacker).getItemCooldownManager().isCoolingDown(this)) {
            if (target instanceof ServerPlayerEntity serverPlayerTarget) {
                serverPlayerTarget.setSpawnExtraParticlesOnFall(true);
            }

            addFlairs(serverWorld, target);
            launchEntity(target, attacker, largeHPower, largeVPower);

            ((PlayerEntity) attacker).getItemCooldownManager().set(this, 300);
        }

        return super.postHit(stack, target, attacker);
    }

    // Adds sound effects and particles
    private void addFlairs(ServerWorld serverWorld, LivingEntity target) {
        serverWorld.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS);
        serverWorld.playSound(null, target.getBlockPos(), SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY, SoundCategory.PLAYERS, 2f, 0.8f);
        ParticleSpawner.spawnSunflowerParticles(ParticleTypes.CLOUD, 150, serverWorld, target.getPos(), 0.3f, 0.5f, true);
        ParticleSpawner.createDustPillar(Blocks.CRYING_OBSIDIAN.getDefaultState(), target.getWorld(), target.getPos(), 1, 100, 3);
    }

    // Sends the target flying where the attacker is looking
    public void launchEntity(LivingEntity target, LivingEntity attacker, double hPower, double vPower) {
        Vec3d vec3d = attacker.getPos().subtract(target.getPos());
        Vec3d vec3d2 = vec3d.normalize().multiply(hPower);
        target.velocityModified = true;
        target.addVelocity(vec3d2.x, vPower, vec3d2.z);
    }

    // Teleports similar to how the player teleports when eating a chorus fruit
    private void chorusTeleport(PlayerEntity user, ServerWorld world) {
        for (int i = 0; i < 16; ++i) {
            double randomPosX = user.getX() + (user.getRandom().nextDouble() - 0.5) * 16.0;
            double randomPosY = MathHelper.clamp(user.getY() + (double)(user.getRandom().nextInt(16) - 8), (double)world.getBottomY(), (double)(world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1));
            double randomPosZ = user.getZ() + (user.getRandom().nextDouble() - 0.5) * 16.0;

            if (user.hasVehicle()) {
                user.stopRiding();
            }
            Vec3d vec3d = user.getPos();
            if (!user.teleport(randomPosX, randomPosY, randomPosZ, true)) continue;

            world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(user));

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);
            user.onLanding();
            break;
        }
    }

    // Teleport to the nearest player within range
    private boolean teleportToClosestPlayer(PlayerEntity user, ServerWorld world) {
        PlayerEntity closestPlayer = world.getClosestPlayer(user.getX(), user.getY(), user.getZ(), maxTpRange, true);
        if (closestPlayer == null) {
            return false;
        }
        user.teleport(world, closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ(), Collections.emptySet(), user.getYaw(), user.getPitch());

        world.emitGameEvent(GameEvent.TELEPORT, closestPlayer.getPos(), GameEvent.Emitter.of(user));

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);
        user.onLanding();
        return true;
    }

    // Ensures that multiple cannot be crafted
    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        CraftingHandler.craftedItem(stack, world, player);
    }
}


