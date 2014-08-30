package com.techshroom.mods.tbm.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class SlotExt extends Slot {
    private static final class ActuallyExtendedSlot extends SlotExt {
        private final Slot worker;

        public ActuallyExtendedSlot(Slot slot) {
            super(slot.inventory, slot.getSlotIndex(), slot.xDisplayPosition,
                    slot.yDisplayPosition);
            worker = slot;
        }

        @Override
        public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
            worker.onSlotChange(p_75220_1_, p_75220_2_);
        }

        @Override
        public int hashCode() {
            return worker.hashCode();
        }

        @Override
        public void onPickupFromSlot(EntityPlayer p_82870_1_,
                ItemStack p_82870_2_) {
            worker.onPickupFromSlot(p_82870_1_, p_82870_2_);
        }

        @Override
        public boolean isItemValid(ItemStack p_75214_1_) {
            return worker.isItemValid(p_75214_1_)
                    && super.isItemValid(p_75214_1_);
        }

        @Override
        public ItemStack getStack() {
            return worker.getStack();
        }

        @Override
        public boolean getHasStack() {
            return worker.getHasStack();
        }

        @Override
        public void putStack(ItemStack p_75215_1_) {
            worker.putStack(p_75215_1_);
        }

        @Override
        public void onSlotChanged() {
            worker.onSlotChanged();
        }

        @Override
        public boolean equals(Object obj) {
            return worker.equals(obj);
        }

        @Override
        public int getSlotStackLimit() {
            return worker.getSlotStackLimit();
        }

        @Override
        public ItemStack decrStackSize(int p_75209_1_) {
            return worker.decrStackSize(p_75209_1_);
        }

        @Override
        public boolean isSlotInInventory(IInventory p_75217_1_, int p_75217_2_) {
            return worker.isSlotInInventory(p_75217_1_, p_75217_2_);
        }

        @Override
        public boolean canTakeStack(EntityPlayer p_82869_1_) {
            return worker.canTakeStack(p_82869_1_);
        }

        @Override
        public IIcon getBackgroundIconIndex() {
            return worker.getBackgroundIconIndex();
        }

        @Override
        public boolean func_111238_b() {
            return worker.func_111238_b();
        }

        @Override
        public ResourceLocation getBackgroundIconTexture() {
            return worker.getBackgroundIconTexture();
        }

        @Override
        public void setBackgroundIcon(IIcon icon) {
            worker.setBackgroundIcon(icon);
        }

        @Override
        public void setBackgroundIconTexture(ResourceLocation texture) {
            worker.setBackgroundIconTexture(texture);
        }

        @Override
        public int getSlotIndex() {
            return worker.getSlotIndex();
        }

        @Override
        public String toString() {
            return worker.toString();
        }

    }

    public SlotExt(IInventory inv, int slotIDInInv, int x, int y) {
        super(inv, slotIDInInv, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack p_75214_1_) {
        return super.isItemValid(p_75214_1_)
                && inventory.isItemValidForSlot(getSlotIndex(), p_75214_1_);
    }

    public static SlotExt extend(Slot slot) {
        return new ActuallyExtendedSlot(slot);
    }
}
