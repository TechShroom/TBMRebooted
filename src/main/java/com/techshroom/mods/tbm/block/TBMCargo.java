package com.techshroom.mods.tbm.block;

import com.techshroom.mods.tbm.block.tile.TBMCargoTile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TBMCargo extends TBMBlockContainer<TBMCargoTile> {
    public TBMCargo() {
        super("cargo", "cargo_side-tex");
    }

    @Override
    protected TBMCargoTile g_createNewTileEntity(World w, int meta) {
        return new TBMCargoTile();
    }

    @Override
    protected boolean onBlockActivated(World w, BlockPos pos,
            EntityPlayer p, EnumFacing side, TBMCargoTile tile) {
        tile.fireGUIOpenRequest(p);
        return true;
    }

}
