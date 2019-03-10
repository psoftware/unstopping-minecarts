package com.lecaldare.unstoppingminecarts;

import com.sun.org.apache.xpath.internal.operations.Bool;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ForcedChunksSaveData;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.ChunkLoader;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class CartChunkLoader {
    static CartChunkLoader instance = new CartChunkLoader();

    private CartChunkLoader() {
        //ForgeChunkManager.requestTicket
        //new ForcedChunksSaveData("ads").func_212438_a();
        //Minecraft.getInstance().world.
    }

    static CartChunkLoader getInstance() {
        return instance;
    }

    void cartMovedOnChunk(EntityUnstoppableMinecart cart, ChunkPos oldPos, ChunkPos newPos) {
        UnstoppingMinecarts.LOGGER.info("cartMovedOnChunk from " + oldPos.toString() + " to " + newPos.toString());
        if(oldPos.x != newPos.x && oldPos.z != newPos.z)
            UnstoppingMinecarts.LOGGER.info("Minecart moved diagonally or teleported? This should not happen");
        else if(oldPos.x != newPos.x) {
            if(oldPos.x < newPos.x) {   // Direction: >
                // Unload past chunks
                ForgeChunkLoader.forceLoadChunk(cart.world, oldPos.x-1, oldPos.z-1, false);
                ForgeChunkLoader.forceLoadChunk(cart.world, oldPos.x-1, oldPos.z, false);
                ForgeChunkLoader.forceLoadChunk(cart.world, oldPos.x-1, oldPos.z+1, false);

                // Load ahead chunks
                ForgeChunkLoader.forceLoadChunk(cart.world, newPos.x+1, newPos.z-1, true);
                ForgeChunkLoader.forceLoadChunk(cart.world, newPos.x+1, newPos.z, true);
                ForgeChunkLoader.forceLoadChunk(cart.world, newPos.x+1, newPos.z+1, true);
            } else {                     // Direction: <
                // Unload past chunks
                ForgeChunkLoader.forceLoadChunk(cart.world, oldPos.x+1, oldPos.z-1, false);
                ForgeChunkLoader.forceLoadChunk(cart.world, oldPos.x+1, oldPos.z, false);
                ForgeChunkLoader.forceLoadChunk(cart.world, oldPos.x+1, oldPos.z+1, false);

                // Load ahead chunks
                ForgeChunkLoader.forceLoadChunk(cart.world, newPos.x-1, newPos.z-1, true);
                ForgeChunkLoader.forceLoadChunk(cart.world, newPos.x-1, newPos.z, true);
                ForgeChunkLoader.forceLoadChunk(cart.world, newPos.x-1, newPos.z+1, true);
            }

        } else if(oldPos.z != newPos.z) {
            if(oldPos.z < newPos.z) {   // Direction: ^
                // Unload past chunks
                ForgeChunkLoader.forceLoadChunk(cart.world, oldPos.x-1, oldPos.z-1, false);
                ForgeChunkLoader.forceLoadChunk(cart.world, oldPos.x, oldPos.z-1, false);
                ForgeChunkLoader.forceLoadChunk(cart.world, oldPos.x+1, oldPos.z-1, false);

                // Load ahead chunks
                ForgeChunkLoader.forceLoadChunk(cart.world, newPos.x-1, newPos.z+1, true);
                ForgeChunkLoader.forceLoadChunk(cart.world, newPos.x, newPos.z+1, true);
                ForgeChunkLoader.forceLoadChunk(cart.world, newPos.x+1, newPos.z+1, true);
            } else {                    // Direction: v
                // Load ahead chunks
                ForgeChunkLoader.forceLoadChunk(cart.world, oldPos.x-1, oldPos.z+1, false);
                ForgeChunkLoader.forceLoadChunk(cart.world, oldPos.x, oldPos.z+1, false);
                ForgeChunkLoader.forceLoadChunk(cart.world, oldPos.x+1, oldPos.z+1, false);

                // Unload past chunks
                ForgeChunkLoader.forceLoadChunk(cart.world, newPos.x-1, newPos.z-1, true);
                ForgeChunkLoader.forceLoadChunk(cart.world, newPos.x, newPos.z-1, true);
                ForgeChunkLoader.forceLoadChunk(cart.world, newPos.x+1, newPos.z-1, true);
            }
        }

        /*ForgeChunkLoader.forceLoadChunk(cart.world, pos.x+1, pos.z, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x, pos.z+1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x+1, pos.z+1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x-1, pos.z, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x, pos.z-1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x-1, pos.z-1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x-1, pos.z+1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x+1, pos.z-1, true);*/

        //ForgeChunkLoader.debugListChunks(cart.world);

    }

    static class ForgeChunkLoader {
        static boolean forceLoadChunk(World world, int x, int z, boolean forceload) {
            UnstoppingMinecarts.LOGGER.info(((forceload) ? "force" : "unforce") + " loading chunk [" + x + ", " + z + "]");
            return world.func_212414_b(x,z,forceload);
        }

        static void debugListChunks(World world) {
            LongSet loadedChunks = world.func_212412_ag();
            UnstoppingMinecarts.LOGGER.info("debugListChunks:" + loadedChunks.toString());
            UnstoppingMinecarts.LOGGER.info("debugListChunks size " + loadedChunks.size());
        }
    }
}
