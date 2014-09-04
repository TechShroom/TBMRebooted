package com.techshroom.mods.tbm.render.entity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import codechicken.lib.math.MathHelper;

import com.techshroom.mods.tbm.entity.TBMEntity;

public class TBMAllBlockRender
        extends GenericRender<TBMEntity<Container, TileEntity, ?>> {
    private RenderBlocks rb = new RenderBlocks();

    @Override
    public void g_doRender(TBMEntity<Container, TileEntity, ?> entity,
            double x, double y, double z, float yaw, float unknown) {
        if (entity.worldObj.getBlock(MathHelper.floor_double(entity.posX),
                MathHelper.floor_double(entity.posY),
                MathHelper.floor_double(entity.posZ)) != entity.blockBase()) {
            if (entity.blockBase() == null) {
                System.err.println("no block base for " + entity);
            } else {
                rb.blockAccess = entity.worldObj;
                GL11.glPushMatrix();
                GL11.glTranslatef((float) x, (float) y, (float) z);
                this.bindEntityTexture(entity);
                GL11.glDisable(GL11.GL_LIGHTING);

                rb.setRenderBoundsFromBlock(entity.blockBase());
                // TODO meta might not be zero later
                renderTBMBlockInEntityLocation(entity.worldObj,
                        entity.blockBase(), entity.posX, entity.posY,
                        entity.posZ, 0);

                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glPopMatrix();
            }
        }
    }

    @Override
    public ResourceLocation g_getEntityTexture(
            TBMEntity<Container, TileEntity, ?> entity) {
        return entity.getEntityTexture();
    }

    /*
     * RenderBlocks.renderBlockSandFalling but saved here
     */
    private void renderTBMBlockInEntityLocation(World w, Block b, double x,
            double y, double z, int meta) {
        int fx = MathHelper.floor_double(x);
        int fy = MathHelper.floor_double(y);
        int fz = MathHelper.floor_double(z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        tessellator.setBrightness(b.getMixedBrightnessForBlock(w, fx, fy, fz));
        tessellator.setColorOpaque_F(f, f, f);
        rb.renderFaceYNeg(b, 0, 0, 0,
                rb.getBlockIconFromSideAndMetadata(b, 0, meta));
        tessellator.setColorOpaque_F(f1, f1, f1);
        rb.renderFaceYPos(b, 0, 0, 0,
                rb.getBlockIconFromSideAndMetadata(b, 1, meta));
        tessellator.setColorOpaque_F(f2, f2, f2);
        rb.renderFaceZNeg(b, 0, 0, 0,
                rb.getBlockIconFromSideAndMetadata(b, 2, meta));
        tessellator.setColorOpaque_F(f2, f2, f2);
        rb.renderFaceZPos(b, 0, 0, 0,
                rb.getBlockIconFromSideAndMetadata(b, 3, meta));
        tessellator.setColorOpaque_F(f3, f3, f3);
        rb.renderFaceXNeg(b, 0, 0, 0,
                rb.getBlockIconFromSideAndMetadata(b, 4, meta));
        tessellator.setColorOpaque_F(f3, f3, f3);
        rb.renderFaceXPos(b, 0, 0, 0,
                rb.getBlockIconFromSideAndMetadata(b, 5, meta));
        tessellator.draw();
    }

    public void renderTBMBlock(Block par1Block, World par2World, int par3,
            int par4, int par5, int par6) {
        RenderBlocks tbmRender = rb;
        IIcon par7Icon = par1Block.getBlockTextureFromSide(0);
        // float f = 0.5F;
        // float f1 = 1.0F;
        // float f2 = 0.8F;
        // float f3 = 0.6F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(
                par2World, par3, par4, par5));
        float f4 = 1.0F;
        float f5 = 1.0F;

        if (f5 < f4) {
            f5 = f4;
        }

        // tessellator.setColorOpaque_F(f * f5, f * f5, f * f5);
        par7Icon = par1Block.getBlockTextureFromSide(ForgeDirection.DOWN.flag);
        tbmRender.renderFaceYNeg(par1Block, -0.5d, 0, -0.5d, par7Icon);
        f5 = 1.0F;

        if (f5 < f4) {
            f5 = f4;
        }

        // tessellator.setColorOpaque_F(f1 * f5, f1 * f5, f1 * f5);
        par7Icon = par1Block.getBlockTextureFromSide(ForgeDirection.UP.flag);
        tbmRender.renderFaceYPos(par1Block, -0.5d, 0, -0.5d, par7Icon);
        f5 = 1.0F;

        if (f5 < f4) {
            f5 = f4;
        }

        // tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        par7Icon = par1Block.getBlockTextureFromSide(ForgeDirection.NORTH.flag);
        tbmRender.renderFaceZNeg(par1Block, -0.5d, 0, -0.5d, par7Icon);
        f5 = 1.0F;

        if (f5 < f4) {
            f5 = f4;
        }

        // tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        par7Icon = par1Block.getBlockTextureFromSide(ForgeDirection.SOUTH.flag);
        tbmRender.renderFaceZPos(par1Block, -0.5d, 0, -0.5d, par7Icon);
        f5 = 1.0F;

        if (f5 < f4) {
            f5 = f4;
        }

        // tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        par7Icon = par1Block.getBlockTextureFromSide(ForgeDirection.WEST.flag);
        tbmRender.renderFaceXNeg(par1Block, -0.5d, 0, -0.5d, par7Icon);
        f5 = 1.0F;

        if (f5 < f4) {
            f5 = f4;
        }

        // tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        par7Icon = par1Block.getBlockTextureFromSide(ForgeDirection.EAST.flag);
        tbmRender.renderFaceXPos(par1Block, -0.5d, 0, -0.5d, par7Icon);
        tessellator.draw();
    }
}
