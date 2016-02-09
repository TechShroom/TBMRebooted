package com.techshroom.mods.tbm.util;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

public final class NbtUtil {

    public static NBTTagCompound uuidToTagCompound(UUID uuid) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong("most", uuid.getMostSignificantBits());
        tag.setLong("least", uuid.getLeastSignificantBits());
        return tag;
    }

    public static UUID tagCompoundToUuid(NBTTagCompound tag) {
        return new UUID(tag.getLong("most"), tag.getLong("least"));
    }

    private NbtUtil() {
    }

}
