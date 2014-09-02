package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMCargoTile;
import com.techshroom.mods.tbm.gui.container.ContainerTBMCargo;

public class TBMCargoEntity extends TBMEntity<ContainerTBMCargo, TBMCargoTile> {
    private TBMCargoTile guiSource;

    public TBMCargoEntity(World w) {
        super(w);
    }

    public TBMCargoEntity withTile(TBMCargoTile tile) {
        guiSource = tile;
        return this;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
    }

    @Override
    public boolean providesGUI() {
        return true;
    }
    
    @Override
    public Block blockBase() {
        return store_get("cargo");
    }

    @Override
    public int getGUIId() {
        return guiSource.getGUIId();
    }

    @Override
    public GuiScreen guiScreen(ContainerTBMCargo c) {
        return guiSource.guiScreen(c);
    }

    @Override
    public Container container(EntityPlayer player) {
        return guiSource.container(player);
    }

    @Override
    public TBMCargoTile convertToTile() {
        return guiSource;
    }
}
