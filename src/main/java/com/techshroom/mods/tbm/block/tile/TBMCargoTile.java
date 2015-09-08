package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.ConvertsToEntity;
import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.entity.TBMCargoEntity;
import com.techshroom.mods.tbm.gui.GuiTBMCargo;
import com.techshroom.mods.tbm.gui.container.ContainerTBMCargo;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;

public class TBMCargoTile extends AlwaysSyncedCPUSidedTile implements
        ConvertsToEntity<TBMCargoEntity>, IPlayerContainerProvider,
        IGuiProvider<ContainerTBMCargo> {
    private static final int SIZE = 27;

    public TBMCargoTile() {
        super(new InventoryBasic("Cargo", false, SIZE), slotsToAllowAnySidedAcess(SIZE));
    }

    @Override
    public int getSizeInventory() {
        return SIZE;
    }

    @Override
    public TBMCargoEntity convertToEntity() {
        return new TBMCargoEntity(this.worldObj).withTile(this);
    }

    @Override
    public Container container(EntityPlayer player) {
        return new ContainerTBMCargo(this, player.inventory);
    }

    @Override
    public GuiScreen guiScreen(ContainerTBMCargo c) {
        return new GuiTBMCargo(c);
    }

    @Override
    public int getGUIId() {
        return store.get(TBMKeys.GuiId.CARGO).getAsInt();
    }

}
