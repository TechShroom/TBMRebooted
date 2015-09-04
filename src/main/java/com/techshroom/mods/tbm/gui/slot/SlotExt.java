package com.techshroom.mods.tbm.gui.slot;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SlotExt extends Slot {

    private static final class ActuallyExtendedSlot extends SlotExt {

        private final Slot worker;

        public ActuallyExtendedSlot(Slot slot) {
            super(slot.inventory, slot.getSlotIndex(), slot.xDisplayPosition,
                    slot.yDisplayPosition);
            this.worker = slot;
        }

        @Override
        public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
            this.worker.onSlotChange(p_75220_1_, p_75220_2_);
        }

        @Override
        public int hashCode() {
            return this.worker.hashCode();
        }

        @Override
        public void onPickupFromSlot(EntityPlayer p_82870_1_,
                ItemStack p_82870_2_) {
            this.worker.onPickupFromSlot(p_82870_1_, p_82870_2_);
        }

        @Override
        public boolean isItemValid(ItemStack p_75214_1_) {
            return this.worker.isItemValid(p_75214_1_)
                    && super.isItemValid(p_75214_1_);
        }

        @Override
        public ItemStack getStack() {
            return this.worker.getStack();
        }

        @Override
        public boolean getHasStack() {
            return this.worker.getHasStack();
        }

        @Override
        public void putStack(ItemStack p_75215_1_) {
            this.worker.putStack(p_75215_1_);
        }

        @Override
        public void onSlotChanged() {
            this.worker.onSlotChanged();
        }

        @Override
        public boolean equals(Object obj) {
            return this.worker.equals(obj);
        }

        @Override
        public int getSlotStackLimit() {
            return this.worker.getSlotStackLimit();
        }

        @Override
        public ItemStack decrStackSize(int p_75209_1_) {
            return this.worker.decrStackSize(p_75209_1_);
        }

        @Override
        public boolean isHere(IInventory inv, int slotIn) {
            return this.worker.isHere(inv, slotIn);
        }

        @Override
        public boolean canTakeStack(EntityPlayer p_82869_1_) {
            return this.worker.canTakeStack(p_82869_1_);
        }

        @Override
        public int getItemStackLimit(ItemStack stack) {
            return this.worker.getItemStackLimit(stack);
        }

        @Override
        public String getSlotTexture() {
            return this.worker.getSlotTexture();
        }

        @Override
        public boolean canBeHovered() {
            return this.worker.canBeHovered();
        }

        @Override
        public ResourceLocation getBackgroundLocation() {
            return this.worker.getBackgroundLocation();
        }

        @Override
        public void setBackgroundLocation(ResourceLocation texture) {
            this.worker.setBackgroundLocation(texture);
        }

        @Override
        public void setBackgroundName(String name) {
            this.worker.setBackgroundName(name);
        }

        @Override
        public TextureAtlasSprite getBackgroundSprite() {
            return this.worker.getBackgroundSprite();
        }

        @Override
        public int getSlotIndex() {
            return this.worker.getSlotIndex();
        }

        @Override
        public String toString() {
            return this.worker.toString();
        }

    }

    public SlotExt(IInventory inv, int slotIDInInv, int x, int y) {
        super(inv, slotIDInInv, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack p_75214_1_) {
        return super.isItemValid(p_75214_1_) && this.inventory
                .isItemValidForSlot(getSlotIndex(), p_75214_1_);
    }

    public static SlotExt extend(Slot slot) {
        return new ActuallyExtendedSlot(slot);
    }
}
