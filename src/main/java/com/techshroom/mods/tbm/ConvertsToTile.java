package com.techshroom.mods.tbm;

import net.minecraft.tileentity.TileEntity;

public interface ConvertsToTile<TileType extends TileEntity> {

    TileType convertToTile();
}
