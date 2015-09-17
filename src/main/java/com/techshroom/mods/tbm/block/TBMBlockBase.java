package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.store;
import static com.techshroom.mods.tbm.Tutils.addressMod;
import static com.techshroom.mods.tbm.Tutils.getSideBaseNoYAxisState;
import static com.techshroom.mods.tbm.Tutils.getSideBaseState;
import static com.techshroom.mods.tbm.Tutils.getSideBlockNoYAxisState;
import static com.techshroom.mods.tbm.Tutils.getSideBlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.Tutils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class TBMBlockBase extends Block {

    private FacingStyle facingStyle = FacingStyle.NONE;

    protected TBMBlockBase(Material mat, String unlocalizedName,
            String blockTexBase) {
        super(mat);
        CreativeTabs blockTab = store.get(TBMKeys.BLOCK_TAB).get();
        setUnlocalizedName(unlocalizedName).setCreativeTab(blockTab);
        // ModelLoader.setCustomStateMapper(this, mapper(unlocalizedName));
    }

    @SuppressWarnings("unchecked")
    protected IStateMapper mapper(String unlocalizedName) {
        return b -> {
            // inferred
            Map<IBlockState, ModelResourceLocation> map = new HashMap<>();
            for (IBlockState state : (List<IBlockState>) getBlockState()
                    .getValidStates()) {
                map.put(state, new ModelResourceLocation(
                        addressMod(unlocalizedName), state.toString()));
            }
            map.put(getDefaultState(), new ModelResourceLocation(
                    addressMod(unlocalizedName), "normal"));
            /*
             * map.put(getDefaultState(), new ModelResourceLocation(
             * addressMod(unlocalizedName), "inventory"));
             */
            return ImmutableMap.copyOf(map);
        };
    }

    protected TBMBlockBase(String unlocalizedName, String blockTexBase) {
        this(Material.iron, unlocalizedName, blockTexBase);
    }

    protected static enum FacingStyle {
        NONE, ALL, HORIZONTAL;
    }

    protected TBMBlockBase setFacingStyle(FacingStyle style) {
        if (style == FacingStyle.ALL) {
            setDefaultState(getSideBaseState(this));
        }
        if (style == FacingStyle.HORIZONTAL) {
            setDefaultState(getSideBaseNoYAxisState(this));
        }
        this.facingStyle = style;
        return this;
    }

    public FacingStyle getFacingStyle() {
        return this.facingStyle;
    }

    @Override
    protected BlockState createBlockState() {
        if (this.facingStyle == FacingStyle.NONE) {
            return super.createBlockState();
        } else if (this.facingStyle == FacingStyle.ALL) {
            return getSideBlockState(this);
        } else if (this.facingStyle == FacingStyle.HORIZONTAL) {
            return getSideBlockNoYAxisState(this);
        } else {
            throw new UnsupportedOperationException(
                    String.valueOf(this.facingStyle));
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
            EntityLivingBase placer, ItemStack stack) {
        if (this.facingStyle != FacingStyle.NONE) {
            EnumFacing facing = null;
            if (this.facingStyle == FacingStyle.ALL) {
            }
            if (this.facingStyle == FacingStyle.HORIZONTAL) {
            }
            worldIn.setBlockState(pos,
                    createBlockState().getBaseState()
                            .withProperty(Tutils.PROP_FACING, facing),
                    Tutils.SetBlockFlag.SEND);
        }
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
