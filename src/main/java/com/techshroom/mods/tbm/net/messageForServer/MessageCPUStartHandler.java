package com.techshroom.mods.tbm.net.messageForServer;

import com.techshroom.mods.tbm.block.tile.TBMCPUTile;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
