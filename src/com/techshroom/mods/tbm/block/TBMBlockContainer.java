package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.*;
import static com.techshroom.mods.tbm.Tutils.*;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TBMBlockContainer<TileType extends TileEntity> extends
        BlockContainer {

    protected TBMBlockContainer(Material mat, String unlocalizedName,
            String blockTexBase) {
        super(mat);
        CreativeTabs blockTab = store_get("blockTab");
        setBlockName(unlocalizedName).setBlockTextureName(
                address(mod().id(), blockTexBase).toString()).setCreativeTab(
                blockTab);
    }

    protected TBMBlockContainer(String unlocalizedName, String blockTexBase) {
        this(Material.iron, unlocalizedName, blockTexBase);
    }

    @Override
    public boolean onBlockActivated(World w, int x, int y, int z,
            EntityPlayer p, int p_149727_6_, float p_149727_7_,
            float p_149727_8_, float p_149727_9_) {
        TileType tile = cast(w.getTileEntity(x, y, z));
        return onBlockActivated(w, x, y, z, p, tile);
    }

    protected boolean onBlockActivated(World w, int x, int y, int z,
            EntityPlayer p, TileType tile) {
        return false;
    }

    @Override
    public final TileEntity createNewTileEntity(World p_149915_1_,
            int p_149915_2_) {
        return g_createNewTileEntity(p_149915_1_, p_149915_2_);
    }

    @Override
    public final TileEntity createTileEntity(World world, int metadata) {
        return createNewTileEntity(world, metadata);
    }

    protected abstract TileType g_createNewTileEntity(World w, int meta);
}
