package com.lecaldare.unstoppingminecarts;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.MinecartEntityTypes;

public class RenderUnstoppableMinecart<T extends EntityUnstoppableMinecart> extends RenderMinecart<T> {
    private static final ResourceLocation UNSTOPPABLEMINECART_TEXTURES =
            new ResourceLocation(UnstoppingMinecarts.MODID,"textures/entity/unstoppableminecart.png");

    public RenderUnstoppableMinecart(RenderManager renderManagerIn) {
        super(renderManagerIn);
        //UnstoppingMinecarts.LOGGER.info("Rendering: " + UNSTOPPABLEMINECART_TEXTURES.getPath());
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return UNSTOPPABLEMINECART_TEXTURES;
    }
}
