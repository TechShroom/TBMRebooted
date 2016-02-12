package com.techshroom.mods.tbm.gui;

import com.techshroom.mods.tbm.util.BlockToEntityMap;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
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
        Container cont = getContainer(id, player, world, pos);
        Entity ent = BlockToEntityMap.getForWorld(world).get(pos);
        if (ent instanceof IGuiProvider) {
            @SuppressWarnings("unchecked")
            IGuiProvider<Container> gp = (IGuiProvider<Container>) ent;
            return gp.guiScreen(cont);
        }
        return null;
    }

    private Container getContainer(int id, EntityPlayer player, World world,
            BlockPos pos) {
        Entity source = BlockToEntityMap.getForWorld(world).get(pos);
        if (source instanceof IPlayerContainerProvider) {
            return ((IPlayerContainerProvider) source).container(player);
        }
        if (source instanceof IContainerProvider) {
            return ((IContainerProvider) source).container();
        }
        return null;
    }
}
