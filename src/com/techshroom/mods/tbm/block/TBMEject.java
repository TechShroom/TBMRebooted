package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.*;
import static com.techshroom.mods.tbm.Tutils.address;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMEjectTile;

public class TBMEject extends BlockContainer {
    public TBMEject() {
        super(Material.iron);
        this.setBlockName("ejecter")
                .setBlockTextureName(
                        address(mod().id(), "eject-tex").toString())
                .setCreativeTab((CreativeTabs) store_get("blockTab"));
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TBMEjectTile();
    }
}
