package com.techshroom.mods.tbm.render.entity;

import org.lwjgl.opengl.GL11;

import com.techshroom.mods.tbm.block.TBMBlockBase;
import com.techshroom.mods.tbm.entity.TBMEntity;

import codechicken.lib.math.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TBMAllBlockRender extends GenericRender<TBMEntity> {

    public TBMAllBlockRender(RenderManager renderManager) {
        super(renderManager);
    }

    private final RegionRenderCacheBuilder rrc = new RegionRenderCacheBuilder();
    private final BlockRendererDispatcher render =
            Minecraft.getMinecraft().getBlockRendererDispatcher();

    @Override
    public void g_doRender(TBMEntity entity, double renderOnScreenX,
            double renderOnScreenY, double renderOnScreenZ, float yaw,
            float unknown) {
        if (entity.blockBase() == null) {
            System.err.println("no block base for " + entity);
        } else {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) renderOnScreenX, (float) renderOnScreenY,
                    (float) renderOnScreenZ);
            this.bindEntityTexture(entity);
            GL11.glDisable(GL11.GL_LIGHTING);
            int x = MathHelper.floor_double(entity.posX);
            int y = MathHelper.floor_double(entity.posY);
            int z = MathHelper.floor_double(entity.posZ);

            // TODO meta might not be zero later
            renderBlock(entity.blockBase(), entity.worldObj, x, y, z, 0,
                    entity.posX, entity.posY, entity.posZ);

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    private void preRenderBlocks(WorldRenderer worldRendererIn, double x,
            double y, double z) {
        worldRendererIn.startDrawing(7);
        worldRendererIn.setVertexFormat(DefaultVertexFormats.BLOCK);
        worldRendererIn.setTranslation(-x, -y, -z);
    }

    private void postRenderBlocks(WorldRenderer worldRendererIn) {
        worldRendererIn.finishDrawing();
    }

    private void renderBlock(Block b, World w, int x, int y, int z, int meta,
            double dox, double doy, double doz) {
        BlockPos pos = new BlockPos(x, y, z);
        if (!(b instanceof TBMBlockBase)) {
            throw new IllegalStateException(
                    "Trying to render an entity with a non-tbm block! block="
                            + b + ", location=" + pos);
        }
        IBlockState state = b.getDefaultState();
        Tessellator t = Tessellator.getInstance();
        WorldRenderer worldRenderer = t.getWorldRenderer();
        preRenderBlocks(worldRenderer, dox, doy, doz);
        this.render.renderBlock(state, pos, w, worldRenderer);
        t.draw();// postRenderBlocks(worldRenderer);
        worldRenderer.setTranslation(0, 0, 0);
        worldRenderer.reset();
    }

    @Override
    public ResourceLocation g_getEntityTexture(TBMEntity entity) {
        return entity.getEntityTexture();
    }

}
