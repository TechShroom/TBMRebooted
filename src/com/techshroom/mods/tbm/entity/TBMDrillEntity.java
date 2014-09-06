package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMDrillTile;
import com.techshroom.mods.tbm.gui.container.ContainerTBMDrill;

public class TBMDrillEntity
        extends TBMEntity<ContainerTBMDrill, TBMDrillTile, TBMDrillEntity> {
    public TBMDrillEntity(World w) {
        super(w);
    }

    @Override
    public Block blockBase() {
        return store_get("drill");
    }
}
