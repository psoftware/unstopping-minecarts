package com.lecaldare.unstoppingminecarts;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMinecart;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(UnstoppingMinecarts.MODID)
public class UnstoppingMinecarts {
    public static final String MODID = "unstoppingminecarts";
    public static final String VERSION = "0.1";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    private ForgeEventHandler eventHandler;

    public UnstoppingMinecarts() {
        LOGGER.info("Executing constructor...");
        eventHandler = new ForgeEventHandler();
        MinecraftForge.EVENT_BUS.register(ForgeEventHandler.class);
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ForgeEventHandler {
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            LOGGER.info("Registering Items...");
            event.getRegistry().registerAll(new ItemUnstoppableMinecart(EntityMinecart.Type.RIDEABLE, new Item.Properties())
                .setRegistryName("unstoppableminecart"));
        }

        @SubscribeEvent
        public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
            LOGGER.info("Registering Entities...");
            EntityType<?> unstoppableMinecart =
                    EntityType.Builder.create(EntityUnstoppableMinecart.class, EntityUnstoppableMinecart::new)
                            .build(MODID + ":" + "unstoppableminecart")
                            .setRegistryName(new ResourceLocation(MODID, "unstoppableminecart"));

            event.getRegistry().register(unstoppableMinecart);
            proxy.onRegisterEntities();
        }

        @SubscribeEvent
        public static void playerInteraction(PlayerInteractEvent event) {
            // Do only at server side
            if(!event.getWorld().isRemote)
                return;

            LOGGER.info("New PlayerInteractEvent! "
                    + event.getHand().toString()
                    + ", " + event.getItemStack().getDisplayName());

            // Check if it's a Minecart
            if(event.getItemStack().getItem() instanceof ItemMinecart)
                LOGGER.info("It's a minecart!");
        }

        @SubscribeEvent //PreInit
        public void initClient(FMLClientSetupEvent event)
        {
            LOGGER.info("Executing initClient...");
        }
    }
}