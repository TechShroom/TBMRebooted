package com.techshroom.mods.tbm.gui;

import org.lwjgl.opengl.GL11;

import com.techshroom.mods.tbm.gui.container.ContainerTBMCargo;

import net.minecraft.util.ResourceLocation;

public class GuiTBMCargo extends GuiContainerExt {

    private static final ResourceLocation field_147017_u =
            new ResourceLocation("textures/gui/container/generic_54.png");

    public GuiTBMCargo(ContainerTBMCargo cont) {
        super(cont);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
            int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147017_u);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize,
                this.container.getNumRows() * 18 + 17);
        this.drawTexturedModalRect(k, l + this.container.getNumRows() * 18 + 17,
                0, 126, this.xSize, 96);
    }
}
