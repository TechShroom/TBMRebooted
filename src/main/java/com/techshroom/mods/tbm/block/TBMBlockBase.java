package com.techshroom.mods.tbm.block;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.techshroom.mods.tbm.TBMMod.store;
import static com.techshroom.mods.tbm.Tutils.getSideBaseNoYAxisState;
import static com.techshroom.mods.tbm.Tutils.getSideBaseState;
import static com.techshroom.mods.tbm.Tutils.getSideBlockNoYAxisState;
import static com.techshroom.mods.tbm.Tutils.getSideBlockState;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.Tutils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public abstract class TBMBlockBase extends Block {

    private FacingStyle facingStyle = FacingStyle.NONE;
    private BlockState blockState = super.getBlockState();

    protected TBMBlockBase(Material mat, String unlocalizedName,
            String blockTexBase) {
        super(mat);
        CreativeTabs blockTab = store.get(TBMKeys.BLOCK_TAB).get();
        setUnlocalizedName(unlocalizedName).setCreativeTab(blockTab);
        ModelLoader.setCustomStateMapper(this,
                new BetterStateMapper(unlocalizedName));
    }

    protected TBMBlockBase(String unlocalizedName, String blockTexBase) {
        this(Material.iron, unlocalizedName, blockTexBase);
    }

    protected static enum FacingStyle {
        NONE, ALL, HORIZONTAL;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (this.facingStyle == FacingStyle.NONE) {
            return super.getStateFromMeta(meta);
        } else {
            return getBlockState().getBaseState().withProperty(
                    (this.facingStyle == FacingStyle.ALL ? Tutils.PROP_FACING
                            : Tutils.PROP_FACING_HORIZ),
                    EnumFacing.getFront(meta));
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (this.facingStyle == FacingStyle.NONE) {
            return super.getMetaFromState(state);
        } else {
            return ((EnumFacing) state
                    .getValue((this.facingStyle == FacingStyle.ALL
                            ? Tutils.PROP_FACING : Tutils.PROP_FACING_HORIZ)))
                                    .getIndex();
        }
    }

    protected TBMBlockBase setFacingStyle(FacingStyle style) {
        checkNotNull(style);
        if (style == FacingStyle.ALL) {
            setDefaultState(getSideBaseState(this));
        }
        if (style == FacingStyle.HORIZONTAL) {
            setDefaultState(getSideBaseNoYAxisState(this));
        }
        this.facingStyle = style;
        this.blockState = createBlockState();
        return this;
    }

    public FacingStyle getFacingStyle() {
        return this.facingStyle;
    }

    @Override
    protected BlockState createBlockState() {
        // null -> early block constr. call
        if (this.facingStyle == null || this.facingStyle == FacingStyle.NONE) {
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
    public BlockState getBlockState() {
        return this.blockState;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
            EntityLivingBase placer, ItemStack stack) {
        if (this.facingStyle != FacingStyle.NONE) {
            EnumFacing facing = null;
            IProperty prop = null;
            if (this.facingStyle == FacingStyle.ALL) {
                facing = Tutils.getFacing(pos, placer);
                prop = Tutils.PROP_FACING;
            } else if (this.facingStyle == FacingStyle.HORIZONTAL) {
                facing = Tutils.getHorizontalFacing(placer);
                prop = Tutils.PROP_FACING_HORIZ;
            } else {
                throw new UnsupportedOperationException(
                        String.valueOf(this.facingStyle));
            }
            worldIn.setBlockState(pos, createBlockState().getBaseState()
                    .withProperty(prop, facing), Tutils.SetBlockFlag.SEND);
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
