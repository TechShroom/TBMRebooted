package com.techshroom.mods.tbm.block;

import com.techshroom.mods.tbm.Tutils;
import com.techshroom.mods.tbm.Tutils.MetadataConstants;
import com.techshroom.mods.tbm.block.tile.TBMDrillTile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TBMDrill extends TBMBlockContainer<TBMDrillTile> {

    public TBMDrill() {
        super("drill", "drill-tex");
    }

    @Override
    protected TBMDrillTile g_createNewTileEntity(World w, int meta) {
        return new TBMDrillTile();
    }

    @Override
    protected boolean onBlockActivated(World w, BlockPos pos, EntityPlayer p,
            EnumFacing side, TBMDrillTile tile) {
        tile.fireGUIOpenRequest(p);
        return true;
    }

    @Override
    public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state,
            EntityLivingBase ent, ItemStack stack) {
        super.onBlockPlacedBy(w, pos, state, ent, stack);
        w.setBlockState(pos,
                Tutils.createStateForSideByEntityRotation(this, pos, ent),
                MetadataConstants.SEND);
    }
}
