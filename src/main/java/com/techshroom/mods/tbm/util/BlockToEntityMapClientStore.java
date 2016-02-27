package com.techshroom.mods.tbm.util;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public final class BlockToEntityMapClientStore {

    private static BlockToEntityMap current;

    public static BlockToEntityMap getCurrent() {
        return current;
    }

    public static void onPacketUpdate(NBTTagCompound packetData) {
        try (
                NotExceptionalClosable close = BlockToEntityMap
                        .withCurrentWorld(Minecraft.getMinecraft().theWorld)) {
            current = new BlockToEntityMap(BlockToEntityMap.DATA_ID);
        }
        current.readFromNBTPacket(packetData);
    }

    private BlockToEntityMapClientStore() {
    }

}
