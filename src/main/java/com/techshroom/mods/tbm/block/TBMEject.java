package com.techshroom.mods.tbm.block;

import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMEjectTile;

public class TBMEject extends TBMBlockContainer<TBMEjectTile> {
    public TBMEject() {
        super("ejecter", "eject-tex");
    }

    @Override
    protected TBMEjectTile g_createNewTileEntity(World w, int meta) {
        return new TBMEjectTile();
    }
}
