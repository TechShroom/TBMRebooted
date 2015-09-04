package com.techshroom.mods.tbm.block;

import com.techshroom.mods.tbm.block.tile.TBMCPUTile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TBMCPU extends TBMBlockContainer<TBMCPUTile> {

    public TBMCPU() {
        super("cpu", "cpu-tex");
    }

    @Override
    protected TBMCPUTile g_createNewTileEntity(World w, int meta) {
        return new TBMCPUTile();
    }

    @Override
    protected boolean onBlockActivated(World w, BlockPos pos, EntityPlayer p,
            EnumFacing side, TBMCPUTile tile) {
        tile.fireGUIOpenRequest(p);
        return true;
    }
}
