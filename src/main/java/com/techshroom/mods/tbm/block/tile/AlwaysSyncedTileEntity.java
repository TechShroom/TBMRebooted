package com.techshroom.mods.tbm.block.tile;

import static com.techshroom.mods.tbm.Tutils.*;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public abstract class AlwaysSyncedTileEntity extends TileEntity {

    @Override
    public final void markDirty() {
        super.markDirty();
        localMarkDirty();
        sync();
    }

    // there seems to be an id system?
    private static int packetIDCounter = 5;

    private static HashMap<Class<? extends AlwaysSyncedTileEntity>, Integer> idmap =
            new HashMap<Class<? extends AlwaysSyncedTileEntity>, Integer>();

    @Override
    public Packet getDescriptionPacket() {
        Class<? extends AlwaysSyncedTileEntity> c = getClass();
        if (!idmap.containsKey(c)) {
            idmap.put(c, packetIDCounter++);
        }
        int id = idmap.get(c);
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        S35PacketUpdateTileEntity p =
                new S35PacketUpdateTileEntity(this.pos, id, nbt);
        return p;
    }

    @Override
    public void onDataPacket(NetworkManager net,
            S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    public void sync() {
        if (!isClient(this.worldObj)) {
            syncOnServer();
        }
    }

    private void syncOnServer() {
        WorldServer server = cast(this.worldObj);
        List<EntityPlayerMP> players = cast(server.playerEntities);
        for (EntityPlayerMP play : players) {
            play.playerNetServerHandler.sendPacket(getDescriptionPacket());
        }
    }

    public void localMarkDirty() {
    }
}
