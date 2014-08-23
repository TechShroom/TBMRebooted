package com.techshroom.mods.tbm.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;

import com.techshroom.mods.tbm.gui.container.ContainerTBMDrill;

public class GuiTBMDrill extends GuiContainer {
    private static int btnID = 0;
    private static int EXIT = btnID++;

    public GuiTBMDrill(ContainerTBMDrill cont) {
        super(cont);
        xSize = 16;
        ySize = 16;
    }

    @Override
    public void initGui() {
        super.initGui();
        List<GuiButton> btns = buttonList();
        GuiButton last = null;
        btns.add(last =
                new GuiButton(EXIT, width / 2, (int) (height / 1.5d), "Exit"));
        last.xPosition -= last.width / 2;
        last.yPosition -= last.height / 2;
    }

    @SuppressWarnings("unchecked")
    private List<GuiButton> buttonList() {
        return (List<GuiButton>) buttonList;
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
        if (btn.id == EXIT) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
            int p_146976_2_, int p_146976_3_) {
    }
}
