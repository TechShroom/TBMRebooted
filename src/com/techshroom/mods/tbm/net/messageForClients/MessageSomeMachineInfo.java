package com.techshroom.mods.tbm.net.messageForClients;

import static com.techshroom.mods.tbm.TBMMod.worldFetcher;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public abstract class MessageSomeMachineInfo implements IMessage {
    private transient World cacheWorld;
    private int entId;
    private int worldDim;

    public int getEntityId() {
        return entId;
    }

    public Entity getTargetEntity() {
        return getTargetWorld().getEntityByID(getEntityId());
    }

    public World getTargetWorld() {
        return updateCache();
    }

    private World updateCache() {
        if (cacheWorld == null) {
            cacheWorld = worldFetcher().fetch(worldDim);
        }
        return cacheWorld;
    }
}
