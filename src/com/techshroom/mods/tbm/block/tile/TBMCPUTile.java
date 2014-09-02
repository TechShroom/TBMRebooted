package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.TBMMod.*;
import static com.techshroom.mods.tbm.Tutils.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockSourceImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

import com.techshroom.mods.tbm.ConvertsToEntity;
import com.techshroom.mods.tbm.MessageCPUStartClient;
import com.techshroom.mods.tbm.entity.TBMCPUEntity;
import com.techshroom.mods.tbm.entity.TBMEntity;
import com.techshroom.mods.tbm.gui.GuiTBMCPU;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TBMCPUTile extends AlwaysSyncedCPUTile implements
        ConvertsToEntity<TBMCPUEntity>, IPlayerContainerProvider,
        IGuiProvider<Container> {
    public TBMCPUTile() {
        insertWhenPossible(this);
    }

    private List<TileEntity> insertLater = new ArrayList<TileEntity>();
    private List<TileEntity> tiles = new ArrayList<TileEntity>();

    private void insertWhenPossible(TileEntity tile) {
        insertLater.add(tile);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj != null) {
            for (TileEntity inserting : insertLater) {
                insertTileEntity(inserting);
            }
        }
    }

    public void insertTileEntity(TileEntity ref) {
        if (ref.getWorldObj() != worldObj) {
            mod().log.warn("TE not in same world, rejecting");
            return;
        }
        if (isClient(worldObj)) {
            // reject client side
            return;
        }
        tiles.add(ref);
    }

    public void removeTileEntity(TileEntity ref) {
        tiles.remove(ref);
    }

    public void guiStart() {
        if (isClient(worldObj)) {
            client_sendStart();
            Minecraft.getMinecraft().displayGuiScreen(null);
        } else {
            for (TileEntity tile : tiles) {
                if (tile instanceof ConvertsToEntity) {
                    ConvertsToEntity<TBMEntity<Container, TileEntity>> tileToEntity =
                            cast(tile);
                    worldObj.spawnEntityInWorld(tileToEntity.convertToEntity());
                    worldObj.setBlockToAir(tile.xCoord, tile.yCoord,
                            tile.zCoord);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void client_sendStart() {
        SimpleNetworkWrapper serverHook = store_get("channel");
        serverHook.sendToServer(new MessageCPUStartClient(new BlockSourceImpl(
                worldObj, xCoord, yCoord, zCoord)));
    }

    @Override
    public TBMCPUEntity convertToEntity() {
        return new TBMCPUEntity(worldObj).withTile(this);
    }

    public void fireGUIOpenRequest(EntityPlayer player) {
        player.openGui(mod(), getGUIId(), worldObj, xCoord, yCoord, zCoord);
    }

    private int getGUIId() {
        return (Integer) store_get("cpu-gui-id");
    }

    public GuiScreen guiScreenFromEntity(Container c, TBMCPUEntity entity) {
        return new GuiTBMCPU(entity);
    }

    @Override
    public GuiScreen guiScreen(Container c) {
        return new GuiTBMCPU(this);
    }

    @Override
    public Container container(EntityPlayer player) {
        return IPlayerContainerProvider.NULL.container(player);
    }
}
