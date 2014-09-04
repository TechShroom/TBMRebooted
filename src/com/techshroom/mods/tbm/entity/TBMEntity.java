package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.Tutils.cast;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import codechicken.lib.math.MathHelper;

import com.techshroom.mods.tbm.ConvertsToTile;
import com.techshroom.mods.tbm.block.tile.IGuiProvider;
import com.techshroom.mods.tbm.block.tile.IPlayerContainerProvider;

public abstract class TBMEntity<C extends Container, T extends TileEntity, TH extends TBMEntity<C, T, TH>>
        extends Entity implements IGuiProvider<C>, IPlayerContainerProvider,
        ConvertsToTile<T> {
    public TBMEntity(World w) {
        super(w);
        setSize(1.0f, 1.0f);
    }

    public abstract boolean providesGUI();

    public abstract Block blockBase();

    public final TH withTile(T tile) {
        setLocationAndAngles(tile.xCoord + 0.5D, tile.yCoord,
                tile.zCoord + 0.5D, 0, 0);
        pWithTile(tile);
        return cast(this);
    }

    protected abstract void pWithTile(T tile);

    public ResourceLocation getEntityTexture() {
        return TextureMap.locationBlocksTexture;
    }

    public int getGUIId() {
        if (providesGUI()) {
            throw new IllegalStateException(getClass().getSimpleName()
                    + " claims to provide GUI but does not");
        }
        return -1;
    }

    @Override
    public Container container(EntityPlayer player) {
        if (providesGUI()) {
            throw new IllegalStateException(getClass().getSimpleName()
                    + " claims to provide GUI but does not");
        }
        return null;
    }

    @Override
    public GuiScreen guiScreen(C c) {
        if (providesGUI()) {
            throw new IllegalStateException(getClass().getSimpleName()
                    + " claims to provide GUI but does not");
        }
        return null;
    }

    public boolean rightClick(EntityPlayer player, int fx, int fy, int fz) {
        if (providesGUI())
            player.openGui(mod(), getGUIId(), worldObj, fx, fy, fz);
        return providesGUI();
    }

    @Override
    public boolean interactFirst(EntityPlayer p_130002_1_) {
        return rightClick(p_130002_1_, MathHelper.floor_double(posX),
                MathHelper.floor_double(posY), MathHelper.floor_double(posZ))
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
    public boolean handleLavaMovement() {
        return false;
    }

    @Override
    public boolean hitByEntity(Entity p_85031_1_) {
        return super.hitByEntity(p_85031_1_);
    }
}
