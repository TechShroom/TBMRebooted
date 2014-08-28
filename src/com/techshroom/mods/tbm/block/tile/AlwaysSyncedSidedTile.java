package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.Tutils.isClient;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.Constants.NBT;

import com.techshroom.mods.tbm.inv.ExtendedInventoryUtils;

public abstract class AlwaysSyncedSidedTile extends AlwaysSyncedTileEntity
        implements ISidedInventory {
    protected static final int SIDE_COUNT =
            ForgeDirection.VALID_DIRECTIONS.length;
    private static final Random rand = new Random();
    private static boolean[] storeContains = { true };
    private static int[][][] slotStore = { new int[SIDE_COUNT][0] };

    private static boolean __has(int i) {
        return storeContains[i];
    }

    private static int[][] __get(int i) {
        return slotStore[i];
    }

    private static int[][] __set(int i, int[][] slots) {
        ensureCap(i);
        storeContains[i] = true;
        slotStore[i] = slots;
        return __get(i);
    }

    private static final int INC = 5;
    
    private static void ensureCap(int i) {
        if (storeContains.length <= i) {
            storeContains = Arrays.copyOf(storeContains, i + INC);
        }
        if (slotStore.length <= i) {
            slotStore = Arrays.copyOf(slotStore, i + INC);
        }
    }

    protected static final int[][] slotAccessAll(int invSize) {
        if (__has(invSize)) {
            return __get(invSize);
        }
        int[][] slots = new int[SIDE_COUNT][invSize];
        for (int[] is : slots) {
            for (int i = 0; i < is.length; i++) {
                is[i] = i;
            }
        }
        return __set(invSize, slots);
    }

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
                nbt.getTagList("inv", NBT.TAG_COMPOUND), this);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setTag("inv", ExtendedInventoryUtils.writeInventory(this));
    }

    @Override
    public void localMarkDirty() {
        backingInv.markDirty();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (isClient(worldObj)) {
            return;
        }
        // block destroyed or similar
        for (int i1 = 0; i1 < getSizeInventory(); ++i1) {
            ItemStack itemstack = getStackInSlot(i1);

            if (itemstack != null) {
                float f = rand.nextFloat() * 0.8F + 0.1F;
                float f1 = rand.nextFloat() * 0.8F + 0.1F;
                EntityItem entityitem;

                for (float f2 = rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; getWorldObj()
                        .spawnEntityInWorld(entityitem)) {
                    int j1 = rand.nextInt(21) + 10;

                    if (j1 > itemstack.stackSize) {
                        j1 = itemstack.stackSize;
                    }

                    itemstack.stackSize -= j1;
                    entityitem =
                            new EntityItem(getWorldObj(),
                                    (double) ((float) xCoord + f),
                                    (double) ((float) yCoord + f1),
                                    (double) ((float) zCoord + f2),
                                    new ItemStack(itemstack.getItem(), j1,
                                            itemstack.getItemDamage()));
                    float f3 = 0.05F;
                    entityitem.motionX =
                            (double) ((float) rand.nextGaussian() * f3);
                    entityitem.motionY =
                            (double) ((float) rand.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ =
                            (double) ((float) rand.nextGaussian() * f3);

                    if (itemstack.hasTagCompound()) {
                        entityitem.getEntityItem().setTagCompound(
                                (NBTTagCompound) itemstack.getTagCompound()
                                        .copy());
                    }
                }
            }
        }
    }
}
