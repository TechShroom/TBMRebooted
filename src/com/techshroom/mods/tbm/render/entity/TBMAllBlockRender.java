package com.techshroom.mods.tbm.render.entity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import codechicken.lib.math.MathHelper;

import com.techshroom.mods.tbm.entity.TBMEntity;

public class TBMAllBlockRender
        extends GenericRender<TBMEntity<Container, TileEntity, ?>> {
    private RenderBlocks rb = new RenderBlocks();

    @Override
    public void g_doRender(TBMEntity<Container, TileEntity, ?> entity,
            double renderOnScreenX, double renderOnScreenY,
            double renderOnScreenZ, float yaw, float unknown) {
        if (entity.worldObj.getBlock(MathHelper.floor_double(entity.posX),
                MathHelper.floor_double(entity.posY),
                MathHelper.floor_double(entity.posZ)) != entity.blockBase()) {
            if (entity.blockBase() == null) {
                System.err.println("no block base for " + entity);
            } else {
                rb.blockAccess = entity.worldObj;
                GL11.glPushMatrix();
                GL11.glTranslatef((float) renderOnScreenX,
                        (float) renderOnScreenY, (float) renderOnScreenZ);
                this.bindEntityTexture(entity);
                GL11.glDisable(GL11.GL_LIGHTING);

                rb.setRenderBoundsFromBlock(entity.blockBase());
                // TODO meta might not be zero later
                renderBlock(entity.blockBase(), entity.worldObj,
                        MathHelper.floor_double(entity.posX),
                        MathHelper.floor_double(entity.posY),
                        MathHelper.floor_double(entity.posZ), 0);

                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glPopMatrix();
            }
        }
    }

    private void renderBlock(Block b, World w, int x, int y, int z, int meta) {
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(b.getMixedBrightnessForBlock(w, x, y, z));
        tessellator.setColorOpaque_F(f, f, f);
        rb.renderFaceYNeg(b, -0.5D, 0, -0.5D,
                rb.getBlockIconFromSideAndMetadata(b, 0, meta));
        tessellator.setColorOpaque_F(f1, f1, f1);
        rb.renderFaceYPos(b, -0.5D, 0, -0.5D,
                rb.getBlockIconFromSideAndMetadata(b, 1, meta));
        tessellator.setColorOpaque_F(f2, f2, f2);
        rb.renderFaceZNeg(b, -0.5D, 0, -0.5D,
                rb.getBlockIconFromSideAndMetadata(b, 2, meta));
        tessellator.setColorOpaque_F(f2, f2, f2);
        rb.renderFaceZPos(b, -0.5D, 0, -0.5D,
                rb.getBlockIconFromSideAndMetadata(b, 3, meta));
        tessellator.setColorOpaque_F(f3, f3, f3);
        rb.renderFaceXNeg(b, -0.5D, 0, -0.5D,
                rb.getBlockIconFromSideAndMetadata(b, 4, meta));
        tessellator.setColorOpaque_F(f3, f3, f3);
        rb.renderFaceXPos(b, -0.5D, 0, -0.5D,
                rb.getBlockIconFromSideAndMetadata(b, 5, meta));
        tessellator.draw();
    }

    @Override
    public ResourceLocation g_getEntityTexture(
            TBMEntity<Container, TileEntity, ?> entity) {
        return entity.getEntityTexture();
    }
}
