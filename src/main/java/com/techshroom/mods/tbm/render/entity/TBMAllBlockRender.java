package com.techshroom.mods.tbm.render.entity;

import static org.lwjgl.opengl.GL11.GL_QUADS;

import com.flowpowered.math.GenericMath;
import com.techshroom.mods.tbm.block.TBMBlockBase;
import com.techshroom.mods.tbm.entity.TBMEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TBMAllBlockRender extends Render<TBMEntity> {

    public TBMAllBlockRender(RenderManager renderManager) {
        super(renderManager);
    }

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
            this.bindTexture(TextureMap.locationBlocksTexture);
            int x = GenericMath.floor(entity.posX);
            int y = GenericMath.floor(entity.posY);
            int z = GenericMath.floor(entity.posZ);

            renderBlock(entity.getState(), entity.worldObj, x, y, z,
                    renderOnScreenX, renderOnScreenY, renderOnScreenZ);

        }
    }

    private void renderBlock(IBlockState state, World w, int x, int y, int z,
            double dox, double doy, double doz) {
        BlockPos pos = new BlockPos(x, y, z);
        if (!(state.getBlock() instanceof TBMBlockBase)) {
            throw new IllegalStateException(
                    "Trying to render an entity with a non-tbm block! block="
                            + state + ", location=" + pos);
        }
        if (state.getBlock().getRenderType() != -1) {
            if (state.getBlock().getRenderType() == 3) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(dox, doy, doz);
                GlStateManager.disableLighting();
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRender = tessellator.getWorldRenderer();
                worldRender.begin(GL_QUADS, DefaultVertexFormats.BLOCK);
                worldRender.setTranslation(-x - 0.5d, -y, -z - 0.5d);
                BlockRendererDispatcher bird =
                        Minecraft.getMinecraft().getBlockRendererDispatcher();
                IBakedModel model =
                        bird.getModelFromBlockState(state, w, (BlockPos) null);
                bird.getBlockModelRenderer().renderModel(w, model, state, pos,
                        worldRender, false);
                worldRender.setTranslation(0.0D, 0.0D, 0.0D);
                tessellator.draw();
                GlStateManager.enableLighting();
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public ResourceLocation getEntityTexture(TBMEntity entity) {
        return entity.getEntityTexture();
    }

}
