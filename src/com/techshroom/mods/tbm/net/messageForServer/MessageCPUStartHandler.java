package com.techshroom.mods.tbm.net.messageForServer;

import com.techshroom.mods.tbm.block.tile.TBMCPUTile;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageCPUStartHandler implements
        IMessageHandler<MessageCPUStartClient, IMessage> {

    @Override
    public IMessage
            onMessage(MessageCPUStartClient message, MessageContext ctx) {
        TBMCPUTile serverTile = message.getTile();
        serverTile.guiStart();
        return null;
    }
}
