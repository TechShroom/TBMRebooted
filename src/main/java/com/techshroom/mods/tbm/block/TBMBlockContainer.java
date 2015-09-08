package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.*;
import static com.techshroom.mods.tbm.Tutils.*;

import com.techshroom.mods.tbm.TBMKeys;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class TBMBlockContainer<TileType extends TileEntity>
        extends BlockContainer {

    protected TBMBlockContainer(Material mat, String unlocalizedName,
            String blockTexBase) {
        super(mat);
        CreativeTabs blockTab = store.get(TBMKeys.BLOCK_TAB).get();
        setUnlocalizedName(unlocalizedName).setCreativeTab(blockTab);
    }

    protected TBMBlockContainer(String unlocalizedName, String blockTexBase) {
        this(Material.iron, unlocalizedName, blockTexBase);
    }

    @Override
    public boolean onBlockActivated(World w, BlockPos pos, IBlockState state,
            EntityPlayer player, EnumFacing side, float hitX, float hitY,
            float hitZ) {
        TileType tile = cast(w.getTileEntity(pos));
        return onBlockActivated(w, pos, player, side, tile);
    }

    protected boolean onBlockActivated(World w, BlockPos pos, EntityPlayer p,
            EnumFacing side, TileType tile) {
        return false;
    }

    @Override
    public final TileEntity createNewTileEntity(World w, int meta) {
        TileEntity ent = g_createNewTileEntity(w, meta);
        return ent;
    }

    protected abstract TileType g_createNewTileEntity(World w, int meta);
}
