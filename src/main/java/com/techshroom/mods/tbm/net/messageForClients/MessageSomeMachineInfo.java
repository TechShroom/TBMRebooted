package com.techshroom.mods.tbm.net.messageForClients;

import static com.techshroom.mods.tbm.TBMMod.worldFetcher;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class MessageSomeMachineInfo implements IMessage {

    private transient World cacheWorld;
    private int entId;
    private int worldDim;

    public int getEntityId() {
        return this.entId;
    }

    public Entity getTargetEntity() {
        return getTargetWorld().getEntityByID(getEntityId());
    }

    public World getTargetWorld() {
        return updateCache();
    }

    private World updateCache() {
        if (this.cacheWorld == null) {
            this.cacheWorld = worldFetcher().fetch(this.worldDim);
        }
        return this.cacheWorld;
    }
}
