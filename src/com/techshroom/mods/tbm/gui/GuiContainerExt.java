package com.techshroom.mods.tbm.gui;

import net.minecraft.client.gui.inventory.GuiContainer;

import com.techshroom.mods.tbm.gui.container.ContainerExt;

public abstract class GuiContainerExt extends GuiContainer {
    protected final ContainerExt container;
    
    public GuiContainerExt(ContainerExt contExt) {
        super(contExt);
        container = contExt;
        int specialChestOffset = 114;
        this.ySize = specialChestOffset + contExt.getNumRows() * 18;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
            int p_146976_2_, int p_146976_3_) {
    }
}
