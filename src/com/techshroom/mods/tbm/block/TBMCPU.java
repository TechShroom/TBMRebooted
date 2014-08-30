package com.techshroom.mods.tbm.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMCPUTile;

public class TBMCPU extends TBMBlockContainer<TBMCPUTile> {
    public TBMCPU() {
        super("cpu", "cpu-tex");
    }

    @Override
    protected TBMCPUTile g_createNewTileEntity(World w, int meta) {
        return new TBMCPUTile();
    }

    @Override
    protected boolean onBlockActivated(World w, int x, int y, int z,
            EntityPlayer p, TBMCPUTile tile) {
        tile.fireGUIOpenRequest(p);
        return true;
    }
}
