package com.techshroom.mods.tbm.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.Tutils.isClient;

import java.util.ArrayList;
import java.util.UUID;

import com.google.common.base.Predicates;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.techshroom.mods.tbm.net.messageForClients.BlockToEntityMapTranferForClient;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.util.Constants.NBT;

public class BlockToEntityMap extends WorldSavedData {

    public static final String DATA_ID = mod().id() + "blockToEntityMap";
    private static final ThreadLocal<World> currentLoadingWorld =
            ThreadLocal.withInitial(() -> {
                // This shouldn't be called under normal circumstances.
                throw new IllegalStateException(
                        "No currentLoadingWorld for thread "
                                + Thread.currentThread());
            });

    public static NotExceptionalClosable withCurrentWorld(World world) {
        currentLoadingWorld.set(world);
        return currentLoadingWorld::remove;
    }

    public static BlockToEntityMap getForWorld(World world) {
        return loadOrCreate(world);
    }

    @CalledOnServerOnly
    private static BlockToEntityMap loadOrCreate(World world) {
        try (
                NotExceptionalClosable close = withCurrentWorld(world)) {
            if (isClient(world)) {
                return BlockToEntityMapClientStore.getCurrent();
            }
            BlockToEntityMap map = ((BlockToEntityMap) world.getMapStorage()
                    .loadData(BlockToEntityMap.class, DATA_ID));
            if (map == null) {
                map = new BlockToEntityMap(DATA_ID);
                world.getPerWorldStorage().setData(DATA_ID, map);
            }
            return map;
        }
    }

    private final BiMap<Vec3i, Entity> map = HashBiMap.create();
    private final TObjectIntMap<Vec3i> tryAgain = new TObjectIntHashMap<>();
    private final World relatedWorld = currentLoadingWorld.get();

    public BlockToEntityMap(String id) {
        super(id);
        checkNotNull(this.relatedWorld, "no loaded world, what!");
    }

    public Vec3i getReverse(Entity ent) {
        tryAgain();
        return this.map.inverse().get(ent);
    }

    public Entity get(Vec3i pos) {
        tryAgain();
        System.err.println("acc " + pos + "=" + this.map.get(pos));
        return this.map.get(pos);
    }

    private void tryAgain() {
        this.tryAgain.forEachEntry((k, v) -> {
            Entity ent = this.relatedWorld.getEntityByID(v);
            if (ent != null) {
                this.map.put(k, ent);
            }
            return true;
        });
    }

    public Entity get(int x, int y, int z) {
        return get(new Vec3i(x, y, z));
    }

    public void put(Vec3i pos, Entity entity) {
        checkArgument(entity.worldObj == this.relatedWorld,
                "worlds must be the same");
        System.err.println("asso " + pos + "=" + entity);
        this.map.put(pos, entity);
        triggerSave();
    }

    public void put(int x, int y, int z, Entity entity) {
        put(new Vec3i(x, y, z), entity);
    }

    public Entity remove(Vec3i pos) {
        Entity removed = this.map.remove(pos);
        System.err.println("de-asso " + pos + "=" + removed);
        triggerSave();
        return removed;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList entryList = nbt.getTagList("entries", NBT.TAG_COMPOUND);
        for (int i = 0; i < entryList.tagCount(); i++) {
            NBTTagCompound entry = ((NBTTagCompound) entryList.get(i));
            int[] vec = entry.getIntArray("key");
            UUID uuid =
                    NbtUtil.tagCompoundToUuid(entry.getCompoundTag("value"));
            Entity ref = new ArrayList<>(this.relatedWorld.loadedEntityList)
                    .stream().filter(e -> e.getUniqueID().equals(uuid))
                    .findFirst().orElse(null);
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

    public void readFromNBTPacket(NBTTagCompound nbt) {
        NBTTagList entryList = nbt.getTagList("entries", NBT.TAG_COMPOUND);
        for (int i = 0; i < entryList.tagCount(); i++) {
            NBTTagCompound entry = ((NBTTagCompound) entryList.get(i));
            int[] vec = entry.getIntArray("key");
            int id = entry.getInteger("value");
            Entity ref = this.relatedWorld.getEntityByID(id);
            if (ref != null) {
                put(vec[0], vec[1], vec[2], ref);
            } else {
                this.tryAgain.put(new Vec3i(vec[0], vec[1], vec[2]), id);
            }
        }
    }

    public NBTTagCompound writeToNBTPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList entryList = new NBTTagList();
        this.map.forEach((v, e) -> {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setIntArray("key",
                    new int[] { v.getX(), v.getY(), v.getZ() });
            entry.setInteger("value", e.getEntityId());
            entryList.appendTag(entry);
        });
        nbt.setTag("entries", entryList);
        return nbt;
    }

    private void triggerSave() {
        this.markDirty();
        MapStorage perWorldStorage = this.relatedWorld.getPerWorldStorage();
        perWorldStorage.setData(DATA_ID, this);
        perWorldStorage.saveAllData();
        this.relatedWorld
                .getPlayers(EntityPlayerMP.class,
                        Predicates
                                .alwaysTrue())
                .forEach(p -> mod().netmanager().netWrapper()
                        .sendTo(new BlockToEntityMapTranferForClient(
                                writeToNBTPacket()), p));
    }

}
