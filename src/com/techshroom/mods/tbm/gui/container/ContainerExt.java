package com.techshroom.mods.tbm.gui.container;

import com.techshroom.mods.tbm.gui.slot.SlotExt;

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

    @Override
    protected Slot addSlotToContainer(Slot slot) {
        if (!(slot instanceof SlotExt)) {
            // adapt slot
            slot = SlotExt.extend(slot);
        }
        return super.addSlotToContainer(slot);
    }

    /**
     * Merges provided ItemStack with the first available one in the
     * container/player inventory. Obeys
     * {@link IInventory#isItemValidForSlot(int, ItemStack)}.
     */
    @Override
    protected boolean mergeItemStack(ItemStack putStack,
            int startIndexInContainer, int endIndexInContainer,
            boolean workBackwards) {
        boolean flag1 = false;
        int k = startIndexInContainer;

        if (workBackwards) {
            k = endIndexInContainer - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (putStack.isStackable()) {
            while (putStack.stackSize > 0
                    && (!workBackwards && k < endIndexInContainer || workBackwards
                            && k >= startIndexInContainer)) {
                slot = (Slot) this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 != null
                        && itemstack1.getItem() == putStack.getItem()
                        && (!putStack.getHasSubtypes() || putStack
                                .getItemDamage() == itemstack1.getItemDamage())
                        && ItemStack
                                .areItemStackTagsEqual(putStack, itemstack1)) {
                    int l = itemstack1.stackSize + putStack.stackSize;

                    if (l <= putStack.getMaxStackSize()) {
                        putStack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    } else if (itemstack1.stackSize < putStack
                            .getMaxStackSize()) {
                        putStack.stackSize -=
                                putStack.getMaxStackSize()
                                        - itemstack1.stackSize;
                        itemstack1.stackSize = putStack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (workBackwards) {
                    --k;
                } else {
                    ++k;
                }
            }
        }

        if (putStack.stackSize > 0) {
            if (workBackwards) {
                k = endIndexInContainer - 1;
            } else {
                k = startIndexInContainer;
            }

            while (!workBackwards && k < endIndexInContainer || workBackwards
                    && k >= startIndexInContainer) {
                slot = (Slot) this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 == null && slot.isItemValid(putStack)) {
                    slot.putStack(putStack.copy());
                    slot.onSlotChanged();
                    putStack.stackSize = 0;
                    flag1 = true;
                    break;
                }

                if (workBackwards) {
                    --k;
                } else {
                    ++k;
                }
            }
        }

        return flag1;
    }
}
