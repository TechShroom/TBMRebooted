package com.techshroom.mods.tbm.block.tile;

import net.minecraft.tileentity.TileEntity;

public interface CPUConnectable {
    TBMCPUTile getCPUTile();

    void setCPUTile(TBMCPUTile tile);
    
    boolean hasCPUTile();

    boolean updateCPUConnections(TileEntity[] dejaVu, TBMCPUTile backRef);

    boolean sendUpdateRequestToCPU(TileEntity[] dejaVu);
}
