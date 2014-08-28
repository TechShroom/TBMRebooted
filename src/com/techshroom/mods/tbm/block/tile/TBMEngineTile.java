package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;

import com.techshroom.mods.tbm.ConvertsToEntity;
import com.techshroom.mods.tbm.entity.TBMEngineEntity;
import com.techshroom.mods.tbm.gui.GuiTBMEngine;
import com.techshroom.mods.tbm.gui.container.ContainerTBMEngine;

public class TBMEngineTile extends AlwaysSyncedSidedTile implements
        ConvertsToEntity<TBMEngineEntity>, IPlayerContainerProvider,
        IGuiProvider<ContainerTBMEngine> {
    private static final int SIZE = 9;

    public TBMEngineTile() {
        super(new InventoryBasic("Engine", false, SIZE), slotAccessAll(SIZE));
    }

    @Override
    public int getSizeInventory() {
        return SIZE;
    }

    @Override
    public TBMEngineEntity convertToEntity() {
        return new TBMEngineEntity(worldObj).withTile(this);
    }

    @Override
    public Container container(EntityPlayer player) {
        return new ContainerTBMEngine(this, player.inventory);
    }

    @Override
    public GuiScreen guiScreen(ContainerTBMEngine c) {
        return new GuiTBMEngine(c);
    }

    @Override
    public int getGUIId() {
        return ((Integer) store_get("engine-gui-id")).intValue();
    }
}
