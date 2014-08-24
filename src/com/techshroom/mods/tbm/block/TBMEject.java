package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.*;
import static com.techshroom.mods.tbm.Tutils.address;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class TBMEject extends Block {
    public TBMEject() {
        super(Material.iron);
        this.setBlockName("ejecter")
                .setBlockTextureName(
                        address(mod().id(), "eject-tex").toString())
                .setCreativeTab((CreativeTabs) store_get("blockTab"));
    }

}
