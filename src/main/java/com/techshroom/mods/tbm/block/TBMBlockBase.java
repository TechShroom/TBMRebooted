package com.techshroom.mods.tbm.block;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.techshroom.mods.tbm.TBMMod.store;
import static com.techshroom.mods.tbm.Tutils.getSideBaseNoYAxisState;
import static com.techshroom.mods.tbm.Tutils.getSideBaseState;
import static com.techshroom.mods.tbm.Tutils.getSideBlockNoYAxisState;
import static com.techshroom.mods.tbm.Tutils.getSideBlockState;
import static com.techshroom.mods.tbm.Tutils.isClient;

import java.util.List;
import java.util.Optional;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.Tutils;
import com.techshroom.mods.tbm.entity.TBMEntity;
import com.techshroom.mods.tbm.entity.TBMGuiEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public abstract class TBMBlockBase extends Block {

    private FacingStyle facingStyle = FacingStyle.NONE;
    private BlockState blockState = super.getBlockState();

    protected TBMBlockBase(Material mat, String unlocalizedName) {
        super(mat);
        CreativeTabs blockTab = store.get(TBMKeys.BLOCK_TAB).get();
        setUnlocalizedName(unlocalizedName).setCreativeTab(blockTab);
        ModelLoader.setCustomStateMapper(this,
                new BetterStateMapper(unlocalizedName));
    }

    protected TBMBlockBase(String unlocalizedName) {
        this(Material.iron, unlocalizedName);
    }

    protected static enum FacingStyle {
        NONE, ALL, HORIZONTAL;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos,
            IBlockState state, EntityPlayer playerIn, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        Optional<TBMGuiEntity<?>> guiEntity = worldIn
                .getEntitiesWithinAABB(TBMGuiEntity.class,
                        new AxisAlignedBB(pos, pos.add(1, 1, 1)))
                .stream().findFirst().map(Tutils::cast);
        if (guiEntity.isPresent()) {
            guiEntity.get().openRelatedGui(playerIn, pos.getX(), pos.getY(),
                    pos.getZ());
            return true;
        }
        return false;
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
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos,
            EnumFacing side, float hitX, float hitY, float hitZ, int meta,
            EntityLivingBase placer) {
        if (this.facingStyle != FacingStyle.NONE) {
            EnumFacing facing = null;
            IProperty<EnumFacing> prop = null;
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
            return createBlockState().getBaseState().withProperty(prop, facing);
        }
        return createBlockState().getBaseState();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
            EntityLivingBase placer, ItemStack stack) {
        try {
            if (!isClient(worldIn)) {
                Entity spawned =
                        spawnEntity(worldIn, pos, worldIn.getBlockState(pos));
                checkNotNull(spawned);
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
        // worldIn.setBlockToAir(pos);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos,
            IBlockState state) {
        List<?> entitiesWithinAABB =
                world.getEntitiesWithinAABB(TBMEntity.class, aabbFromPos(pos));
        if (entitiesWithinAABB.isEmpty()) {
            return;
        }
        TBMEntity linked = (TBMEntity) entitiesWithinAABB.get(0);
        linked.setDead();
    }

    private AxisAlignedBB aabbFromPos(BlockPos pos) {
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
    }

    public abstract Entity spawnEntity(World world, BlockPos pos,
            IBlockState state);

}
