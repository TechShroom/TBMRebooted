package com.techshroom.mods.tbm.entity;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.techshroom.mods.tbm.TBMMod.store;

import java.util.function.Supplier;

import com.techshroom.mods.tbm.inv.ExtendedInventoryUtils;
import com.techshroom.mods.tbm.util.Storage.IntKey;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

/**
 * All of the common GUI tools. ALL OF THEM.
 */
public abstract class TBMFullGuiEntity<C extends Container>
        extends TBMGuiEntity<C> implements IInventory {

    // If/when external slot access is needed
    // https://gist.github.com/kenzierocks/45b1a25568c35051abe1

    private final IntKey guiIdKey;
    private final transient Supplier<RuntimeException> noGuiIdException;
    protected final IInventory backingInv;

    protected TBMFullGuiEntity(World w, IBlockState state, IntKey guiIdKey,
            IInventory backingInv) {
        super(w, state);
        this.guiIdKey = checkNotNull(guiIdKey);
        this.backingInv = checkNotNull(backingInv);
        this.noGuiIdException = () -> new IllegalStateException("GUI ID key "
                + guiIdKey + " was not present in the global storage");
    }

    @Override
    public int getGuiId() {
        return store.get(this.guiIdKey).orElseThrow(this.noGuiIdException);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setTag("inventory",
                ExtendedInventoryUtils.writeInventory(this.backingInv));
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        ExtendedInventoryUtils.readInventory(
                nbt.getTagList("inventory",
                        ExtendedInventoryUtils.INVENTORY_TAG_TYPE),
                this.backingInv);
    }

    @Override
    public String getCustomNameTag() {
        return this.backingInv.getCommandSenderName();
    }

    @Override
    public int getSizeInventory() {
        return this.backingInv.getSizeInventory();
    }

    @Override
    public boolean hasCustomName() {
        return this.backingInv.hasCustomName();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.backingInv.getStackInSlot(index);
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.backingInv.getDisplayName();
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return this.backingInv.decrStackSize(index, count);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return this.backingInv.getStackInSlotOnClosing(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.backingInv.setInventorySlotContents(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return this.backingInv.getInventoryStackLimit();
    }

    @Override
    public void markDirty() {
        this.backingInv.markDirty();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSqToEntity(this) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        this.backingInv.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        this.backingInv.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return this.backingInv.isItemValidForSlot(index, stack);
    }

    @Override
    public int getField(int id) {
        return this.backingInv.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        this.backingInv.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return this.backingInv.getFieldCount();
    }

    @Override
    public void clear() {
        this.backingInv.clear();
    }

}
