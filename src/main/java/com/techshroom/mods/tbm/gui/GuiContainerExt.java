package com.techshroom.mods.tbm.gui;

import com.techshroom.mods.tbm.gui.container.ContainerExt;

import net.minecraft.client.gui.inventory.GuiContainer;

public abstract class GuiContainerExt extends GuiContainer {

    protected final ContainerExt container;

    public GuiContainerExt(ContainerExt contExt) {
        super(contExt);
        this.container = contExt;
        int specialChestOffset = 114;
        this.ySize = specialChestOffset + contExt.getNumRows() * 18;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
            int p_146976_2_, int p_146976_3_) {
    }
}
