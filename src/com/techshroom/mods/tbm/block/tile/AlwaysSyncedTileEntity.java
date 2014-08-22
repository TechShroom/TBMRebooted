package com.techshroom.mods.tbm.block.tile;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class AlwaysSyncedTileEntity extends TileEntity {
    @Override
    public final void markDirty() {
        super.markDirty();
        localMarkDirty();
        sync();
    }

    // there seems to be an id system?
    private static int packetIDCounter = 5;

    private static HashMap<Class<? extends AlwaysSyncedTileEntity>, Integer> idmap = new HashMap<Class<? extends AlwaysSyncedTileEntity>, Integer>();

    @Override
    public Packet getDescriptionPacket() {
        Class<? extends AlwaysSyncedTileEntity> c = getClass();
        if (!idmap.containsKey(c)) {
            idmap.put(c, packetIDCounter++);
        }
        int id = idmap.get(c);
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        S35PacketUpdateTileEntity p = new S35PacketUpdateTileEntity(xCoord,
                yCoord, zCoord, id, nbt);
        return p;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    public void sync() {
        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void localMarkDirty() {
    }
}
