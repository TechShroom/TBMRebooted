package com.techshroom.mods.tbm.render;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.TBMMod.store;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.techshroom.mods.tbm.TBMKeys;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import io.netty.util.internal.EmptyArrays;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * Manages dynamic lookup of colors and mapping them to metadata values.
 */
public final class DynamicColorMetaManager {

    private static final Configuration CONFIGURATION =
            new Configuration(store.get(TBMKeys.CONFIG_DIR).get()
                    .resolve(mod().id() + "ColorConfig.cfg").toFile());
    private static final Property COLOR_PROPERTY;

    static {
        CONFIGURATION.load();
        Property prop = CONFIGURATION.get("render", "colors",
                EmptyArrays.EMPTY_STRINGS);
        prop.setLanguageKey("colors");
        prop.comment = "Extra colors."
                + " Don't edit unless you know what you're doing."
                + " [default: nothing]";
        COLOR_PROPERTY = prop;
        CONFIGURATION.save();
    }

    private static final TIntIntMap colors = new TIntIntHashMap();
    private static final TObjectIntMap<ItemStack> metaForSource =
            new TObjectIntHashMap<>();
    private static int metaCounter = 0;
    private static boolean locked;

    private static void addColor(ItemStack stack, int color) {
        if (metaForSource.containsKey(stack) || locked) {
            return;
        }
        metaForSource.put(stack, metaCounter);
        colors.put(metaCounter, color);
        metaCounter++;
    }

    private static void addConfigColors() {
        String[] configColors = COLOR_PROPERTY.getStringList();
        for (String color : configColors) {
            deserializeColor(color);
        }
    }

    private static void saveConfigColors() {
        String[] configColors = COLOR_PROPERTY.getStringList();
        Set<String> addnColors = new HashSet<>(Arrays.asList(configColors));
        metaForSource.forEachEntry((stack, meta) -> {
            addnColors.add(serializeColor(stack, meta, getColor(meta)));
            return true;
        });
        COLOR_PROPERTY.set(addnColors.stream().toArray(String[]::new));
        CONFIGURATION.save();
    }

    private static String serializeColor(ItemStack stack, int meta, int color) {
        NBTTagCompound rootTag = new NBTTagCompound();
        NBTTagCompound stackTag = new NBTTagCompound();
        stack.writeToNBT(stackTag);
        rootTag.setTag("stack", stackTag);
        rootTag.setInteger("meta", meta);
        rootTag.setInteger("color", color);
        return rootTag.toString();
    }

    private static void deserializeColor(String nbtJson) {
        try {
            NBTTagCompound rootTag = JsonToNBT.getTagFromJson(nbtJson);
            ItemStack stack = ItemStack
                    .loadItemStackFromNBT(rootTag.getCompoundTag("stack"));
            int meta = rootTag.getInteger("meta");
            int color = rootTag.getInteger("color");
            colors.put(meta, color);
            metaForSource.put(stack, meta);
        } catch (NBTException e) {
            throw new RuntimeException("bad input string: " + nbtJson, e);
        }
    }

    private static void discoverDynamicColors() {
        // TODO
    }

    static {
        addColor(new ItemStack(Blocks.planks), 0x9c6000);
        addColor(new ItemStack(Items.iron_ingot), 0xdbdbdb);
        addColor(new ItemStack(Items.diamond), 0x00fff2);
        addConfigColors();
        discoverDynamicColors();
        saveConfigColors();
        locked = true;
    }

    public static int getMaximumMeta() {
        return metaCounter - 1;
    }

    public static int getColorCount() {
        return metaCounter;
    }

    public static int getMeta(ItemStack stack) {
        return metaForSource.get(stack);
    }

    /**
     * Note: Returns the color for the meta given by {@link #getMeta(ItemStack)}
     * , not {@link ItemStack#getItemDamage()}.
     */
    public static int getColor(ItemStack stack) {
        return getColor(getMeta(stack));
    }

    public static int getColor(int metadata) {
        return colors.get(metadata);
    }

}
