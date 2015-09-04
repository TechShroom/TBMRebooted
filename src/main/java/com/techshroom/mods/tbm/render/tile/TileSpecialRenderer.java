package com.techshroom.mods.tbm.render.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public abstract class TileSpecialRenderer<T extends TileEntity>
        extends TileEntitySpecialRenderer {

    @SuppressWarnings("unchecked")
    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z,
            float p_180535_8_, int p_180535_9_) {
        g_renderTileEntityAt((T) te, x, y, z, p_180535_8_);

    }

    public abstract void g_renderTileEntityAt(T tile, double x, double y,
            double z, float partialTicks);
}
