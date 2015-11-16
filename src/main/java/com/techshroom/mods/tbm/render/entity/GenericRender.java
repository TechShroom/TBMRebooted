package com.techshroom.mods.tbm.render.entity;

import com.techshroom.mods.tbm.Tutils;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public abstract class GenericRender<T extends Entity> extends Render {

    protected GenericRender(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public final void doRender(Entity p_76986_1_, double p_76986_2_,
            double p_76986_4_, double p_76986_6_, float p_76986_8_,
            float p_76986_9_) {
        g_doRender(Tutils.<T> cast(p_76986_1_), p_76986_2_, p_76986_4_,
                p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Render entity generic
     * 
     * @param entity
     *            - entity
     * @param xOnScreen
     *            - x on screen
     * @param yOnScreen
     *            - y on screen
     * @param zOnScreen
     *            - z on screen
     * @param yaw
     *            - probably the yaw, not sure
     * @param partialTicks
     *            - partialTicks...? are something
     */
    public abstract void g_doRender(T entity, double xOnScreen,
            double yOnScreen, double zOnScreen, float yaw, float partialTicks);

    @Override
    protected final ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return g_getEntityTexture(Tutils.<T> cast(p_110775_1_));
    }

    /**
     * Get entity texture generic
     * 
     * @param entity
     *            - the entity
     */
    public abstract ResourceLocation g_getEntityTexture(T entity);
}
