package net.jammydodger101.pomevents.item.custom;

import net.jammydodger101.pomevents.PomEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.*;

public class ChunkShifter extends Item {
    // Super buggy and doesn't work client-side on servers lol
    public static WorldChunk loadedChunk = null;

    public ChunkShifter(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (loadedChunk == null && !world.isClient) {
            loadedChunk = (WorldChunk) world.getChunk(user.getBlockPos());
        } else if (loadedChunk != null) {
            swapChunks((WorldChunk) world.getChunk(user.getBlockPos()));
            if (!world.isClient) {
                loadedChunk = null;
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public void swapChunks(WorldChunk currentChunk){
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = -64; y < 320; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    currentChunk.setBlockState(pos, loadedChunk.getBlockState(pos), true);
                }
            }
        }
        currentChunk.getHeightmap(Heightmap.Type.WORLD_SURFACE).setTo(loadedChunk, Heightmap.Type.WORLD_SURFACE, loadedChunk.getHeightmap(Heightmap.Type.WORLD_SURFACE).asLongArray());
        currentChunk.getChunkSkyLight().refreshSurfaceY(currentChunk);
    }
}
