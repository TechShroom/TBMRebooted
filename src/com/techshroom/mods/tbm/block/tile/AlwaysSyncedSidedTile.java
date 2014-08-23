package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.TBMMod.mod;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

import com.techshroom.mods.tbm.inv.ExtendedInventoryUtils;

public abstract class AlwaysSyncedSidedTile extends AlwaysSyncedTileEntity
        implements ISidedInventory {
    protected final IInventory backingInv;
    protected final int[][] SLOT_ACCESS;

    protected AlwaysSyncedSidedTile(IInventory backing, int[][] slotAccess) {
        backingInv = backing;
        SLOT_ACCESS = slotAccess;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return backingInv.getStackInSlot(p_70301_1_);
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return backingInv.decrStackSize(p_70298_1_, p_70298_2_);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return backingInv.getStackInSlotOnClosing(p_70304_1_);
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        backingInv.setInventorySlotContents(p_70299_1_, p_70299_2_);
    }

    @Override
    public String getInventoryName() {
        return backingInv.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return backingInv.hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return backingInv.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return backingInv.isUseableByPlayer(p_70300_1_);
    }

    protected abstract int getGUIId();

    public void fireGUIOpenRequest(EntityPlayer player) {
        player.openGui(mod(), getGUIId(), worldObj, xCoord, yCoord, zCoord);
    }

    public void fireGUICloseRequest(EntityPlayer player) {
        player.closeScreen();
    }

    @Override
    public void openInventory() {
        backingInv.openInventory();
    }

    @Override
    public void closeInventory() {
        backingInv.closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return backingInv.isItemValidForSlot(p_94041_1_, p_94041_2_);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return SLOT_ACCESS[p_94128_1_];
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_,
            int p_102007_3_) {
        return Arrays.asList(SLOT_ACCESS[p_102007_3_]).contains(p_102007_1_)
                && isItemValidForSlot(p_102007_1_, p_102007_2_);
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_,
            int p_102008_3_) {
        return Arrays.asList(SLOT_ACCESS[p_102008_3_]).contains(p_102008_1_);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        ExtendedInventoryUtils.readInventory(
                nbt.getTagList("inv", NBT.TAG_LIST), this);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        ExtendedInventoryUtils.writeInventory(this);
    }

    @Override
    public void localMarkDirty() {
        backingInv.markDirty();
    }
}
