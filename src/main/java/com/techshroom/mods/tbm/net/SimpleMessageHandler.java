package com.techshroom.mods.tbm.net;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public interface SimpleMessageHandler<REQ extends IMessage>
        extends IMessageHandler<REQ, IMessage> {

    interface Client<REQ extends IMessage> extends SimpleMessageHandler<REQ> {

        @Override
        default Side getSide() {
            return Side.CLIENT;
        }

    }

    interface Server<REQ extends IMessage> extends SimpleMessageHandler<REQ> {

        @Override
        default Side getSide() {
            return Side.SERVER;
        }

    }

    @Override
    default IMessage onMessage(REQ message, MessageContext ctx) {
        MessageProcessUtility.processOnMCThread(
                () -> processOnMCThread(message, ctx), getSide());
        return null;
    }

    Side getSide();

    void processOnMCThread(REQ message, MessageContext ctx);

}
