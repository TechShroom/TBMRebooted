package com.techshroom.mods.tbm.net.messageForServer;

import static com.techshroom.mods.tbm.Tutils.cast;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import net.minecraft.block.BlockSourceImpl;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public abstract class TileEntMessageForServer<TileType extends TileEntity>
        implements IMessage {
    private IBlockSource location;

    public TileEntMessageForServer(IBlockSource loc) {
        location = loc;
    }

    /**
     * @deprecated - Don't use it, it's for Netty.
     */
    @Deprecated
    public TileEntMessageForServer() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        location =
                new BlockSourceImpl(MinecraftServer.getServer()
                        .worldServerForDimension(buf.readInt()), buf.readInt(),
                        buf.readInt(), buf.readInt());
        fromBAfter(buf);
    }

    public void fromBAfter(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(location.getWorld().provider.dimensionId);
        buf.writeInt(location.getXInt());
        buf.writeInt(location.getYInt());
        buf.writeInt(location.getZInt());
        toBAfter(buf);
    }

    public void toBAfter(ByteBuf buf) {
    }

    public TileType getTile() {
        return cast(location.getBlockTileEntity());
    }

    public static class Simple<TileType extends TileEntity>
            extends TileEntMessageForServer<TileType> {
        private static final Charset THE_CHARSET = Charsets.UTF_8;
        private String message;

        public Simple(IBlockSource location, String msg) {
            super(location);
            message = msg;
        }

        /**
         * @deprecated - Don't use it, it's for Netty.
         */
        @Deprecated
        public Simple() {
            super();
        }

        @Override
        public void toBAfter(ByteBuf buf) {
            byte[] bytes = message.getBytes(THE_CHARSET);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        }

        @Override
        public void fromBAfter(ByteBuf buf) {
            byte[] bytes = new byte[buf.readInt()];
            buf.readBytes(bytes, 0, bytes.length);
            message = new String(bytes, THE_CHARSET);
        }

        public String getMessage() {
            return message;
        }
    }
}
