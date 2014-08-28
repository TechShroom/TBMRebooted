package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.techshroom.mods.tbm.ConvertsToEntity;
import com.techshroom.mods.tbm.entity.TBMDrillEntity;
import com.techshroom.mods.tbm.gui.GuiTBMDrill;
import com.techshroom.mods.tbm.gui.container.ContainerTBMDrill;

public class TBMDrillTile extends AlwaysSyncedSidedTile implements
        ConvertsToEntity<TBMDrillEntity>, IPlayerContainerProvider,
        IGuiProvider<ContainerTBMDrill> {
    private static final int SIZE = 1;

    public TBMDrillTile() {
        super(new InventoryBasic("Drill", false, SIZE), slotAccessAll(SIZE));
    }

    @Override
    public int getSizeInventory() {
        return SIZE;
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return super.isItemValidForSlot(p_94041_1_, p_94041_2_)
                && p_94041_1_ == 0
                && p_94041_2_.getItem() == (Item) store_get("drillhead");
    }

    @Override
    public TBMDrillEntity convertToEntity() {
        return new TBMDrillEntity(worldObj).withTile(this);
    }

    @Override
    public Container container(EntityPlayer player) {
        return new ContainerTBMDrill(this, player.inventory);
    }

    @Override
    public GuiScreen guiScreen(ContainerTBMDrill c) {
        return new GuiTBMDrill(c);
    }

    @Override
    public int getGUIId() {
        return (Integer) store_get("drill-gui-id");
    }
}
