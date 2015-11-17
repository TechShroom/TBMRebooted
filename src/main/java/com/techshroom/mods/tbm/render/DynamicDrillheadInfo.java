package com.techshroom.mods.tbm.render;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.TBMMod.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import com.google.auto.value.AutoValue;
import com.techshroom.mods.tbm.TBMKeys;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import io.netty.util.internal.EmptyArrays;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * Drillhead information.
 */
@AutoValue
public abstract class DynamicDrillheadInfo {

    @AutoValue
    public static abstract class ItemMetaReference {

        public static ItemMetaReference of(Block block) {
            return of(block, 0);
        }

        public static ItemMetaReference of(Block block, int metadata) {
            return of(Item.getItemFromBlock(block), metadata);
        }

        public static ItemMetaReference of(Item item) {
            return of(item, 0);
        }

        public static ItemMetaReference of(Item item, int metadata) {
            return new AutoValue_DynamicDrillheadInfo_ItemMetaReference(item,
                    metadata);
        }

        public static ItemMetaReference of(ItemStack stack) {
            return of(stack.getItem(), stack.getItemDamage());
        }

        private static ItemMetaReference deserialize(String string)
                throws NBTException {
            NBTTagCompound tag = JsonToNBT.getTagFromJson(string);
            String item = tag.getString("item");
            try {
                return of(Item.getByNameOrId(item), tag.getInteger("meta"));
            } catch (NullPointerException npe) {
                if (npe.getMessage().equals("Null item")) {
                    NullPointerException msg =
                            new NullPointerException("Null item " + item);
                    msg.setStackTrace(npe.getStackTrace());
                    throw msg;
                }
                throw npe;
            }
        }

        ItemMetaReference() {
        }

        public abstract Item getItem();

        public abstract int getMetadata();

        public ItemStack convertToItemStack(int amount) {
            return new ItemStack(getItem(), amount, getMetadata());
        }

        private String serialize() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("item",
                    Optional.ofNullable((ResourceLocation) Item.itemRegistry
                            .getNameForObject(getItem()))
                    .map(ResourceLocation::toString).orElse("minecraft:air"));
            tag.setInteger("meta", getMetadata());
            return tag.toString();
        }

        @Override
        public final String toString() {
            return getItem().getUnlocalizedName() + ":" + getMetadata();
        }

    }

    private static Configuration CONFIGURATION;
    private static Property DRILLHEADS_PROP;

    public static final void doInitializationThatForgeScrewsOver() {
        CONFIGURATION = new Configuration(store.get(TBMKeys.CONFIG_DIR).get()
                .resolve(mod().id() + "DrillheadConfig.cfg").toFile());
        CONFIGURATION.load();
        Property prop = CONFIGURATION.get("items", "drillhead",
                EmptyArrays.EMPTY_STRINGS);
        prop.setLanguageKey("drillhead");
        prop.comment = "Drillhead listing."
                + " Don't edit unless you know what you're doing."
                + " [default: default + dynamically discovered drillheads]";
        DRILLHEADS_PROP = prop;
        CONFIGURATION.save();
        addDrillhead(ItemMetaReference.of(Items.wooden_pickaxe),
                ItemMetaReference.of(Blocks.planks), 0x9c6000);
        addDrillhead(ItemMetaReference.of(Items.iron_pickaxe),
                ItemMetaReference.of(Items.iron_ingot), 0xdbdbdb);
        addDrillhead(ItemMetaReference.of(Items.diamond_pickaxe),
                ItemMetaReference.of(Items.diamond), 0x00fff2);
        addConfigDrillheads();
        discoverDynamicDrillheads();
        saveConfigDrillheads();
        locked = true;
    }

    private static final Set<DynamicDrillheadInfo> allDrillheads =
            new HashSet<>();
    private static final TIntObjectMap<DynamicDrillheadInfo> metadataToDrill =
            new TIntObjectHashMap<>();
    private static final TIntObjectMap<Collection<DynamicDrillheadInfo>> colorToDrill =
            new TIntObjectHashMap<>();
    private static final Map<ItemMetaReference, DynamicDrillheadInfo> pickaxeToDrill =
            new HashMap<>();
    private static final Map<ItemMetaReference, DynamicDrillheadInfo> componentToDrill =
            new HashMap<>();
    private static BitSet metadataSet = new BitSet();
    private static boolean locked;

    private static DynamicDrillheadInfo checkedAddDrillhead(
            ItemMetaReference pickaxe, ItemMetaReference component, int color,
            int meta) {
        boolean haveMetadata = metadataSet.get(meta);
        boolean havePick = pickaxeToDrill.containsKey(pickaxe);
        boolean haveComp = componentToDrill.containsKey(component);
        if (haveMetadata || havePick || haveComp) {
            checkArgument(haveMetadata && havePick && haveComp,
                    "mismatched meta-pick-comp, we already have %s of [meta=%s,pick=%s,comp=%s]",
                    generateHaves(haveMetadata, havePick, haveComp), meta,
                    pickaxe, component);
            return metadataToDrill.get(meta);
        }
        checkState(!locked, "all info should have been created by now");
        metadataSet.set(meta);
        DynamicDrillheadInfo info = new AutoValue_DynamicDrillheadInfo(color,
                meta, pickaxe, component);
        allDrillheads.add(info);
        colorToDrill.putIfAbsent(color, new HashSet<>());
        colorToDrill.get(color).add(info);
        metadataToDrill.put(meta, info);
        pickaxeToDrill.put(pickaxe, info);
        componentToDrill.put(component, info);
        mod().log.debug("Added DDI " + info);
        return info;
    }

    private static List<String> generateHaves(boolean haveMetadata,
            boolean havePick, boolean haveComp) {
        List<String> strs = new ArrayList<>();
        if (haveMetadata) {
            strs.add("meta");
        }
        if (havePick) {
            strs.add("pick");
        }
        if (haveComp) {
            strs.add("comp");
        }
        return strs;
    }

    private static DynamicDrillheadInfo addDrillhead(ItemMetaReference pickaxe,
            ItemMetaReference component, int color) {
        return checkedAddDrillhead(pickaxe, component, color,
                metadataSet.nextClearBit(0));
    }

    private static void addConfigDrillheads() {
        String[] drillheads = DRILLHEADS_PROP.getStringList();
        for (String drill : drillheads) {
            deserializeDrillhead(drill);
        }
    }

    private static void saveConfigDrillheads() {
        String[] configDrills = DRILLHEADS_PROP.getStringList();
        Set<String> additionalDrills =
                new HashSet<>(Arrays.asList(configDrills));
        allDrillheads.stream().map(DynamicDrillheadInfo::serialize)
                .forEach(additionalDrills::add);
        DRILLHEADS_PROP.set(additionalDrills.stream().toArray(String[]::new));
        CONFIGURATION.save();
    }

    private static void deserializeDrillhead(String nbtJson) {
        try {
            DynamicDrillheadInfo info = deserialize(nbtJson);
            checkedAddDrillhead(info.getPickaxe(), info.getComponent(),
                    info.getColor(), info.getMetadata());
        } catch (NBTException e) {
            throw new RuntimeException("bad input string: " + nbtJson, e);
        }
    }

    private static DynamicDrillheadInfo deserialize(String str)
            throws NBTException {
        NBTTagCompound tag = JsonToNBT.getTagFromJson(str);
        return new AutoValue_DynamicDrillheadInfo(tag.getInteger("color"),
                tag.getInteger("meta"),
                ItemMetaReference.deserialize(tag.getString("pickaxe")),
                ItemMetaReference.deserialize(tag.getString("component")));
    }

    private static void discoverDynamicDrillheads() {
        // TODO
    }

    public static int getMaximumMeta() {
        return metadataSet.length() - 1;
    }

    public static int getColorCount() {
        return colorToDrill.size();
    }

    public static Optional<DynamicDrillheadInfo> lookupByColor(int color,
            Predicate<DynamicDrillheadInfo> filter) {
        return colorToDrill.get(color).stream().filter(filter).findFirst();
    }

    public static Optional<DynamicDrillheadInfo> lookupByMeta(int meta) {
        return Optional.ofNullable(metadataToDrill.get(meta));
    }

    public static Optional<DynamicDrillheadInfo>
            lookupByPickaxe(ItemMetaReference pick) {
        return Optional.ofNullable(pickaxeToDrill.get(pick));
    }

    public static Optional<DynamicDrillheadInfo>
            lookupByComponent(ItemMetaReference component) {
        return Optional.ofNullable(componentToDrill.get(component));
    }

    private String serialize() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("meta", getMetadata());
        tag.setInteger("color", getColor());
        tag.setString("pickaxe", getPickaxe().serialize());
        tag.setString("component", getComponent().serialize());
        return tag.toString();
    }

    public abstract int getColor();

    public abstract int getMetadata();

    public abstract ItemMetaReference getPickaxe();

    public abstract ItemMetaReference getComponent();

}
