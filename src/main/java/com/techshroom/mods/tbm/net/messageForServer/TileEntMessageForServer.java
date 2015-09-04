package com.techshroom.mods.tbm.net.messageForServer;

import static com.techshroom.mods.tbm.Tutils.cast;

import java.nio.charset.Charset;

import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class TileEntMessageForServer<TileType extends TileEntity>
        implements IMessage {

    private IBlockSource location;

    public TileEntMessageForServer(IBlockSource loc) {
        this.location = loc;
    }

    /**
     * @deprecated - Don't use it, it's for Netty.
     */
    @Deprecated
    public TileEntMessageForServer() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.location = new BlockSourceImpl(
                MinecraftServer.getServer()
                        .worldServerForDimension(buf.readInt()),
                new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
        fromBAfter(buf);
    }

    public void fromBAfter(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.location.getWorld().provider.getDimensionId());
        buf.writeInt(this.location.getBlockPos().getX());
        buf.writeInt(this.location.getBlockPos().getY());
        buf.writeInt(this.location.getBlockPos().getZ());
        toBAfter(buf);
    }

    public void toBAfter(ByteBuf buf) {
    }

    public TileType getTile() {
        return cast(this.location.getBlockTileEntity());
    }

    public static class Simple<TileType extends TileEntity>
            extends TileEntMessageForServer<TileType> {

        private static final Charset THE_CHARSET = Charsets.UTF_8;
        private String message;

        public Simple(IBlockSource location, String msg) {
            super(location);
            this.message = msg;
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
            byte[] bytes = this.message.getBytes(THE_CHARSET);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        }

        @Override
        public void fromBAfter(ByteBuf buf) {
            byte[] bytes = new byte[buf.readInt()];
            buf.readBytes(bytes, 0, bytes.length);
            this.message = new String(bytes, THE_CHARSET);
        }

        public String getMessage() {
            return this.message;
        }
    }
}
