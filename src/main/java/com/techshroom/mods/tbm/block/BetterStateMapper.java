package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.Tutils.addressMod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class BetterStateMapper extends StateMapperBase {

    private final String mrlId;

    public BetterStateMapper(String mrlId) {
        this.mrlId = mrlId;
    }

    @Override
    protected ModelResourceLocation
            getModelResourceLocation(IBlockState p_178132_1_) {
        return new ModelResourceLocation(addressMod(this.mrlId),
                this.getPropertyString(p_178132_1_.getProperties()));
    }

}
