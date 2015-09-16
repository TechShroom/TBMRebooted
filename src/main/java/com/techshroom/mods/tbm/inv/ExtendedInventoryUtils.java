package com.techshroom.mods.tbm.inv;

import static com.techshroom.mods.tbm.TBMMod.mod;

import codechicken.lib.inventory.InventoryUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

public final class ExtendedInventoryUtils {

    private ExtendedInventoryUtils() {
    }

    public static NBTTagList writeInventory(IInventory inv) {
        ItemStack[] stack = new ItemStack[inv.getSizeInventory()];
        for (int i = 0; i < stack.length; i++) {
            stack[i] = inv.getStackInSlot(i);
        }
        NBTTagList list = InventoryUtils.writeItemStacksToTag(stack,
                inv.getInventoryStackLimit());
        return list;
    }

    public static void readInventory(NBTTagList list, IInventory inv) {
        ItemStack[] stack = new ItemStack[inv.getSizeInventory()];
        InventoryUtils.readItemStacksFromTag(stack, list);
        for (int i = 0; i < stack.length; i++) {
            ItemStack is = stack[i];
            if (is != null && !inv.isItemValidForSlot(i, is)) {
                mod().log.warn("Item " + is + " is not valid for slot " + i
                        + "; couldn't load it from NBT and store it");
            } else if (is != null) {
                inv.setInventorySlotContents(i, is);
            }
        }
    }
}
