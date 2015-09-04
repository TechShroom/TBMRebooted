package com.techshroom.mods.tbm.net.messageForClients;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.Tutils.cast;

import java.nio.charset.Charset;

import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class TileEntMessageForClient<TileType extends TileEntity>
        implements IMessage {

    private IBlockSource location;

    public TileEntMessageForClient(IBlockSource loc) {
        this.location = loc;
    }

    /**
     * @deprecated - Don't use it, it's for Netty.
     */
    @Deprecated
    public TileEntMessageForClient() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int dimID = buf.readInt();
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        if (theWorld.provider.getDimensionId() != dimID) {
            mod().log.info("received dimension ID " + dimID
                    + " was different from the current world ID "
                    + theWorld.provider.getDimensionId()
                    + ", probably safe to ignore this.");
        }
        this.location = new BlockSourceImpl(theWorld,
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
            extends TileEntMessageForClient<TileType> {

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
