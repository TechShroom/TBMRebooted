package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.*;
import static com.techshroom.mods.tbm.Tutils.address;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.techshroom.mods.tbm.block.tile.TBMEngineTile;

public class TBMEngine extends BlockContainer {
    public TBMEngine() {
        super(Material.iron);
        this.setBlockName("engine")
                .setBlockTextureName(
                        address(mod().id(), "engine-tex").toString())
                .setCreativeTab((CreativeTabs) store_get("blockTab"));
    }

    @Override
    public TileEntity createNewTileEntity(World w, int meta) {
        return new TBMEngineTile();
    }

    @Override
    public boolean onBlockActivated(World w, int x, int y, int z,
            EntityPlayer p, int p_149727_6_, float p_149727_7_,
            float p_149727_8_, float p_149727_9_) {
        TileEntity te = w.getTileEntity(x, y, z);
        ((TBMEngineTile) te).fireGUIOpenRequest(p);
        return true;
    }

    @Override
    public IIcon getIcon(int sd, int mt) {
        ForgeDirection dir = ForgeDirection.getOrientation(sd);
        if (dir == ForgeDirection.DOWN || dir == ForgeDirection.UP) {
            return ((TBMDrill) store_get("drill")).notTheDrillTexture();
        }
        return blockIcon;
    }
}
