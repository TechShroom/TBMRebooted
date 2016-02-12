package com.techshroom.mods.tbm.machine;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import com.google.common.collect.FluentIterable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface TBMMachine {

    String getId();

    String getName();

    Set<Block> getBasicAllowedBlocks();

    /**
     * N.B.: This must allow all blocks in {@link #getBasicAllowedBlocks()}.
     */
    boolean isBlockAllowed(IBlockState state);

    void trackEntity(UUID e);

    void untrackEntity(UUID e);

    Set<UUID> getTrackedEntities();

    default Set<Entity> resolveEntities(World world) {
        return FluentIterable.from(getTrackedEntities()).transform(uuid -> {
            return world.getLoadedEntityList().stream()
                    .filter(e -> uuid.equals(e.getUniqueID())).findFirst()
                    .orElse(null);
        }).filter(Objects::nonNull).toSet();
    }

}
