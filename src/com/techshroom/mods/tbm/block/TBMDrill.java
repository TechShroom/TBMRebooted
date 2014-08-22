package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.Tutils.address;

import com.techshroom.mods.tbm.block.tile.TBMDrillTile;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TBMDrill extends BlockContainer {

    public TBMDrill() {
        super(Material.iron);
        this.setBlockName("tbm.drill").setBlockTextureName(
                address(mod().id(), "drill-tex").toString());
    }

    @Override
    public TileEntity createNewTileEntity(World w, int meta) {
        return new TBMDrillTile();
    }
}
