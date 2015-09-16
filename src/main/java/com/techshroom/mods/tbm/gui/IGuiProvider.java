package com.techshroom.mods.tbm.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;

public interface IGuiProvider<ContainerType extends Container> {

    static final IGuiProvider<Container> NULL = new IGuiProvider<Container>() {

        @Override
        public GuiScreen guiScreen(Container c) {
            return null;
        }
    };

    GuiScreen guiScreen(ContainerType c);
}
