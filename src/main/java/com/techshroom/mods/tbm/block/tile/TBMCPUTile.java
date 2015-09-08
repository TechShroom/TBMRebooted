package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.TBMMod.store;
import static com.techshroom.mods.tbm.Tutils.cast;
import static com.techshroom.mods.tbm.Tutils.isClient;

import java.util.ArrayList;
import java.util.List;

import com.techshroom.mods.tbm.ConvertsToEntity;
import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.block.BlockFlags;
import com.techshroom.mods.tbm.entity.TBMCPUEntity;
import com.techshroom.mods.tbm.entity.TBMEntity;
import com.techshroom.mods.tbm.gui.GuiTBMCPU;
import com.techshroom.mods.tbm.gui.container.ContainerTBMCPU;
import com.techshroom.mods.tbm.net.messageForServer.MessageCPUStartClient;

import net.minecraft.block.BlockSourceImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TBMCPUTile extends AlwaysSyncedCPUTile
        implements ConvertsToEntity<TBMCPUEntity>, IPlayerContainerProvider,
        IGuiProvider<ContainerTBMCPU> {

    public TBMCPUTile() {
        setCPUTile(this);
        insertTileEntity(this);
    }

    private List<TileEntity> insertLater = new ArrayList<TileEntity>();
    private List<TileEntity> tiles = new ArrayList<TileEntity>();

    private void insertWhenPossible(TileEntity tile) {
        this.insertLater.add(tile);
    }

    @Override
    public void update() {
        super.update();
        if (this.worldObj != null) {
            for (TileEntity inserting : this.insertLater) {
                insertTileEntity(inserting);
            }
            this.insertLater.clear();
        }
    }

    public void insertTileEntity(TileEntity ref) {
        if (ref.getWorld() == null || this.worldObj == null) {
            insertWhenPossible(ref);
            return;
        }
        if (ref.getWorld() != this.worldObj) {
            mod().log.warn("TE not in same world, rejecting");
            return;
        }
        if (isClient(this.worldObj)) {
            // reject client side
            return;
        }
        if (this.tiles.contains(ref)) {
            // dupes
            return;
        }
        mod().log.trace(ref);
        this.tiles.add(ref);
    }

    public void removeTileEntity(TileEntity ref) {
        this.tiles.remove(ref);
    }

    public void guiStart() {
        if (isClient(this.worldObj)) {
            client_sendStart();
            Minecraft.getMinecraft().displayGuiScreen(null);
        } else {
            BlockFlags.DOING_TILE_TO_ENTITY_TRANSFER = true;
            for (TileEntity tile : this.tiles) {
                if (tile instanceof ConvertsToEntity) {
                    ConvertsToEntity<TBMEntity<Container, TileEntity, ?>> tileToEntity =
                            cast(tile);
                    mod().log.trace("SPAWN SERVER " + tileToEntity);
                    this.worldObj
                            .spawnEntityInWorld(tileToEntity.convertToEntity());
                    this.worldObj.setBlockToAir(tile.getPos());
                }
            }
            BlockFlags.DOING_TILE_TO_ENTITY_TRANSFER = false;
        }
    }

    static {
        new EntityJoinWorldEvent(null, null).getListenerList().register(0,
                EventPriority.LOWEST, new IEventListener() {

                    @Override
                    public void invoke(Event event) {
                        EntityJoinWorldEvent e = cast(event);
                        entitySpawnOfTheDevil(e);
                    }
                });

    }

    @SubscribeEvent
    public static void entitySpawnOfTheDevil(EntityJoinWorldEvent spawner) {
        if (isClient(spawner.world) && spawner.entity instanceof TBMEntity) {
            mod().log.trace("CLIENT SPAWN " + spawner.entity);
            mod().log
                    .trace("[FactSpewer] Did you know that this world's dimension is "
                            + spawner.world.provider.getDimensionId());
        }
    }

    @SideOnly(Side.CLIENT)
    private void client_sendStart() {
        SimpleNetworkWrapper serverHook = mod().netmanager().netWrapper();
        serverHook.sendToServer(new MessageCPUStartClient(
                new BlockSourceImpl(this.worldObj, this.pos)));
    }

    @Override
    public TBMCPUEntity convertToEntity() {
        return new TBMCPUEntity(this.worldObj).withTile(this);
    }

    public void fireGUIOpenRequest(EntityPlayer player) {
        player.openGui(mod(), getGUIId(), this.worldObj, this.pos.getX(),
                this.pos.getY(), this.pos.getZ());
    }

    public int getGUIId() {
        return store.get(TBMKeys.GuiId.CPU).getAsInt();
    }

    public GuiScreen guiScreenFromEntity(ContainerTBMCPU c,
            TBMCPUEntity entity) {
        return new GuiTBMCPU(entity);
    }

    @Override
    public GuiScreen guiScreen(ContainerTBMCPU c) {
        return new GuiTBMCPU(this);
    }

    @Override
    public Container container(EntityPlayer player) {
        return new ContainerTBMCPU(player);
    }
}
