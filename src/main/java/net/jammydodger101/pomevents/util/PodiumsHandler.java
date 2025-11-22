package net.jammydodger101.pomevents.util;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/*
Handles podiums
UNUSED
 */
public class PodiumsHandler {
    public static List<BlockPos> podiumLocs = new ArrayList<>();

    public static void addPodiumLocation(BlockPos blockPos) {
        podiumLocs.add(blockPos);
    }

    public static List<BlockPos> getPodiumLocs() {
        return podiumLocs;
    }
}
