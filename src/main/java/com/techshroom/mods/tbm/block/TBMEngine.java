package com.techshroom.mods.tbm.block;

import com.techshroom.mods.tbm.block.tile.TBMEngineTile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TBMEngine extends TBMBlockContainer<TBMEngineTile> {

    public TBMEngine() {
        super("engine", "engine-tex");
    }

    @Override
    protected TBMEngineTile g_createNewTileEntity(World w, int meta) {
        return new TBMEngineTile();
    }

    @Override
    protected boolean onBlockActivated(World w, BlockPos pos, EntityPlayer p,
            EnumFacing side, TBMEngineTile tile) {
        tile.fireGUIOpenRequest(p);
        return true;
    }

}
