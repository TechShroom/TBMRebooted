package com.techshroom.mods.tbm.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;

public class ContainerTBMCPU extends ContainerExt {

    public ContainerTBMCPU(EntityPlayer player) {
        super(new InventoryBasic("tbmcpudummy", false, 0), player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

}
