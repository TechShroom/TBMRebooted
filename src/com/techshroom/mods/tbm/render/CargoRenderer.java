package com.techshroom.mods.tbm.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.techshroom.mods.tbm.block.tile.TBMCargoTile;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public final class CargoRenderer implements ISimpleBlockRenderingHandler {
    public static int id;
    private final TileEntity theTile = new TBMCargoTile();

    public CargoRenderer(int id) {
        CargoRenderer.id = id;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId,
            RenderBlocks renderer) {
        TileEntityRendererDispatcher.instance.renderTileEntityAt(theTile, 0,
                0, 0, 0);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
            Block block, int modelId, RenderBlocks renderer) {
        return modelId == id;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return id;
    }

}
