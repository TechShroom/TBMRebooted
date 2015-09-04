package com.techshroom.mods.tbm.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerTBMEngine extends ContainerExt {

    public ContainerTBMEngine(IInventory inventory, InventoryPlayer playerInv) {
        super(inventory, playerInv.player);
        this.withBalancedSlots().withPlayerInventory(playerInv);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
