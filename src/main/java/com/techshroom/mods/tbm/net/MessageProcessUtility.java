package com.techshroom.mods.tbm.net;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class MessageProcessUtility {

    @SideOnly(Side.CLIENT)
    private static final IThreadListener CLIENT = Minecraft.getMinecraft();
    private static final IThreadListener SERVER = MinecraftServer.getServer();

    public static void processOnMCThread(Runnable r, Side side) {
        IThreadListener l = getThreader(side);
        if (!l.isCallingFromMinecraftThread()) {
            l.addScheduledTask(r);
        } else {
            r.run();
        }
    }

    private static IThreadListener getThreader(Side side) {
        if (side == Side.CLIENT) {
            return CLIENT;
        }
        return SERVER;
    }

    private MessageProcessUtility() {
        throw new AssertionError();
    }

}
