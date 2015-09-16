package com.techshroom.mods.tbm.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public interface IPlayerContainerProvider {

    static final IPlayerContainerProvider NULL =
            new IPlayerContainerProvider() {

                @Override
                public Container container(EntityPlayer player) {
                    return null;
                }
            };

    Container container(EntityPlayer player);
}
