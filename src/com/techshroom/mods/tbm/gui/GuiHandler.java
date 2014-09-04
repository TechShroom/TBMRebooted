package com.techshroom.mods.tbm.gui;

import static com.techshroom.mods.tbm.Tutils.*;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.IContainerProvider;
import com.techshroom.mods.tbm.block.tile.IGuiProvider;
import com.techshroom.mods.tbm.block.tile.IPlayerContainerProvider;
import com.techshroom.mods.tbm.entity.TBMEntity;

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
        Object source = world.getTileEntity(x, y, z);
        Container c = getContainer(source, player, x, y, z);
        if (source == null) {
            List<?> l =
                    player.worldObj.getEntitiesWithinAABB(TBMEntity.class,
                            AxisAlignedBB.getBoundingBox(x - 0.5, y - 0.5,
                                    z - 0.5, x - 0.5, y - 0.5, z - 0.5));
            for (Object o : l) {
                if (o instanceof IPlayerContainerProvider) {
                    source = o;
                } else if (o instanceof IContainerProvider) {
                    source = o;
                }
                if (source != null) {
                    break;
                }
            }
            if (source == null)
                throwing(new RuntimeException("No source @ (" + x + "," + y
                        + "," + z + ")"));
        }
        IGuiProvider<Container> tileAsGP = IGuiProvider.NULL;
        try {
            tileAsGP = cast(source);
        } catch (ClassCastException e) {
            throwing(e);
        }
        return tileAsGP.guiScreen(c);
    }

    private Container getContainer(int id, EntityPlayer player, World world,
            int x, int y, int z) {
        return getContainer(world.getTileEntity(x, y, z), player, x, y, z);
    }

    private Container getContainer(Object source, EntityPlayer player, int x,
            int y, int z) {
        if (source == null) {
            List<?> l =
                    player.worldObj.getEntitiesWithinAABB(TBMEntity.class,
                            AxisAlignedBB.getBoundingBox(x - 0.5, y - 0.5,
                                    z - 0.5, x - 0.5, y - 0.5, z - 0.5));
            for (Object o : l) {
                if (o instanceof IPlayerContainerProvider) {
                    source = o;
                } else if (o instanceof IContainerProvider) {
                    source = o;
                }
                if (source != null) {
                    break;
                }
            }
            if (source == null)
                source = IContainerProvider.NULL;
            /*
             * throwing(new RuntimeException("No source @ (" + x + "," + y + ","
             * + z + ")"));
             */
        }
        System.err.println(isClient(player.worldObj) + "=>" + source);
        if (source instanceof IPlayerContainerProvider) {
            return ((IPlayerContainerProvider) source).container(player);
        }
        if (source instanceof IContainerProvider) {
            return ((IContainerProvider) source).container();
        }
        return null;
    }
}
