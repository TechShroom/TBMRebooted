package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.gui.GuiTBMCargo;
import com.techshroom.mods.tbm.gui.container.ContainerTBMCargo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMCargoEntity extends TBMFullGuiEntity<ContainerTBMCargo> {

    public TBMCargoEntity(World w) {
        this(w, store.get(TBMKeys.Blocks.CARGO).get().getDefaultState());
    }

    public TBMCargoEntity(World w, IBlockState state) {
        super(w, state, TBMKeys.GuiId.CARGO,
                new InventoryBasic("Cargo", false, 27));
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
    public void onMoveToBlock(BlockPos pos) {
    }

}
