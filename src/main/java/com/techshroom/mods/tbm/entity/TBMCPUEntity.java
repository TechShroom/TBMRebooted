package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.gui.GuiTBMCPU;
import com.techshroom.mods.tbm.gui.container.ContainerTBMCPU;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMCPUEntity extends TBMGuiEntity<ContainerTBMCPU> {

    public TBMCPUEntity(World w) {
        super(w, store.get(TBMKeys.Blocks.CPU).get().getDefaultState());
    }

    @Override
    public Container container(EntityPlayer player) {
        return new ContainerTBMCPU(player);
    }

    @Override
    public GuiScreen guiScreen(ContainerTBMCPU c) {
        return new GuiTBMCPU(this);
    }

    @Override
    public int getGuiId() {
        return store.get(TBMKeys.GuiId.CPU).getAsInt();
    }

    public void guiStop() {
    }

    public void guiStart() {
    }

    @Override
    public void onMoveToBlock(BlockPos pos) {
    }

}
