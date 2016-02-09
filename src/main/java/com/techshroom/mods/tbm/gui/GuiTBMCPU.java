package com.techshroom.mods.tbm.gui;

import static com.techshroom.mods.tbm.Tutils.cast;

import java.util.List;

import com.techshroom.mods.tbm.Tutils.Client;
import com.techshroom.mods.tbm.entity.TBMCPUEntity;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

public class GuiTBMCPU extends GuiScreen {

    private static final class Buttons {

        private static final int MOTION = 0;

        private Buttons() {
        }
    }

    private TBMCPUEntity entRef = null;

    public GuiTBMCPU(TBMCPUEntity entity) {
        this.entRef = entity;
    }

    private List<GuiButton> buttons() {
        return cast(this.buttonList);
    }

    @Override
    public void initGui() {
        super.initGui();
        List<GuiButton> btns = buttons();
        btns.add(new GuiButton(Buttons.MOTION, this.width / 2 - 100,
                this.height / 2 - 10, getMotionMessage()));
    }

    private String getMotionMessage() {
        String trans = isAtAStandstill() ? "cpugui.start" : "cpugui.stop";
        return StatCollector.translateToLocal(trans);
    }

    private boolean isAtAStandstill() {
        // TODO better definition of moving
        return this.entRef.motionX < 0.001 && this.entRef.motionZ < 0.001
                && this.entRef.motionY < 0.001;
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
        if (Client.buttonIsPressed(Buttons.MOTION, button)) {
            if (isAtAStandstill()) {
                this.entRef.guiStart();
            } else {
                this.entRef.guiStop();
            }
        }
    }
}
