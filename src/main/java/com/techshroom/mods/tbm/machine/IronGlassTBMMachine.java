package com.techshroom.mods.tbm.machine;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class IronGlassTBMMachine extends TBMMachineBase {

    private static final Set<Material> VALID_MATERIALS =
            ImmutableSet.of(Material.glass, Material.iron);

    public IronGlassTBMMachine(String id, String name) {
        super(id, name);
    }

    @Override
    public boolean isBlockAllowed(IBlockState state) {
        return super.isBlockAllowed(state)
                || VALID_MATERIALS.contains(state.getBlock().getMaterial());
    }

}
