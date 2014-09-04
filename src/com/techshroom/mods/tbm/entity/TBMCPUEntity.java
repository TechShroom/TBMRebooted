package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import static com.techshroom.mods.tbm.Tutils.isClient;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMCPUTile;

public class TBMCPUEntity
        extends TBMEntity<Container, TBMCPUTile, TBMCPUEntity> {
    private TBMCPUTile guiSource;

    public TBMCPUEntity(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    @Override
    public void pWithTile(TBMCPUTile tile) {
        System.err.println(tile);
        guiSource = tile;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        NBTTagCompound tileBase = nbt.getCompoundTag("tile");
        pWithTile(new TBMCPUTile());
        guiSource.readFromNBT(tileBase);
        System.err.println(isClient(worldObj) + "=>" + "READ-A");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        NBTTagCompound tileBase = nbt.getCompoundTag("tile");
        guiSource.writeToNBT(tileBase);
        nbt.setTag("tile", tileBase);
        System.err.println(isClient(worldObj) + "=>" + "WRITE-A");
    }

    @Override
    public TBMCPUTile convertToTile() {
        return guiSource;
    }

    @Override
    public boolean providesGUI() {
        return true;
    }

    @Override
    public int getGUIId() {
        System.err.println(isClient(worldObj) + "=>" + guiSource);
        return guiSource.getGUIId();
    }

    @Override
    public Block blockBase() {
        return store_get("cpu");
    }

    @Override
    public Container container(EntityPlayer player) {
        return guiSource.container(player);
    }

    @Override
    public GuiScreen guiScreen(Container c) {
        return guiSource.guiScreenFromEntity(c, this);
    }

    public void guiStop() {
    }
}
