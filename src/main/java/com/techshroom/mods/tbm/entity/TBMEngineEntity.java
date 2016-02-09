package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.gui.GuiTBMEngine;
import com.techshroom.mods.tbm.gui.container.ContainerTBMEngine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMEngineEntity extends TBMFullGuiEntity<ContainerTBMEngine> {

    public TBMEngineEntity(World w) {
        this(w, store.get(TBMKeys.Blocks.ENGINE).get().getDefaultState());
    }

    public TBMEngineEntity(World w, IBlockState state) {
        super(w, state, TBMKeys.GuiId.ENGINE,
                new InventoryBasic("Engine", false, 9));
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return super.isItemValidForSlot(slot, stack)
                && slot < this.getSizeInventory() && isFuel(stack);
    }

    private boolean isFuel(ItemStack item) {
        return TileEntityFurnace.isItemFuel(item);
    }

    @Override
    public GuiScreen guiScreen(ContainerTBMEngine c) {
        return new GuiTBMEngine(c);
    }

    @Override
    public Container container(EntityPlayer player) {
        return new ContainerTBMEngine(this, player.inventory);
    }

    @Override
    public void onMoveToBlock(BlockPos pos) {
    }
}
