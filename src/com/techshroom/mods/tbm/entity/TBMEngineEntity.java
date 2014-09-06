package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMEngineTile;
import com.techshroom.mods.tbm.gui.container.ContainerTBMEngine;

public class TBMEngineEntity
        extends TBMEntity<ContainerTBMEngine, TBMEngineTile, TBMEngineEntity> {
    public TBMEngineEntity(World w) {
        super(w);
    }

    @Override
    public Block blockBase() {
        return store_get("engine");
    }
}
