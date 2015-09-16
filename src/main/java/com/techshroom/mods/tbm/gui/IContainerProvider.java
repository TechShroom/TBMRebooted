package com.techshroom.mods.tbm.gui;

import net.minecraft.inventory.Container;

public interface IContainerProvider {

    static final IContainerProvider NULL = new IContainerProvider() {

        @Override
        public Container container() {
            return null;
        }
    };

    Container container();
}
