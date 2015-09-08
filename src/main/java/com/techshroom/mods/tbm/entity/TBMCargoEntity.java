package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.block.tile.TBMCargoTile;
import com.techshroom.mods.tbm.gui.container.ContainerTBMCargo;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class TBMCargoEntity
        extends TBMEntity<ContainerTBMCargo, TBMCargoTile, TBMCargoEntity> {
    public TBMCargoEntity(World w) {
        super(w);
    }

    @Override
    public Block blockBase() {
        return store.get(TBMKeys.Blocks.CARGO).get();
    }
}
