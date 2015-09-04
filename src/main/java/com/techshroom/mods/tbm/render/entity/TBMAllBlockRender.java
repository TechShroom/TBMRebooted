package com.techshroom.mods.tbm.render.entity;

import org.lwjgl.opengl.GL11;

import com.techshroom.mods.tbm.block.TBMBlockContainer;
import com.techshroom.mods.tbm.entity.TBMEntity;

import codechicken.lib.math.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TBMAllBlockRender
        extends GenericRender<TBMEntity<Container, TileEntity, ?>> {

    public TBMAllBlockRender(RenderManager renderManager) {
        super(renderManager);
    }

    private final BlockRendererDispatcher render =
            Minecraft.getMinecraft().getBlockRendererDispatcher();

    @Override
    public void g_doRender(TBMEntity<Container, TileEntity, ?> entity,
            double renderOnScreenX, double renderOnScreenY,
            double renderOnScreenZ, float yaw, float unknown) {
        if (entity.worldObj
                .getBlockState(
                        new BlockPos(entity.posX, entity.posY, entity.posZ))
                .getBlock() != entity.blockBase()) {
            if (entity.blockBase() == null) {
                System.err.println("no block base for " + entity);
            } else {
                GL11.glPushMatrix();
                GL11.glTranslatef((float) renderOnScreenX,
                        (float) renderOnScreenY, (float) renderOnScreenZ);
                this.bindEntityTexture(entity);
                GL11.glDisable(GL11.GL_LIGHTING);

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

    private void preRenderBlocks(WorldRenderer worldRendererIn, BlockPos pos) {
        worldRendererIn.startDrawing(7);
        worldRendererIn.setVertexFormat(DefaultVertexFormats.BLOCK);
        worldRendererIn.setTranslation((double) (-pos.getX()),
                (double) (-pos.getY()), (double) (-pos.getZ()));
    }

    private void postRenderBlocks(WorldRenderer worldRendererIn) {
        worldRendererIn.finishDrawing();
    }

    private void renderBlock(Block b, World w, int x, int y, int z, int meta) {
        BlockPos pos = new BlockPos(x, y, z);
        if (!(b instanceof TBMBlockContainer)) {
            throw new IllegalStateException(
                    "Trying to render an entity with a non-tbm block! block="
                            + b + ", location=" + pos);
        }
        IBlockState state = b.getDefaultState();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        preRenderBlocks(worldRenderer, pos);
        this.render.renderBlock(state, pos, w, worldRenderer);
        postRenderBlocks(worldRenderer);
        tessellator.draw();
    }

    @Override
    public ResourceLocation
            g_getEntityTexture(TBMEntity<Container, TileEntity, ?> entity) {
        return entity.getEntityTexture();
    }

}
