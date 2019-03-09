package com.lecaldare.unstoppingminecarts;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class EntityUnstoppableMinecart extends EntityMinecartEmpty {
    ChunkPos oldChunkPos;

    EntityUnstoppableMinecart(World world) {
        super(world);
    }

    EntityUnstoppableMinecart(World world, double x, double y, double z) {
        super(world, x, y, z);
        oldChunkPos = new ChunkPos(new BlockPos(x,y,z));
        UnstoppingMinecarts.LOGGER.info("Created new EntityUnstoppableMinecart!");
    }

    /*public static EntityMinecart func_184263_a(World world, double q, double r, double s, EntityMinecart.Type minecartType) {
        switch(minecartType) {
            case CHEST:
                return new EntityMinecartChest(world, q, r, s);
            case FURNACE:
                return new EntityMinecartFurnace(world, q, r, s);
            case TNT:
                return new EntityMinecartTNT(world, q, r, s);
            case SPAWNER:
                return new EntityMinecartMobSpawner(world, q, r, s);
            case HOPPER:
                return new EntityMinecartHopper(world, q, r, s);
            case COMMAND_BLOCK:
                return new EntityMinecartCommandBlock(world, q, r, s);
            default:
                return new EntityMinecartEmpty(world, q, r, s);
        }
    }*/

    @Override
    public void moveMinecartOnRail(BlockPos pos) {
        super.moveMinecartOnRail(pos);

        // Check if cart moved to another chunk
        ChunkPos newChunkPos = new ChunkPos(pos);
        if(!newChunkPos.equals(oldChunkPos)) {
            UnstoppingMinecarts.LOGGER.info("Chunk changed to x=" + newChunkPos.x + " z=" + newChunkPos.z);
            CartChunkLoader.getInstance().cartMovedOnChunk(this, newChunkPos);
            oldChunkPos = newChunkPos;
        }

        /*int chunkX = pos.getX() >> 4;
        int chunkY = pos.getY() >> 4;
        int chunkZ = pos.getZ() >> 4;*/
        //System.out.println("Moving ON RAIL to x=" + pos.getX() + " y=" + pos.getY() + " z=" + pos.getZ());
    }

    /*
    @Override
    protected void moveAlongTrack(BlockPos pos, IBlockState state) {
        super.moveAlongTrack(pos, state);
        System.out.println("Moving ALONG TRACK to x=" + pos.getX() + " y=" + pos.getY() + " z=" + pos.getZ());
    }*/

    /*@Override
    public void move(MoverType type, double x, double y, double z) {
        System.out.println("Moving to x=" + x + " y=" + y + " z=" + z);
        super.move(type, x, y, z);
    }*/
}