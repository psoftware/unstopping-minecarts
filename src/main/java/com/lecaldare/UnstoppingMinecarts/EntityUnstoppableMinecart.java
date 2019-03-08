package com.lecaldare.UnstoppingMinecarts;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.world.World;

public class EntityUnstoppableMinecart extends EntityMinecartEmpty {
    EntityUnstoppableMinecart(World w) {
        super(w);
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
        System.out.println("Moving!");
        super.move(type, x, y, z);
    }
}