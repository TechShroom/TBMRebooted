package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.Tutils.isClient;

import com.google.common.base.Throwables;
import com.techshroom.mods.tbm.Tutils.DefaultMethods;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public abstract class AlwaysSyncedCPUSidedTile extends AlwaysSyncedSidedTile
        implements CPUConnectable {

    protected TBMCPUTile cpuTile;

    protected AlwaysSyncedCPUSidedTile(IInventory backing, int[][] slotAccess) {
        super(backing, slotAccess);
    }

    @Override
    public TBMCPUTile getCPUTile() {
        if (isClient(this.worldObj)) {
            // not allowed
            Throwables.propagate(new IllegalAccessException(
                    "getCPUTile() only avalible on server"));
        }
        return this.cpuTile;
    }

    @Override
    public void setCPUTile(TBMCPUTile tile) {
        if (isClient(this.worldObj)) {
            // will happen on server side
            System.err.println("isclient block");
            return;
        }
        System.err.println(this + ".cpuTile = " + tile);
        this.cpuTile = tile;
        tile.insertTileEntity(this);
    }

    @Override
    public boolean hasCPUTile() {
        return this.cpuTile != null;
    }

    @Override
    public void update() {
        DefaultMethods.CPUConnectable_updateEntity(this, this);
    }

    @Override
    public boolean updateCPUConnections(TileEntity[] dejaVu,
            TBMCPUTile backRef) {
        return DefaultMethods.CPUConnectable_updateCPUConnections(this, this,
                dejaVu, backRef);
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        DefaultMethods.CPUConnectable_updateContainingBlockInfo(this, this);
    }

    @Override
    public boolean sendUpdateRequestToCPU(TileEntity[] dejaVu) {
        return DefaultMethods.CPUConnectable_sendUpdateRequestToCPU(this, this,
                dejaVu);
    }
}
