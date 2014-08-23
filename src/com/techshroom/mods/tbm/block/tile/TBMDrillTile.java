package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.techshroom.mods.tbm.ConvertsToEntity;
import com.techshroom.mods.tbm.entity.TBMDrillEntity;
import com.techshroom.mods.tbm.gui.GuiTBMDrill;
import com.techshroom.mods.tbm.gui.container.ContainerTBMDrill;

public class TBMDrillTile extends AlwaysSyncedSidedTile implements
        ConvertsToEntity<TBMDrillEntity>, IContainerProvider,
        IGuiProvider<ContainerTBMDrill> {

    public TBMDrillTile() {
        super(new InventoryBasic("Drill", false, 1), new int[][] { { 0 },
                { 0 }, { 0 }, { 0 }, { 0 }, { 0 } });
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return super.isItemValidForSlot(p_94041_1_, p_94041_2_)
                && p_94041_1_ == 0
                && p_94041_2_.getItem() == (Item) store_get("drillhead");
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public TBMDrillEntity convertToEntity() {
        return new TBMDrillEntity(worldObj).withTile(this);
    }

    @Override
    public Container container() {
        return new ContainerTBMDrill(this);
    }

    @Override
    public GuiScreen guiScreen(ContainerTBMDrill c) {
        return new GuiTBMDrill(c);
    }

    @Override
    protected int getGUIId() {
        return (Integer) store_get("drill-gui-id");
    }
}
