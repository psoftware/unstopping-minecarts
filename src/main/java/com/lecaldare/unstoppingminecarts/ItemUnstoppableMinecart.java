package com.lecaldare.unstoppingminecarts;

import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMinecart;
import net.minecraft.entity.item.EntityMinecart.Type;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemUnstoppableMinecart extends ItemMinecart {
    ItemUnstoppableMinecart(Type t, Properties p) {
        super(t, p);
        //new ItemMinecart(t, p);
    }

    @Override
    public EnumActionResult onItemUse(ItemUseContext itemUseContext) {
        //return super.onItemUse(itemUseContext);

        World world = itemUseContext.getWorld();
        EntityPlayer player = itemUseContext.getPlayer();
        BlockPos pos = itemUseContext.getPos();
        //EnumHand hand = player.getActiveHand();
        //ItemStack stack = player.getHeldItem(hand);
        ItemStack stack = player.getActiveItemStack();

        if (!world.isRemote) {
            if (BlockRailBase.isRail(world, pos)) {
                try {
                    final EntityUnstoppableMinecart cart =
                            new EntityUnstoppableMinecart(world,
                                    pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
                    world.spawnEntity(cart);
                } catch (Exception e) {
                    e.printStackTrace();
                    return EnumActionResult.FAIL;
                }
                stack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public ITextComponent getName() {
        return new TextComponentString("Unstoppable Minecart");
    }

    //onBlockDestroyed
    //onItemUse
}
