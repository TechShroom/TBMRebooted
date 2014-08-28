package com.techshroom.mods.tbm.gui;

import static com.techshroom.mods.tbm.Tutils.genericize;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiTBMCPU extends GuiScreen {
    public GuiTBMCPU() {
    }

    private List<GuiButton> buttons() {
        return genericize(buttonList);
    }

    @Override
    public void initGui() {
        super.initGui();
        List<GuiButton> btns = buttons();
        btns.add(new GuiButton(0, 100, 100,
                "Wow, what a Cool Personal Unicorn!"));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        drawDefaultBackground();
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
