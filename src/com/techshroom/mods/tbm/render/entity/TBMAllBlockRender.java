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

public class TBMAllBlockRender extends
        GenericRender<TBMEntity<Container, TileEntity>> {
    private RenderBlocks rb = new RenderBlocks();

    @Override
    public void g_doRender(TBMEntity<Container, TileEntity> entity, double x,
            double y, double z, float yaw, float unknown) {
        // System.err.println("render " + entity);
        rb.blockAccess = entity.worldObj;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        this.bindEntityTexture(entity);
        GL11.glDisable(GL11.GL_LIGHTING);

        // TODO meta might not be zero later
        renderTBMBlockInEntityLocation(entity.worldObj, entity.blockBase(), x,
                y, z, 0);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    @Override
    public ResourceLocation g_getEntityTexture(
            TBMEntity<Container, TileEntity> entity) {
        return entity.getEntityTexture();
    }

    /*
     * RenderBlocks.renderBlockSandFalling but saved here
     */
    private void renderTBMBlockInEntityLocation(World w, Block b, double x,
            double y, double z, int meta) {
        rb.setRenderBoundsFromBlock(b);
        x += 0.5D;
        y += 0.5D;
        z += 0.5D;
        int fx = MathHelper.floor_double(x);
        int fy = MathHelper.floor_double(y);
        int fz = MathHelper.floor_double(z);
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(b.getMixedBrightnessForBlock(w, fx, fy, fz));
        tessellator.setColorOpaque_F(f, f, f);
        rb.renderFaceYNeg(b, -0.5D, -0.5D, -0.5D,
                rb.getBlockIconFromSideAndMetadata(b, 0, meta));
        tessellator.setColorOpaque_F(f1, f1, f1);
        rb.renderFaceYPos(b, -0.5D, -0.5D, -0.5D,
                rb.getBlockIconFromSideAndMetadata(b, 1, meta));
        tessellator.setColorOpaque_F(f2, f2, f2);
        rb.renderFaceZNeg(b, -0.5D, -0.5D, -0.5D,
                rb.getBlockIconFromSideAndMetadata(b, 2, meta));
        tessellator.setColorOpaque_F(f2, f2, f2);
        rb.renderFaceZPos(b, -0.5D, -0.5D, -0.5D,
                rb.getBlockIconFromSideAndMetadata(b, 3, meta));
        tessellator.setColorOpaque_F(f3, f3, f3);
        rb.renderFaceXNeg(b, -0.5D, -0.5D, -0.5D,
                rb.getBlockIconFromSideAndMetadata(b, 4, meta));
        tessellator.setColorOpaque_F(f3, f3, f3);
        rb.renderFaceXPos(b, -0.5D, -0.5D, -0.5D,
                rb.getBlockIconFromSideAndMetadata(b, 5, meta));
        tessellator.draw();
    }
}
