package com.lecaldare.unstoppingminecarts;

import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy {
    @Override
    public void commonSetup() {
        RenderingRegistry.registerEntityRenderingHandler(EntityUnstoppableMinecart.class, RenderUnstoppableMinecart::new);
    }
}
