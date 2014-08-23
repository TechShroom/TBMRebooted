package com.techshroom.mods.tbm.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerTBMDrill extends Container {
    public ContainerTBMDrill(IInventory inventory) {
        addSlotToContainer(new Slot(inventory, 0, 0, 0));
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
