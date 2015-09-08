package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.block.tile.TBMEngineTile;
import com.techshroom.mods.tbm.gui.container.ContainerTBMEngine;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class TBMEngineEntity
        extends TBMEntity<ContainerTBMEngine, TBMEngineTile, TBMEngineEntity> {

    public TBMEngineEntity(World w) {
        super(w);
    }

    @Override
    public Block blockBase() {
        return store.get(TBMKeys.Blocks.ENGINE).get();
    }
}
