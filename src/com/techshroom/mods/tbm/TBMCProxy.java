package com.techshroom.mods.tbm;

import com.techshroom.mods.tbm.entity.TBMEntity;
import com.techshroom.mods.tbm.render.entity.TBMAllBlockRender;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class TBMCProxy extends TBMProxy {
    @Override
    protected void subpreinit() {
        RenderingRegistry.registerEntityRenderingHandler(TBMEntity.class,
                new TBMAllBlockRender());
    }

    @Override
    protected void subinit() {
    }

    @Override
    protected void subpostinit() {
    }

    @Override
    public boolean isClient() {
        return true;
    }
}
