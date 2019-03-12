package com.lecaldare.unstoppingminecarts;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class EntityUnstoppableMinecart extends EntityMinecart {
    ChunkPos oldChunkPos;

    public EntityUnstoppableMinecart(World world) {
        super(UnstoppingMinecarts.UNSTOPPABLEMINECART_TYPE, world);
        if(!this.world.isRemote())
            UnstoppingMinecarts.LOGGER.info("Created new EntityUnstoppableMinecart! Constructor 1");
    }

    public EntityUnstoppableMinecart(World world, double x, double y, double z) {
        super(UnstoppingMinecarts.UNSTOPPABLEMINECART_TYPE, world, x, y, z);
        if(!this.world.isRemote()) {
            oldChunkPos = new ChunkPos(new BlockPos(x, y, z));
            UnstoppingMinecarts.LOGGER.info("Created new EntityUnstoppableMinecart! Constructor 2 on " + oldChunkPos.asBlockPos());
            CartChunkLoader.getInstance().cartCreated(this, oldChunkPos);
        }
    }

    private void positionChanged(double x, double y, double z) {
        // Check if cart moved to another chunk
        ChunkPos newChunkPos = new ChunkPos(new BlockPos(x,y,z));

        // First move: we can be here only if object has been costructed using the first constructor
        if(oldChunkPos == null) {
            CartChunkLoader.getInstance().cartCreated(this, newChunkPos);
            oldChunkPos = newChunkPos;
            return;
        }
        // Moving to a near chunk
        else if(!newChunkPos.equals(oldChunkPos)) {
            UnstoppingMinecarts.LOGGER.info("Chunk changed to x=" + newChunkPos.x + " z=" + newChunkPos.z);
            CartChunkLoader.getInstance().cartMovedOnChunk(this, oldChunkPos, newChunkPos);
            oldChunkPos = newChunkPos;
        }

        //CartChunkLoader.getInstance().cartMoved(this, oldChunkPos, newChunkPos);
        //UnstoppingMinecarts.LOGGER.info("Changed minecart position to " + newChunkPos.toString());
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
        if(!this.world.isRemote())
            positionChanged(x, y, z);
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
    }

    @Override
    public void moveMinecartOnRail(BlockPos pos) {
        super.moveMinecartOnRail(pos);
        //int chunkX = pos.getX() >> 4;
        //int chunkY = pos.getY() >> 4;
        //int chunkZ = pos.getZ() >> 4;
        //System.out.println("Moving ON RAIL to x=" + pos.getX() + " y=" + pos.getY() + " z=" + pos.getZ());
    }*/

    @Override
    public void remove() {
        if(!this.world.isRemote()) {
            UnstoppingMinecarts.LOGGER.info("Minecart deleted");
            CartChunkLoader.getInstance().cartDestroyed(this, oldChunkPos);
        }
        super.remove();
    }

    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }
}