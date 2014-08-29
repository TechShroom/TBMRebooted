package com.techshroom.mods.tbm.gui;

import static com.techshroom.mods.tbm.Tutils.cast;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

import com.techshroom.mods.tbm.Tutils.Client;
import com.techshroom.mods.tbm.block.tile.TBMCPUTile;
import com.techshroom.mods.tbm.entity.TBMCPUEntity;

public class GuiTBMCPU extends GuiScreen {
    private static final class BUTTONS {
        private static final int MOTION = 0;

        private BUTTONS() {
        }
    }

    private TBMCPUTile tileRef = null;
    private TBMCPUEntity entRef = null;

    public GuiTBMCPU(TBMCPUEntity entity) {
        entRef = entity;
    }

    public GuiTBMCPU(TBMCPUTile cpu) {
        tileRef = cpu;
    }

    private List<GuiButton> buttons() {
        return cast(buttonList);
    }

    @Override
    public void initGui() {
        super.initGui();
        List<GuiButton> btns = buttons();
        btns.add(new GuiButton(BUTTONS.MOTION, width / 2 - 100,
                height / 2 - 10, getMotionMessage()));
    }

    private String getMotionMessage() {
        String trans = tileRef != null ? "cpugui.start" : "cpugui.stop";
        return StatCollector.translateToLocal(trans);
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

    @Override
    protected void actionPerformed(GuiButton button) {
        if (Client.buttonIsPressed(BUTTONS.MOTION, button)) {
            if (tileRef != null) {
                tileRef.guiStart();
            } else {
                entRef.guiStop();
            }
        }
    }
}
