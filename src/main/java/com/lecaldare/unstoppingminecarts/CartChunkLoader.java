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

    void cartMovedOnChunk(EntityUnstoppableMinecart cart, ChunkPos pos) {

        UnstoppingMinecarts.LOGGER.info("cartMovedOnChunk! loading near " + pos.toString());
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x+1, pos.z, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x, pos.z+1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world,pos.x+1, pos.z+1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world,pos.x-1, pos.z, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x, pos.z-1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world,pos.x-1, pos.z-1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world,pos.x-1, pos.z+1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world,pos.x+1, pos.z-1, true);

        ForgeChunkLoader.debugListChunks(cart.world);

    }

    static class ForgeChunkLoader {
        static boolean forceLoadChunk(World world, int x, int z, boolean forceload) {
            return world.func_212414_b(x,z,forceload);
        }

        static void debugListChunks(World world) {
            LongSet loadedChunks = world.func_212412_ag();
            UnstoppingMinecarts.LOGGER.info("debugListChunks:" + loadedChunks.toString());
            UnstoppingMinecarts.LOGGER.info("debugListChunks size " + loadedChunks.size());
        }
    }
}
