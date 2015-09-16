package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public abstract class TBMBlockBase extends Block {

    protected TBMBlockBase(Material mat, String unlocalizedName,
            String blockTexBase) {
        super(mat);
        CreativeTabs blockTab = store.get(TBMKeys.BLOCK_TAB).get();
        setUnlocalizedName(unlocalizedName).setCreativeTab(blockTab);
    }

    protected TBMBlockBase(String unlocalizedName, String blockTexBase) {
        this(Material.iron, unlocalizedName, blockTexBase);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
            EntityLivingBase placer, ItemStack stack) {
        try {
            Entity spawned = spawnEntity(worldIn, pos, state);
            if (spawned == null) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            if (placer.sendCommandFeedback()) {
                placer.addChatMessage(new ChatComponentText(
                        "Failed to perform block-entity swap at " + pos
                                + "; send the logs to the author (error: "
                                + e.getMessage() + ")"));
            }
            e.printStackTrace();
        }
        worldIn.setBlockToAir(pos);
    }

    public abstract Entity spawnEntity(World world, BlockPos pos,
            IBlockState state);

}
