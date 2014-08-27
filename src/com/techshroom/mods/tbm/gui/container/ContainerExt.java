package com.techshroom.mods.tbm.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerExt extends Container {
    protected static final int SLOTS_PER_ROW_DEFAULT = 9;
    protected boolean usingPlayerInventory;
    protected int numRows;
    protected int slotsPerRow = SLOTS_PER_ROW_DEFAULT;
    protected IInventory inventory;

    public ContainerExt(IInventory inv) {
        this(inv, SLOTS_PER_ROW_DEFAULT);
    }

    public ContainerExt(IInventory inv, int rowSize) {
        this.numRows = inv.getSizeInventory() / (slotsPerRow = rowSize);
        if (numRows * slotsPerRow < inv.getSizeInventory()) {
            // compensate for floor
            numRows++;
        }
        inv.openInventory();
        inventory = inv;
    }

    public int getSlotsPerRow() {
        return slotsPerRow;
    }

    public int getNumRows() {
        return numRows;
    }

    /**
     * Balanced slots: balanced by rowSize, centered if possible
     */
    protected ContainerExt withBalancedSlots() {
        int slotCount = inventory.getSizeInventory();
        int slotIndex = 0;

        for (int row = 0; row < this.numRows && slotIndex < slotCount; ++row) {
            for (int col = 0; col < 9 && slotIndex < slotCount; ++col) {
                this.addSlotToContainer(new Slot(inventory, slotIndex,
                        8 + col * 18, 18 + row * 18));
                slotIndex++;
            }
        }
        if (slotIndex != slotCount) {
            throw new IllegalStateException(
                    "unbalanced slots, algorithm incorrect "
                            + String.format("(%s != %s)", slotIndex, slotCount));
        }
        return this;
    }

    protected ContainerExt withPlayerInventory(InventoryPlayer playerInv) {
        usingPlayerInventory = true;
        int playerInvOffset = (this.numRows - 4) * 18;
        int row;
        int col;

        // upper 3 rows
        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(playerInv, col + row * 9 + 9,
                        8 + col * 18, 103 + row * 18 + playerInvOffset));
            }
        }

        // hotbar
        for (row = 0; row < 9; ++row) {
            this.addSlotToContainer(new Slot(playerInv, row, 8 + row * 18,
                    161 + playerInvOffset));
        }
        return this;
    }

    @Override
    public ItemStack
            transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        return shiftClick(p_82846_1_, p_82846_2_);
    }

    protected ItemStack shiftClick(EntityPlayer player, int slotIndex) {
        ItemStack stackCopy = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stackCopy = slotStack.copy();

            int notPlayerSize = inventory.getSizeInventory();
            int allSlotsSize = inventorySlots.size();

            if (slotIndex < notPlayerSize) {
                if (!this.mergeItemStack(slotStack, notPlayerSize,
                        allSlotsSize, true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(slotStack, 0, notPlayerSize, false)) {
                return null;
            }

            if (slotStack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
        }

        return stackCopy;
    }
    
    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        inventory.closeInventory();
    }
}
