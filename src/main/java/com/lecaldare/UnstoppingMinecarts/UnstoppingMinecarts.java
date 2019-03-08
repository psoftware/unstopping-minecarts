package com.lecaldare.UnstoppingMinecarts;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMinecart;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;

@Mod(UnstoppingMinecarts.MODID)
public class UnstoppingMinecarts {
    public static final String MODID = "unstoppingminecarts";
    public static final String VERSION = "0.1";

    public UnstoppingMinecarts() {
        System.out.println("Executing constructor...");
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    public static class EventHandler {
        @SubscribeEvent
        public void registerItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(new ItemUnstoppableMinecart(EntityMinecart.Type.RIDEABLE, null));
        }

        @SubscribeEvent
        public void registerEntities(RegistryEvent.Register<EntityType> event) {
            event.getRegistry().registerAll(new EntityUnstoppableMinecart(Minecraft.getInstance().world));
        }

        @SubscribeEvent
        public void playerInteraction(PlayerInteractEvent event) {
            // Do only at server side
            if(!event.getWorld().isRemote)
                return;

            System.out.println("New PlayerInteractEvent! "
                    + event.getHand().toString()
                    + ", " + event.getItemStack().getDisplayName());

            // Check if it's a Minecart
            if(event.getItemStack().getItem() instanceof ItemMinecart)
                System.out.println("It's a minecart!");
        }

        @SubscribeEvent //PreInit
        public void initClient(FMLClientSetupEvent event)
        {
            System.out.println("Executing initClient...");
        }
    }

    static public void main(String[] args) {
        System.out.println("Executing main...");
    }
}