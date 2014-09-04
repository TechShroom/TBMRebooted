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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.ConvertsToTile;
import com.techshroom.mods.tbm.block.tile.IGuiProvider;
import com.techshroom.mods.tbm.block.tile.IPlayerContainerProvider;

public abstract class TBMEntity<C extends Container, T extends TileEntity, TH extends TBMEntity<C, T, TH>>
        extends Entity implements IGuiProvider<C>,
        IPlayerContainerProvider, ConvertsToTile<T> {
    public TBMEntity(World w) {
        super(w);
    }

    public abstract boolean providesGUI();

    public abstract Block blockBase();

    public final TH withTile(T tile) {
        setLocationAndAngles(tile.xCoord, tile.yCoord, tile.zCoord, 0, 0);
        pWithTile(tile);
        return cast(this);
    }

    protected abstract void pWithTile(T tile);

    public ResourceLocation getEntityTexture() {
        return TextureMap.locationBlocksTexture;
    }

    public int getGUIId() {
        if (providesGUI()) {
            throw new IllegalStateException(
                    "Implementation claims to provide GUI but does not");
        }
        return -1;
    }

    @Override
    public Container container(EntityPlayer player) {
        if (providesGUI()) {
            throw new IllegalStateException(
                    "Implementation claims to provide GUI but does not");
        }
        return null;
    }

    @Override
    public GuiScreen guiScreen(C c) {
        if (providesGUI()) {
            throw new IllegalStateException(
                    "Implementation claims to provide GUI but does not");
        }
        return null;
    }

    public void rightClick(EntityPlayer player, int fx, int fy, int fz) {
        if (providesGUI())
            player.openGui(mod(), getGUIId(), worldObj, fx, fy, fz);
    }
}
