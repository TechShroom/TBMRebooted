package com.techshroom.mods.tbm.machine;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class IronGlassTBMMachine extends TBMMachineBase {

    private static final Set<Material> VALID_MATERIALS =
            ImmutableSet.of(Material.glass, Material.iron);

    public IronGlassTBMMachine(String name) {
        super(name);
    }

    @Override
    public boolean isBlockAllowed(Block block, IBlockState state) {
        return super.isBlockAllowed(block, state)
                || VALID_MATERIALS.contains(block.getMaterial());
    }

}
