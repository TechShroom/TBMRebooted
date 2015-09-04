package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.block.tile.TBMCargoTile;
import com.techshroom.mods.tbm.gui.container.ContainerTBMCargo;

public class TBMCargoEntity
        extends TBMEntity<ContainerTBMCargo, TBMCargoTile, TBMCargoEntity> {
    public TBMCargoEntity(World w) {
        super(w);
    }

    @Override
    public Block blockBase() {
        return store_get("cargo");
    }
}
