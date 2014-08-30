package com.techshroom.mods.tbm.block.tile;

public abstract class AlwaysSyncedCPUTile extends AlwaysSyncedTileEntity
        implements CPUConnectable {
    protected TBMCPUTile cpuTile;

    @Override
    public TBMCPUTile getCPUTile() {
        return cpuTile;
    }

    @Override
    public void setCPUTile(TBMCPUTile tile) {
        this.cpuTile = tile;
    }
}
