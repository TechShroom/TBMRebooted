package com.techshroom.mods.tbm.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBase extends Item {
    public ItemBase(String name) {
        setUnlocalizedName(name);
        GameRegistry.registerItem(this, name);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (hasSubtypes) {
            return getUnlocalizedName() + "[" + stack.getItemDamage() + "]";
        }
        return getUnlocalizedName();
    }
}
