package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.block.tile.TBMCPUTile;
import com.techshroom.mods.tbm.gui.GuiTBMCPU;
import com.techshroom.mods.tbm.gui.container.ContainerTBMCPU;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public class TBMCPUEntity
        extends TBMEntity<ContainerTBMCPU, TBMCPUTile, TBMCPUEntity> {

    public TBMCPUEntity(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    @Override
    public Block blockBase() {
        return store.get(TBMKeys.Blocks.CPU).get();
    }

    @Override
    public Container container(EntityPlayer player) {
        if (this.tref == null) {
            return new ContainerTBMCPU(player);
        }
        return this.tref.container(player);
    }

    @Override
    public GuiScreen guiScreen(ContainerTBMCPU c) {
        if (this.tref == null) {
            return new GuiTBMCPU(this);
        }
        return this.tref.guiScreenFromEntity(c, this);
    }

    public void guiStop() {
    }
}
