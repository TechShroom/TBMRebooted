package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.*;
import static com.techshroom.mods.tbm.Tutils.*;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMDrillTile;

public class TBMDrill extends BlockContainer {
    private IIcon nonFront;

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
    public void onBlockPlacedBy(World w, int x, int y, int z,
            EntityLivingBase ent, ItemStack stack) {
        super.onBlockPlacedBy(w, x, y, z, ent, stack);
        w.setBlockMetadataWithNotify(x, y, z,
                getMetadataForBlock(x, y, z, ent), MetadataConstants.SEND);
    }

    @Override
    public boolean onBlockActivated(World w, int x, int y, int z,
            EntityPlayer p, int p_149727_6_, float p_149727_7_,
            float p_149727_8_, float p_149727_9_) {
        TileEntity te = w.getTileEntity(x, y, z);
        ((TBMDrillTile) te).fireGUIOpenRequest(p);
        return true;
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);
        nonFront =
                reg.registerIcon(address(mod().id(), "drill_close-tex")
                        .toString());
    }

    @Override
    public IIcon getIcon(int sd, int mt) {
        return sd == mt ? blockIcon : nonFront;
    }
}
