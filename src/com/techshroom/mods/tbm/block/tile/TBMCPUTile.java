package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.TBMMod.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import com.techshroom.mods.tbm.ConvertsToEntity;
import com.techshroom.mods.tbm.entity.TBMCPUEntity;
import com.techshroom.mods.tbm.gui.GuiTBMCPU;

public class TBMCPUTile extends AlwaysSyncedTileEntity implements
        ConvertsToEntity<TBMCPUEntity>, IPlayerContainerProvider,
        IGuiProvider<Container> {
    @Override
    public TBMCPUEntity convertToEntity() {
        return new TBMCPUEntity(worldObj).withTile(this);
    }

    public void fireGUIOpenRequest(EntityPlayer player) {
        player.openGui(mod(), getGUIId(), worldObj, xCoord, yCoord, zCoord);
    }

    private int getGUIId() {
        return (Integer) store_get("cpu-gui-id");
    }

    @Override
    public GuiScreen guiScreen(Container c) {
        return new GuiTBMCPU();
    }

    @Override
    public Container container(EntityPlayer player) {
        return IPlayerContainerProvider.NULL.container(player);
    }
}
