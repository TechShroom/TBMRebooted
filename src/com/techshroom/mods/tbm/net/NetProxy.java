package com.techshroom.mods.tbm.net;

import static com.techshroom.mods.tbm.TBMMod.*;

import com.techshroom.mods.tbm.net.messageForServer.MessageCPUStartClient;
import com.techshroom.mods.tbm.net.messageForServer.MessageCPUStartHandler;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public abstract class NetProxy {
    private static final SimpleNetworkWrapper netmanager = store_put("channel",
            NetworkRegistry.INSTANCE.newSimpleChannel(mod().id()));

    public SimpleNetworkWrapper netWrapper() {
        return netmanager;
    }

    private static int discCounter = 0;

    public static int requestDiscriminationID() {
        return discCounter++;
    }

    public static final class Client
            extends NetProxy {
        private final NetProxy.Server server = new NetProxy.Server();

        @Override
        public void doNetRegistrationsForSide() {
            mod().log.trace("client net registrations start");
            mod().log.trace("client net registrations end");
            server.doNetRegistrationsForSide();
        }

        @Override
        public boolean extendingOther() {
            return true;
        }

        @Override
        public Side getSide() {
            return Side.CLIENT;
        }
    }

    public static final class Server
            extends NetProxy {
        @Override
        public void doNetRegistrationsForSide() {
            mod().log.trace("server net registrations start");
            registerMessage(MessageCPUStartHandler.class,
                    MessageCPUStartClient.class, requestDiscriminationID());
            mod().log.trace("server net registrations end");
        }

        @Override
        public Side getSide() {
            return Side.SERVER;
        }
    }

    public abstract void doNetRegistrationsForSide();

    public boolean extendingOther() {
        return false;
    }

    public abstract Side getSide();

    public boolean isClient() {
        return getSide() == Side.CLIENT;
    }

    public boolean isServer() {
        return getSide() == Side.SERVER;
    }

    protected
            <REQ extends IMessage, REPLY extends IMessage>
            void
            registerMessage(
                    Class<? extends IMessageHandler<REQ, REPLY>> messageHandler,
                    Class<REQ> requestMessageType, int discriminator) {
        netWrapper().registerMessage(messageHandler, requestMessageType,
                discriminator, getSide());
    }
}
