package com.techshroom.mods.tbm.block.tile;

import net.minecraft.inventory.IInventory;

public abstract class AlwaysSyncedCPUSidedTile extends AlwaysSyncedSidedTile
        implements CPUConnectable {
    protected TBMCPUTile cpuTile;

    protected AlwaysSyncedCPUSidedTile(IInventory backing, int[][] slotAccess) {
        super(backing, slotAccess);
    }
    
    @Override
    public TBMCPUTile getCPUTile() {
        return cpuTile;
    }
    
    @Override
    public void setCPUTile(TBMCPUTile tile) {
        this.cpuTile = tile;
    }
}
