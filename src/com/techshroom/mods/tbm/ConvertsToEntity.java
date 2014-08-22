package com.techshroom.mods.tbm;

import net.minecraft.entity.Entity;

public interface ConvertsToEntity<EntityType extends Entity> {
    EntityType convertToEntity();
}
