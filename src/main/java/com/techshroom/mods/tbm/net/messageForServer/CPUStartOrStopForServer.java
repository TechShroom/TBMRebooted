package com.techshroom.mods.tbm.net.messageForServer;

import static com.techshroom.mods.tbm.TBMMod.mod;

import com.techshroom.mods.tbm.entity.TBMCPUEntity;
import com.techshroom.mods.tbm.net.SimpleMessageHandler;
import com.techshroom.mods.tbm.util.BlockToEntityMap;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class CPUStartOrStopForServer implements IMessage {

    public static class Handler
            implements SimpleMessageHandler.Client<CPUStartOrStopForServer> {

        @Override
        public void processOnMCThread(CPUStartOrStopForServer message,
                MessageContext ctx) {
            World contextWorld = ctx.getServerHandler().playerEntity.worldObj;
            Entity atPos =
                    BlockToEntityMap.getForWorld(contextWorld).get(message.pos);
            if (!(atPos instanceof TBMCPUEntity)) {
                // Cop out on bad packets.
                mod().log.warn("bad packet, claiming CPU at " + atPos);
                return;
            }
            TBMCPUEntity cpu = (TBMCPUEntity) atPos;
            if (cpu.getMoving().hasMotion()) {
                cpu.guiStop();
            } else {
                cpu.guiStart();
            }
        }

    }

    private BlockPos pos;

    public CPUStartOrStopForServer(BlockPos pos) {
        this.pos = pos;
    }

    /**
     * @deprecated - Don't use it, it's for Netty.
     */
    @Deprecated
    public CPUStartOrStopForServer() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

}
