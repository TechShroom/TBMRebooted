package com.techshroom.mods.tbm.render.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public abstract class TileSpecialRenderer<T extends TileEntity> extends
        TileEntitySpecialRenderer {
    @SuppressWarnings("unchecked")
    @Override
    public final void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_,
            double p_147500_4_, double p_147500_6_, float p_147500_8_) {
        g_renderTileEntityAt((T) p_147500_1_, p_147500_2_, p_147500_4_,
                p_147500_6_, p_147500_8_);
    }

    public abstract void g_renderTileEntityAt(T tile,
            double x, double y, double z,
            float partialTicks);
}
