package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.Tutils.isClient;
import net.minecraft.tileentity.TileEntity;

import com.google.common.base.Throwables;
import com.techshroom.mods.tbm.Tutils.SharedMethods;

public abstract class AlwaysSyncedCPUTile extends AlwaysSyncedTileEntity
        implements CPUConnectable {
    protected TBMCPUTile cpuTile;

    @Override
    public TBMCPUTile getCPUTile() {
        if (isClient(worldObj)) {
            // not allowed
            Throwables.propagate(new IllegalAccessException(
                    "getCPUTile() only avalible on server"));
        }
        return cpuTile;
    }

    @Override
    public void setCPUTile(TBMCPUTile tile) {
        if (isClient(worldObj)) {
            // will happen on server side
            return;
        }
        System.err.println(this + ".cpuTile = " + tile);
        this.cpuTile = tile;
    }

    @Override
    public boolean hasCPUTile() {
        return cpuTile != null;
    }

    @Override
    public boolean
            updateCPUConnections(TileEntity[] dejaVu, TBMCPUTile backRef) {
        return SharedMethods.CPUConnectable_updateCPUConnections(this, this,
                dejaVu, backRef);
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        SharedMethods.CPUConnectable_updateContainingBlockInfo(this, this);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        SharedMethods.CPUConnectable_updateEntity(this, this);
    }

    @Override
    public boolean sendUpdateRequestToCPU(TileEntity[] dejaVu) {
        return SharedMethods.CPUConnectable_sendUpdateRequestToCPU(this, this,
                dejaVu);
    }
}
