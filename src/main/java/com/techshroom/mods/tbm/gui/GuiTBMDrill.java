package com.techshroom.mods.tbm.gui;

import org.lwjgl.opengl.GL11;

import com.techshroom.mods.tbm.gui.container.ContainerTBMDrill;

import net.minecraft.util.ResourceLocation;

public class GuiTBMDrill extends GuiContainerExt {

    private static final ResourceLocation CHEST_GUI_TEXTURE =
            new ResourceLocation("minecraft",
                    "textures/gui/container/generic_54.png");

    public GuiTBMDrill(ContainerTBMDrill cont) {
        super(cont);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
            int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize,
                this.container.getNumRows() * 18 + 17);
        this.drawTexturedModalRect(k, l + this.container.getNumRows() * 18 + 17,
                0, 126, this.xSize, 96);
    }

}
