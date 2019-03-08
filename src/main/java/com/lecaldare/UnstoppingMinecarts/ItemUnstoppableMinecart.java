package com.lecaldare.UnstoppingMinecarts;

import net.minecraft.item.ItemMinecart;
import net.minecraft.entity.item.EntityMinecart.Type;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ItemUnstoppableMinecart extends ItemMinecart {
    ItemUnstoppableMinecart(Type t, Properties p) {
        super(t, p);
        //new ItemMinecart(t, p);
    }

    @Override
    public ITextComponent getName() {
        return new TextComponentString("Unstoppable Minecart");
    }

    //onBlockDestroyed
    //onItemUse
}
