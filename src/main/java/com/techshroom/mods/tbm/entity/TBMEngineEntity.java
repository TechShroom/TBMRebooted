package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.gui.GuiTBMEngine;
import com.techshroom.mods.tbm.gui.container.ContainerTBMEngine;

import net.minecraft.block.Block;
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
        super(w, TBMKeys.GuiId.ENGINE, new InventoryBasic("Engine", false, 9));
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return super.isItemValidForSlot(p_94041_1_, p_94041_2_)
                && p_94041_1_ == 0 && isFuel(p_94041_2_);
    }

    private boolean isFuel(ItemStack item) {
        return TileEntityFurnace.isItemFuel(item);
    }

    @Override
    public Block blockBase() {
        return store.get(TBMKeys.Blocks.ENGINE).get();
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
