package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.Tutils.isClient;

import java.util.Arrays;

import com.techshroom.mods.tbm.Tutils;
import com.techshroom.mods.tbm.inv.ExtendedInventoryUtils;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants.NBT;

public abstract class AlwaysSyncedSidedTile extends AlwaysSyncedTileEntity
        implements ISidedInventory {

    protected static final int SIDE_COUNT = EnumFacing.VALUES.length;
    private static int[][][] SLOT_CACHE = { new int[SIDE_COUNT][0] };

    private static boolean cacheContains(int i) {
        return SLOT_CACHE.length > i && SLOT_CACHE[i] != null;
    }

    private static int[][] getFromCache(int i) {
        return SLOT_CACHE[i];
    }

    private static int[][] putInCache(int i, int[][] slots) {
        ensureCap(i);
        SLOT_CACHE[i] = slots;
        return getFromCache(i);
    }

    private static final int CAP_INCREASE = 5;

    private static void ensureCap(int i) {
        if (SLOT_CACHE.length <= i) {
            SLOT_CACHE = Arrays.copyOf(SLOT_CACHE, i + CAP_INCREASE);
        }
    }

    protected static final int[][] slotsToAllowAnySidedAcess(int invSize) {
        if (cacheContains(invSize)) {
            return getFromCache(invSize);
        }
        int[][] slots = new int[SIDE_COUNT][invSize];
        int[] inner_slots = Tutils.indexEqualsIndexArray(invSize);
        Arrays.fill(slots, inner_slots);
        return putInCache(invSize, slots);
    }

    protected final IInventory backingInv;
    protected final int[][] slotAccess;

    protected AlwaysSyncedSidedTile(IInventory backing, int[][] slotAccess) {
        this.backingInv = backing;
        this.slotAccess = slotAccess;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return this.backingInv.getStackInSlot(p_70301_1_);
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return this.backingInv.decrStackSize(p_70298_1_, p_70298_2_);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return this.backingInv.getStackInSlotOnClosing(p_70304_1_);
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        this.backingInv.setInventorySlotContents(p_70299_1_, p_70299_2_);
    }

    @Override
    public String getCommandSenderName() {
        return this.backingInv.getCommandSenderName();
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.backingInv.getDisplayName();
    }

    @Override
    public boolean hasCustomName() {
        return this.backingInv.hasCustomName();
    }

    @Override
    public int getInventoryStackLimit() {
        return this.backingInv.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return this.backingInv.isUseableByPlayer(p_70300_1_);
    }

    public abstract int getGUIId();

    public void fireGUIOpenRequest(EntityPlayer player) {
        player.openGui(mod(), getGUIId(), this.worldObj, this.pos.getX(),
                this.pos.getY(), this.pos.getZ());
    }

    public void fireGUICloseRequest(EntityPlayer player) {
        player.closeScreen();
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (player.isSpectator()) {
            return;
        }
        this.backingInv.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (player.isSpectator()) {
            return;
        }
        this.backingInv.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return this.backingInv.isItemValidForSlot(p_94041_1_, p_94041_2_);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return this.slotAccess[side.getIndex()];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn,
            EnumFacing direction) {
        for (int s : getSlotsForFace(direction)) {
            if (s == index) {
                return isItemValidForSlot(index, itemStackIn);
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack,
            EnumFacing direction) {
        for (int s : getSlotsForFace(direction)) {
            if (s == index) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        ExtendedInventoryUtils
                .readInventory(nbt.getTagList("inv", NBT.TAG_COMPOUND), this);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setTag("inv", ExtendedInventoryUtils.writeInventory(this));
    }

    @Override
    public void localMarkDirty() {
        this.backingInv.markDirty();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (isClient(this.worldObj)) {
            return;
        }
        // block destroyed or similar
        for (int i1 = 0; i1 < getSizeInventory(); ++i1) {
            ItemStack itemstack = getStackInSlot(i1);

            if (itemstack != null) {
                Block.spawnAsEntity(this.worldObj, this.pos, itemstack);
            }
        }
    }

    @Override
    public void clear() {
        this.backingInv.clear();
    }

    @Override
    public void setField(int id, int value) {
        this.backingInv.setField(id, value);
    }

    @Override
    public int getField(int id) {
        return this.backingInv.getField(id);
    }

    @Override
    public int getFieldCount() {
        return this.backingInv.getFieldCount();
    }

}
