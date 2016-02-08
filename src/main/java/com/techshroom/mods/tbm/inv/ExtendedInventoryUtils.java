package com.techshroom.mods.tbm.inv;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public final class ExtendedInventoryUtils {

    private ExtendedInventoryUtils() {
    }

    public static final int INVENTORY_TAG_TYPE = NBT.TAG_COMPOUND;

    public static NBTTagList writeInventory(IInventory inv) {
        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);

            if (itemstack != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                itemstack.writeToNBT(itemTag);
                itemList.appendTag(itemTag);
            }
        }

        return itemList;
    }

    public static void readInventory(NBTTagList itemList, IInventory inv) {
        int sizeInventory = inv.getSizeInventory();
        for (int i = 0; i < sizeInventory; ++i) {
            inv.setInventorySlotContents(i, (ItemStack) null);
        }

        for (int k = 0; k < itemList.tagCount(); ++k) {
            NBTTagCompound itemTag = itemList.getCompoundTagAt(k);
            int slot = itemTag.getInteger("Slot");

            if (slot >= 0 && slot < sizeInventory) {
                inv.setInventorySlotContents(slot,
                        ItemStack.loadItemStackFromNBT(itemTag));
            }
        }
    }
}
