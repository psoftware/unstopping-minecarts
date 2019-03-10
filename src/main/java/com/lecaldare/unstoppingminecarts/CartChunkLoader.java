package com.lecaldare.unstoppingminecarts;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class CartChunkLoader {
    static CartChunkLoader instance = new CartChunkLoader();

    private HashMap<ChunkPos,Integer> cartLoadedChunks;

    private CartChunkLoader() {
        //ForgeChunkManager.requestTicket
        //new ForcedChunksSaveData("ads").func_212438_a();
        //Minecraft.getInstance().world.

        cartLoadedChunks = new HashMap<>();
    }

    static CartChunkLoader getInstance() {
        return instance;
    }

    private void loadChunk(World world, EntityUnstoppableMinecart cart, ChunkPos pos) {
        Integer chunkLoadRequests = cartLoadedChunks.get(pos);

        // if chunk is not loaded by other carts
        // also, chunkLoadRequests == 0 should not happen
        if(chunkLoadRequests == null || chunkLoadRequests == 0) {
            ForgeChunkLoader.forceLoadChunk(cart.world, pos.x, pos.z, true);
            cartLoadedChunks.put(pos, 1);
        } else
            cartLoadedChunks.put(pos, chunkLoadRequests+1);
    }

    private void unloadChunk(World world, EntityUnstoppableMinecart cart, ChunkPos pos) {
        Integer chunkLoadRequests = cartLoadedChunks.get(pos);

        // if hashmap does not contain requested chunk (absurd)
        // also, chunkLoadRequests == 0 should not happen
        if(chunkLoadRequests == null || chunkLoadRequests == 0) {
            UnstoppingMinecarts.LOGGER.error("unloadChunk: chunk is not loaded");
            return;
        }

        // if there is no other cart which requested this chunk to load
        if(chunkLoadRequests == 1) {
            // unload chunk
            ForgeChunkLoader.forceLoadChunk(cart.world, pos.x, pos.z, false);
            // remove to reduce hashmap size
            cartLoadedChunks.remove(pos);
        }
        // otherwise just decrement
        else
            cartLoadedChunks.put(pos, chunkLoadRequests-1);
    }

    /*void cartCreated(EntityUnstoppableMinecart cart, ChunkPos pos) {
        // Load all chunks 3x3
        cart.getUniqueID()
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x+1, pos.z, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x, pos.z+1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x+1, pos.z+1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x-1, pos.z, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x, pos.z-1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x-1, pos.z-1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x-1, pos.z+1, true);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x+1, pos.z-1, true);
    }

    void cartDestroyed(EntityUnstoppableMinecart cart, ChunkPos pos) {
        // Destroy all chunks 3x3
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x+1, pos.z, false);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x, pos.z+1, false);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x+1, pos.z+1, false);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x-1, pos.z, false);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x, pos.z-1, false);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x-1, pos.z-1, false);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x-1, pos.z+1, false);
        ForgeChunkLoader.forceLoadChunk(cart.world, pos.x+1, pos.z-1, false);
    }*/

    void cartMovedOnChunk(EntityUnstoppableMinecart cart, ChunkPos oldPos, ChunkPos newPos) {
        UnstoppingMinecarts.LOGGER.info("cartMovedOnChunk from " + oldPos.toString() + " to " + newPos.toString());
        if(oldPos.x != newPos.x && oldPos.z != newPos.z)
            UnstoppingMinecarts.LOGGER.info("Minecart moved diagonally or teleported? This should not happen");
        else if(oldPos.x != newPos.x) {
            if(oldPos.x < newPos.x) {   // Direction: >
                // Unload past chunks
                unloadChunk(cart.world, cart, new ChunkPos(oldPos.x-1, oldPos.z-1));
                unloadChunk(cart.world, cart, new ChunkPos(oldPos.x-1, oldPos.z));
                unloadChunk(cart.world, cart, new ChunkPos(oldPos.x-1, oldPos.z+1));

                // Load ahead chunks
                loadChunk(cart.world, cart, new ChunkPos(newPos.x+1, newPos.z-1));
                loadChunk(cart.world, cart, new ChunkPos(newPos.x+1, newPos.z));
                loadChunk(cart.world, cart, new ChunkPos(newPos.x+1, newPos.z+1));
            } else {                     // Direction: <
                // Unload past chunks
                unloadChunk(cart.world, cart, new ChunkPos(oldPos.x+1, oldPos.z-1));
                unloadChunk(cart.world, cart, new ChunkPos(oldPos.x+1, oldPos.z));
                unloadChunk(cart.world, cart, new ChunkPos(oldPos.x+1, oldPos.z+1));

                // Load ahead chunks
                loadChunk(cart.world, cart, new ChunkPos(newPos.x-1, newPos.z-1));
                loadChunk(cart.world, cart, new ChunkPos(newPos.x-1, newPos.z));
                loadChunk(cart.world, cart, new ChunkPos(newPos.x-1, newPos.z+1));
            }

        } else if(oldPos.z != newPos.z) {
            if(oldPos.z < newPos.z) {   // Direction: ^
                // Unload past chunks
                unloadChunk(cart.world, cart, new ChunkPos(oldPos.x-1, oldPos.z-1));
                unloadChunk(cart.world, cart, new ChunkPos(oldPos.x, oldPos.z-1));
                unloadChunk(cart.world, cart, new ChunkPos(oldPos.x+1, oldPos.z-1));

                // Load ahead chunks
                loadChunk(cart.world, cart, new ChunkPos(newPos.x-1, newPos.z+1));
                loadChunk(cart.world, cart, new ChunkPos(newPos.x, newPos.z+1));
                loadChunk(cart.world, cart, new ChunkPos(newPos.x+1, newPos.z+1));
            } else {                    // Direction: v
                // Load ahead chunks
                unloadChunk(cart.world, cart, new ChunkPos(oldPos.x-1, oldPos.z+1));
                unloadChunk(cart.world, cart, new ChunkPos(oldPos.x, oldPos.z+1));
                unloadChunk(cart.world, cart, new ChunkPos(oldPos.x+1, oldPos.z+1));

                // Unload past chunks
                loadChunk(cart.world, cart, new ChunkPos(newPos.x-1, newPos.z-1));
                loadChunk(cart.world, cart, new ChunkPos(newPos.x, newPos.z-1));
                loadChunk(cart.world, cart, new ChunkPos(newPos.x+1, newPos.z-1));
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
