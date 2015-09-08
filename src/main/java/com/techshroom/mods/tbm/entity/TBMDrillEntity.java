package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.block.tile.TBMDrillTile;
import com.techshroom.mods.tbm.gui.container.ContainerTBMDrill;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class TBMDrillEntity
        extends TBMEntity<ContainerTBMDrill, TBMDrillTile, TBMDrillEntity> {

    public TBMDrillEntity(World w) {
        super(w);
    }

    @Override
    public Block blockBase() {
        return store.get(TBMKeys.Blocks.DRILL).get();
    }
}
