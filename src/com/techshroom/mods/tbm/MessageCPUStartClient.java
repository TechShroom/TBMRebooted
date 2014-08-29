package com.techshroom.mods.tbm;

import static com.techshroom.mods.tbm.Tutils.cast;

import com.techshroom.mods.tbm.block.tile.TBMCPUTile;

import net.minecraft.block.BlockSourceImpl;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.server.MinecraftServer;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class MessageCPUStartClient implements IMessage {
    private IBlockSource location;

    public MessageCPUStartClient(IBlockSource loc) {
        location = loc;
    }

    public MessageCPUStartClient() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        location =
                new BlockSourceImpl(MinecraftServer.getServer()
                        .worldServerForDimension(buf.readInt()), buf.readInt(),
                        buf.readInt(), buf.readInt());
        System.err.println("BUF LOADED");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(location.getWorld().provider.dimensionId);
        buf.writeInt(location.getXInt());
        buf.writeInt(location.getYInt());
        buf.writeInt(location.getZInt());
        System.err.println("BUF WRITTEN");
    }

    public TBMCPUTile getTile() {
        return cast(location.getBlockTileEntity());
    }
}
