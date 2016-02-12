package com.techshroom.mods.tbm.net.messageForClients;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.techshroom.mods.tbm.util.BlockToEntityMapClientStore;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class BlockToEntityMapTranferForClient implements IMessage {

    public static class Handler implements
            IMessageHandler<BlockToEntityMapTranferForClient, IMessage> {

        @Override
        public IMessage onMessage(BlockToEntityMapTranferForClient message,
                MessageContext ctx) {
            BlockToEntityMapClientStore.onPacketUpdate(message.data);
            return null;
        }

    }

    private NBTTagCompound data;

    public BlockToEntityMapTranferForClient(NBTTagCompound data) {
        this.data = data;
    }

    /**
     * @deprecated - Don't use it, it's for Netty.
     */
    @Deprecated
    public BlockToEntityMapTranferForClient() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            int len = buf.readInt();
            byte[] data = new byte[len];
            buf.readBytes(data);
            this.data = CompressedStreamTools
                    .readCompressed(new ByteArrayInputStream(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteArrayOutputStream byteCap = new ByteArrayOutputStream();
        try {
            CompressedStreamTools.writeCompressed(this.data, byteCap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = byteCap.toByteArray();
        buf.writeInt(data.length);
        buf.writeBytes(data);
    }

}
