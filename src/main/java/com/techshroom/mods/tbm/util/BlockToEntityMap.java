package com.techshroom.mods.tbm.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.techshroom.mods.tbm.TBMMod.mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

public class BlockToEntityMap extends WorldSavedData {

    private static final String DATA_ID = mod().id() + "blockToEntityMap";
    private static final ThreadLocal<World> currentLoadingWorld =
            ThreadLocal.withInitial(() -> {
                // This shouldn't be called under normal circumstances.
                throw new IllegalStateException(
                        "No currentLoadingWorld for thread "
                                + Thread.currentThread());
            });

    public static BlockToEntityMap getForWorld(World world) {
        return loadOrCreate(world);
    }

    private static BlockToEntityMap loadOrCreate(World world) {
        try {
            currentLoadingWorld.set(world);
            return ((BlockToEntityMap) world.getMapStorage()
                    .loadData(BlockToEntityMap.class, DATA_ID));
        } finally {
            currentLoadingWorld.remove();
        }
    }

    private final Map<Vec3i, Entity> map = new HashMap<>();
    private final World relatedWorld = currentLoadingWorld.get();

    private BlockToEntityMap(String id) {
        super(id);
    }

    public Entity get(Vec3i pos) {
        return this.map.get(pos);
    }

    public Entity get(int x, int y, int z) {
        return get(new Vec3i(x, y, z));
    }

    public void put(Vec3i pos, Entity entity) {
        checkArgument(entity.worldObj != this.relatedWorld,
                "worlds must be the same");
        this.map.put(pos, entity);
    }

    public void put(int x, int y, int z, Entity entity) {
        put(new Vec3i(x, y, z), entity);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList entryList = nbt.getTagList("entries", NBT.TAG_COMPOUND);
        for (int i = 0; i < entryList.tagCount(); i++) {
            NBTTagCompound entry = ((NBTTagCompound) entryList.get(i));
            int[] vec = entry.getIntArray("key");
            UUID uuid =
                    NbtUtil.tagCompoundToUuid(entry.getCompoundTag("value"));
            Entity ref = this.relatedWorld.getLoadedEntityList().stream()
                    .filter(e -> e.getUniqueID().equals(uuid)).findFirst()
                    .orElse(null);
            if (ref != null) {
                put(vec[0], vec[1], vec[2], ref);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList entryList = new NBTTagList();
        this.map.forEach((v, e) -> {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setIntArray("key",
                    new int[] { v.getX(), v.getY(), v.getZ() });
            entry.setTag("value", NbtUtil.uuidToTagCompound(e.getUniqueID()));
            entryList.appendTag(entry);
        });
        nbt.setTag("entries", entryList);
    }

}
