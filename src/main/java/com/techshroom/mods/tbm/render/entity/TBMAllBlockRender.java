package com.techshroom.mods.tbm.render.entity;

import static org.lwjgl.opengl.GL11.GL_QUADS;

import org.lwjgl.opengl.GL11;

import com.flowpowered.math.GenericMath;
import com.techshroom.mods.tbm.block.TBMBlockBase;
import com.techshroom.mods.tbm.entity.TBMEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TBMAllBlockRender extends Render<TBMEntity> {

    public TBMAllBlockRender(RenderManager renderManager) {
        super(renderManager);
    }

    private final BlockRendererDispatcher render =
            Minecraft.getMinecraft().getBlockRendererDispatcher();

    @Override
    public void doRender(TBMEntity entity, double renderOnScreenX,
            double renderOnScreenY, double renderOnScreenZ, float yaw,
            float unknown) {
        if (!entity.getMoving().hasMotion()) {
            // if the entity is not moving, don't render it.
            // the placed block will handle it
            return;
        }
        if (entity.getState() == null) {
            System.err.println("no block state for " + entity);
        } else {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) renderOnScreenX, (float) renderOnScreenY,
                    (float) renderOnScreenZ);
            this.bindEntityTexture(entity);
            GL11.glDisable(GL11.GL_LIGHTING);
            int x = GenericMath.floor(entity.posX);
            int y = GenericMath.floor(entity.posY);
            int z = GenericMath.floor(entity.posZ);

            renderBlock(entity.getState(), entity.worldObj, x, y, z,
                    entity.posX, entity.posY, entity.posZ);

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    private void preRenderBlocks(WorldRenderer worldRendererIn, double x,
            double y, double z) {
        worldRendererIn.begin(GL_QUADS, DefaultVertexFormats.BLOCK);
        worldRendererIn.setTranslation(-x, -y, -z);
    }

    private void postRenderBlocks(WorldRenderer worldRenderer) {
        worldRenderer.setTranslation(0, 0, 0);
        worldRenderer.reset();
    }

    private void renderBlock(IBlockState state, World w, int x, int y, int z,
            double dox, double doy, double doz) {
        BlockPos pos = new BlockPos(x, y, z);
        if (!(state.getBlock() instanceof TBMBlockBase)) {
            throw new IllegalStateException(
                    "Trying to render an entity with a non-tbm block! block="
                            + state + ", location=" + pos);
        }
        Tessellator t = Tessellator.getInstance();
        WorldRenderer worldRenderer = t.getWorldRenderer();
        preRenderBlocks(worldRenderer, dox, doy, doz);
        this.render.renderBlock(state, pos, w, worldRenderer);
        t.draw();
        postRenderBlocks(worldRenderer);
    }

    @Override
    public ResourceLocation getEntityTexture(TBMEntity entity) {
        return entity.getEntityTexture();
    }

}
