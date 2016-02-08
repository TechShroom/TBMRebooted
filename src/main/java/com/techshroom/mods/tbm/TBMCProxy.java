package com.techshroom.mods.tbm;

import static com.techshroom.mods.tbm.TBMMod.store;
import static com.techshroom.mods.tbm.Tutils.addressMod;

import com.techshroom.mods.tbm.entity.TBMEntity;
import com.techshroom.mods.tbm.render.entity.TBMAllBlockRender;
import com.techshroom.mods.tbm.util.Storage.Key;

import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class TBMCProxy extends TBMProxy {

    @Override
    protected void subpreinit() {
        for (Key<Block> key : TBMKeys.Blocks.ALL) {
            Block block = store.get(key).get();
            // N.B. substring 5 in the code below removes item.
            ModelLoader.setCustomModelResourceLocation(
                    Item.getItemFromBlock(block), 0,
                    new ModelResourceLocation(
                            addressMod(block.getUnlocalizedName()),
                            "inventory"));
        }
        RenderingRegistry.registerEntityRenderingHandler(TBMEntity.class,
                TBMAllBlockRender::new);
    }

    @Override
    protected void subinit() {
    }

    @Override
    protected void subpostinit() {
    }

    @Override
    public boolean isClient() {
        return true;
    }
}
