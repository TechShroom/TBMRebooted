package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.Tutils.isClient;

import com.techshroom.mods.tbm.Tutils;
import com.techshroom.mods.tbm.Tutils.SetBlockFlag;
import com.techshroom.mods.tbm.entity.TBMDrillEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMDrill extends TBMBlockBase {

    public TBMDrill() {
        super("drill", "drill-tex");
        setFacingStyle(FacingStyle.ALL);
    }

    @Override
    public Entity spawnEntity(World world, BlockPos pos, IBlockState state) {
        if (isClient(world)) {
            return null;
        }
        TBMDrillEntity entity = new TBMDrillEntity(world, state);
        entity.moveToBlockPosAndAngles(pos, 0, 0);
        world.spawnEntityInWorld(entity);
        return entity;
    }

    @Override
    public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state,
            EntityLivingBase ent, ItemStack stack) {
        w.setBlockState(pos,
                Tutils.createStateForSideByEntityRotation(this, pos, ent),
                SetBlockFlag.SEND);
        super.onBlockPlacedBy(w, pos, w.getBlockState(pos), ent, stack);
    }

}
