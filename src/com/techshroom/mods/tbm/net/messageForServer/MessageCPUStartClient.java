package com.techshroom.mods.tbm.net.messageForServer;

import com.techshroom.mods.tbm.block.tile.TBMCPUTile;

import net.minecraft.dispenser.IBlockSource;

public class MessageCPUStartClient
        extends TileEntMessageForServer.Simple<TBMCPUTile> {
    public MessageCPUStartClient(IBlockSource loc) {
        super(loc, "start");
    }

    /**
     * @deprecated - Don't use it, it's for Netty.
     */
    @Deprecated
    public MessageCPUStartClient() {
    }
}
