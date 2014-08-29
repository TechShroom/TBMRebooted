package com.techshroom.mods.tbm.gui;

import static com.techshroom.mods.tbm.Tutils.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.IContainerProvider;
import com.techshroom.mods.tbm.block.tile.IGuiProvider;
import com.techshroom.mods.tbm.block.tile.IPlayerContainerProvider;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world,
            int x, int y, int z) {
        return getContainer(ID, player, world, x, y, z);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
            int x, int y, int z) {
        return getGui(ID, player, world, x, y, z);
    }

    private GuiScreen getGui(int id, EntityPlayer player, World world, int x,
            int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        Container c = getContainer(tile, player, x, y, z);
        IGuiProvider<Container> tileAsGP = IGuiProvider.NULL;
        try {
            tileAsGP = cast(tile);
        } catch (ClassCastException e) {
            throwing(e);
        }
        return tileAsGP.guiScreen(c);
    }

    private Container getContainer(int id, EntityPlayer player, World world,
            int x, int y, int z) {
        return getContainer(world.getTileEntity(x, y, z), player, x, y, z);
    }

    private Container getContainer(TileEntity tile, EntityPlayer player, int x,
            int y, int z) {
        if (tile == null) {
            throwing(new RuntimeException("No tile @ (" + x + "," + y + "," + z
                    + ")"));
        }
        if (tile instanceof IPlayerContainerProvider) {
            return ((IPlayerContainerProvider) tile).container(player);
        }
        if (tile instanceof IContainerProvider) {
            return ((IContainerProvider) tile).container();
        }
        return null;
    }
}
