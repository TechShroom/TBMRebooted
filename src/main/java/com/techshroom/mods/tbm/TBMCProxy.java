package com.techshroom.mods.tbm;

import com.techshroom.mods.tbm.entity.TBMEntity;
import com.techshroom.mods.tbm.render.entity.TBMAllBlockRender;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class TBMCProxy extends TBMProxy {

    @Override
    protected void subpreinit() {
    }

    @Override
    protected void subinit() {
        RenderingRegistry.registerEntityRenderingHandler(TBMEntity.class,
                new TBMAllBlockRender(
                        Minecraft.getMinecraft().getRenderManager()));
    }

    @Override
    protected void subpostinit() {
    }

    @Override
    public boolean isClient() {
        return true;
    }
}
