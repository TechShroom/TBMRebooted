package com.techshroom.mods.tbm.gui;

import static com.techshroom.mods.tbm.Tutils.cast;
import static com.techshroom.mods.tbm.Tutils.isClient;
import static com.techshroom.mods.tbm.Tutils.throwing;

import java.util.List;

import com.techshroom.mods.tbm.block.tile.IContainerProvider;
import com.techshroom.mods.tbm.block.tile.IGuiProvider;
import com.techshroom.mods.tbm.block.tile.IPlayerContainerProvider;
import com.techshroom.mods.tbm.entity.TBMEntity;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world,
            int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return getContainer(ID, player, world, pos);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
            int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return getGui(ID, player, world, pos);
    }

    private GuiScreen getGui(int id, EntityPlayer player, World world,
            BlockPos pos) {
        Object source = world.getTileEntity(pos);
        Container c = getContainer(source, player, pos);
        if (source == null) {
            List<?> l = player.worldObj.getEntitiesWithinAABB(TBMEntity.class,
                    AxisAlignedBB.fromBounds(pos.getX() - 0.5, pos.getY() - 0.5,
                            pos.getZ() - 0.5, pos.getX() + 0.5,
                            pos.getY() + 0.5, pos.getZ() + 0.5));
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
                throwing(new RuntimeException("No source @ " + pos));
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
            BlockPos pos) {
        return getContainer(world.getTileEntity(pos), player, pos);
    }

    private Container getContainer(Object source, EntityPlayer player,
            BlockPos pos) {
        if (source == null) {
            List<?> l = player.worldObj.getEntitiesWithinAABB(TBMEntity.class,
                    AxisAlignedBB.fromBounds(pos.getX() - 0.5, pos.getY() - 0.5,
                            pos.getZ() - 0.5, pos.getX() + 0.5,
                            pos.getY() + 0.5, pos.getZ() + 0.5));
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
