package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.Tutils.*;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import codechicken.lib.math.MathHelper;

import com.techshroom.mods.tbm.ConvertsToTile;
import com.techshroom.mods.tbm.Tutils;
import com.techshroom.mods.tbm.block.tile.AlwaysSyncedSidedTile;
import com.techshroom.mods.tbm.block.tile.IGuiProvider;
import com.techshroom.mods.tbm.block.tile.IPlayerContainerProvider;
import com.techshroom.mods.tbm.block.tile.TBMCPUTile;

public abstract class TBMEntity<C extends Container, T extends TileEntity, TH extends TBMEntity<C, T, TH>>
        extends Entity implements IGuiProvider<C>, IPlayerContainerProvider,
        ConvertsToTile<T> {
    /**
     * No extra collision border
     */
    private static final float NO_BORDER = 0.0f;
    private static final float FULL_BOX_SIZE = 1f;
    private static final float BOX_SIZE = FULL_BOX_SIZE;
    protected T tref;

    public TBMEntity(World w) {
        super(w);
        setSize(BOX_SIZE, BOX_SIZE);
    }

    public final boolean providesGUI() {
        return (this.tref instanceof IPlayerContainerProvider && this.tref instanceof IGuiProvider)
                || specialGUICase();
    }

    protected boolean specialGUICase() {
        return false;
    }

    public abstract Block blockBase();

    public final TH withTile(T tile) {
        setLocationAndAngles(tile.getPos().getX() + 0.5D, tile.getPos().getY(),
                tile.getPos().getY() + 0.5D, 0, 0);
        pWithTile(tile);
        return cast(this);
    }

    protected void pWithTile(T tile) {
        this.tref = tile;
    }

    @Override
    public final T convertToTile() {
        return this.tref;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        NBTTagCompound tileBase = nbt.getCompoundTag("tile");
        pWithTile(Tutils.<T> cast(blockBase().createTileEntity(this.worldObj, blockBase().getDefaultState())));
        this.tref.readFromNBT(tileBase);
        System.err.println(isClient(this.worldObj) + "=>" + "READ-A");
        readNBT(nbt);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        NBTTagCompound tileBase = nbt.getCompoundTag("tile");
        this.tref.writeToNBT(tileBase);
        nbt.setTag("tile", tileBase);
        System.err.println(isClient(this.worldObj) + "=>" + "WRITE-A");
        writeNBT(nbt);
    }

    protected void readNBT(NBTTagCompound nbt) {
    }

    protected void writeNBT(NBTTagCompound nbt) {
    }

    public ResourceLocation getEntityTexture() {
        return TextureMap.locationBlocksTexture;
    }

    public int getGUIId() {
        if (providesGUI() && specialGUICase()) {
            throw new IllegalStateException(getClass().getSimpleName()
                    + " claims to provide GUI but does not");
        } else if (providesGUI()) {
            if (this.tref instanceof AlwaysSyncedSidedTile) {
                return ((AlwaysSyncedSidedTile) this.tref).getGUIId();
            } else if (this.tref instanceof TBMCPUTile) {
                return ((TBMCPUTile) this.tref).getGUIId();
            } else {
                throw new IllegalStateException("Unhandled: " + this.tref.getClass());
            }
        }
        return -1;
    }

    @Override
    public Container container(EntityPlayer player) {
        if (providesGUI() && specialGUICase()) {
            throw new IllegalStateException(getClass().getSimpleName()
                    + " claims to provide GUI but does not");
        } else if (providesGUI()) {
            IPlayerContainerProvider contP = cast(this.tref);
            return contP.container(player);
        }
        return null;
    }

    @Override
    public GuiScreen guiScreen(C c) {
        if (providesGUI() && specialGUICase()) {
            throw new IllegalStateException(getClass().getSimpleName()
                    + " claims to provide GUI but does not");
        } else if (providesGUI()) {
            IGuiProvider<C> guiP = cast(this.tref);
            return guiP.guiScreen(c);
        }
        return null;
    }

    public boolean rightClick(EntityPlayer player, int fx, int fy, int fz) {
        if (providesGUI() && !isClient(this.worldObj))
            player.openGui(mod(), getGUIId(), this.worldObj, fx, fy, fz);
        return providesGUI();
    }

    @Override
    public boolean interactFirst(EntityPlayer p_130002_1_) {
        return rightClick(p_130002_1_, MathHelper.floor_double(this.posX),
                MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))
                || super.interactFirst(p_130002_1_);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void applyEntityCollision(Entity p_70108_1_) {
        // super.applyEntityCollision(p_70108_1_);
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    public boolean hitByEntity(Entity p_85031_1_) {
        return super.hitByEntity(p_85031_1_);
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return this.getEntityBoundingBox();
    }
    
    @Override
    public float getCollisionBorderSize() {
        return NO_BORDER;
    }
}
