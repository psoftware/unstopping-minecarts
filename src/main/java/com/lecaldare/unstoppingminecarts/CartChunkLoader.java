package com.lecaldare.unstoppingminecarts;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class CartChunkLoader {
    static CartChunkLoader instance = new CartChunkLoader();

    // TODO: take dimension also into account
    private HashMap<ChunkPos, HashSet<EntityUnstoppableMinecart>> cartLoadedChunks;

    private CartChunkLoader() {
        //ForgeChunkManager.requestTicket
        //new ForcedChunksSaveData("ads").func_212438_a();
        //Minecraft.getInstance().world.

        // TODO: load forced chunks from NBT
        cartLoadedChunks = new HashMap<>();
    }

    static CartChunkLoader getInstance() {
        return instance;
    }

    private void loadChunk(World world, EntityUnstoppableMinecart cart, ChunkPos pos) {
        HashSet<EntityUnstoppableMinecart> chunkLoadRequests = cartLoadedChunks.get(pos);

        // if chunk is not loaded by other carts
        // also, chunkLoadRequests.isEmpty() should not happen
        if(chunkLoadRequests == null || chunkLoadRequests.isEmpty()) {
            ForgeChunkLoader.forceLoadChunk(world, pos.x, pos.z, true);
            chunkLoadRequests = new HashSet<>();
            chunkLoadRequests.add(cart);
            cartLoadedChunks.put(pos, chunkLoadRequests);
        }
        // otherwise just add the cart to the list of the cart loading requests
        else {
            if(!chunkLoadRequests.add(cart))
                UnstoppingMinecarts.LOGGER.info("loadChunk: cart already requested to load chunk " + pos.toString());
        }
    }

    private void unloadChunk(World world, EntityUnstoppableMinecart cart, ChunkPos pos) {
        HashSet<EntityUnstoppableMinecart> chunkLoadRequests = cartLoadedChunks.get(pos);

        // if hashmap does not contain requested chunk (absurd)
        // also, chunkLoadRequests.isEmpty() should not happen
        if(chunkLoadRequests == null || chunkLoadRequests.isEmpty()) {
            UnstoppingMinecarts.LOGGER.error("unloadChunk: chunk " + pos.toString() +" is not loaded");
            return;
        }

        // check if this cart has previously loaded the requested chunk
        if(!chunkLoadRequests.contains(cart)) {
            UnstoppingMinecarts.LOGGER.error("unloadChunk: cart " + cart.getUniqueID() +
                    " has not previously requested to load chunk " + pos.toString());
            return;
        }

        // if there is no other cart which requested this chunk to load
        if(chunkLoadRequests.size() == 1) {
            // unload chunk
            ForgeChunkLoader.forceLoadChunk(world, pos.x, pos.z, false);
            // remove to reduce hashmap size
            cartLoadedChunks.remove(pos);
        }
        // otherwise just remove the cart from the list
        else
            chunkLoadRequests.remove(cart);
    }

    //TODO: Deletion could be based on hashsets for handling errors
    void cartCreated(EntityUnstoppableMinecart cart, ChunkPos pos) {
        // Load all chunks 3x3
        for(int i=-1; i<=1; i++)
            for(int j=-1; j<=1; j++)
                loadChunk(cart.world, cart, new ChunkPos(pos.x+i, pos.z+j));
    }

    void cartDestroyed(EntityUnstoppableMinecart cart, ChunkPos pos) {
        // Destroy all chunks 3x3
        for(int i=-1; i<=1; i++)
            for(int j=-1; j<=1; j++)
                unloadChunk(cart.world, cart, new ChunkPos(pos.x+i, pos.z+j));
    }

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
