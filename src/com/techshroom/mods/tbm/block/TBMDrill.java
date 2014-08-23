package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.*;
import static com.techshroom.mods.tbm.Tutils.address;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMDrillTile;

public class TBMDrill extends BlockContainer {

    public TBMDrill() {
        super(Material.iron);
        this.setBlockName("drill")
                .setBlockTextureName(
                        address(mod().id(), "drill-tex").toString())
                .setCreativeTab((CreativeTabs) store_get("blockTab"));
    }

    @Override
    public TileEntity createNewTileEntity(World w, int meta) {
        return new TBMDrillTile();
    }

    @Override
    public boolean onBlockActivated(World w, int x, int y, int z,
            EntityPlayer p, int p_149727_6_, float p_149727_7_,
            float p_149727_8_, float p_149727_9_) {
        TileEntity te = w.getTileEntity(x, y, z);
        ((TBMDrillTile) te).fireGUIOpenRequest(p);
        return true;
    }
}
