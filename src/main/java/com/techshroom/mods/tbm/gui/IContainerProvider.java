package com.techshroom.mods.tbm.gui;

import net.minecraft.inventory.Container;

@FunctionalInterface
public interface IContainerProvider {

    static final IContainerProvider NULL = () -> null;

    Container container();
}
