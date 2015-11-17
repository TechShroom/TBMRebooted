package com.techshroom.mods.tbm;

import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.techshroom.mods.tbm.util.Storage.BooleanKey;
import com.techshroom.mods.tbm.util.Storage.IntKey;
import com.techshroom.mods.tbm.util.Storage.Key;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public final class TBMKeys {

    public static final Key<TBMMod> MOD = Key.Named.create("mod");
    public static final Key<TBMProxy> PROXY = Key.Named.create("proxy");
    public static final Key<WorldFetchProxy> WORLD_PROXY =
            Key.Named.create("world_proxy");
    public static final Key<String> ID = Key.Named.create("id");
    public static final Key<CreativeTabs> ITEM_TAB =
            Key.Named.create("itemTab");
    public static final Key<CreativeTabs> BLOCK_TAB =
            Key.Named.create("blockTab");
    public static final Key<Path> CONFIG_DIR = Key.Named.create("configDir");
    public static final Key<Configuration> CONFIG = Key.Named.create("config");
    public static final Key<Logger> LOGGER = Key.Named.create("logger");
    public static final Key<SimpleNetworkWrapper> NETWORK_CHANNEL =
            Key.Named.create("netchannel");
    public static final BooleanKey USE_CHEST_MODEL =
            BooleanKey.Named.create("chestmodel");

    public static final class GuiId {

        public static final IntKey DRILL = IntKey.Named.create("drill_gui");
        public static final IntKey CARGO = IntKey.Named.create("cargo_gui");
        public static final IntKey ENGINE = IntKey.Named.create("engine_gui");
        public static final IntKey CPU = IntKey.Named.create("cpu_gui");

        public static final List<IntKey> ALL =
                ImmutableList.of(DRILL, CARGO, ENGINE, CPU);

        private GuiId() {
        }

    }

    public static final class Items {

        public static final Key<Item> DRILLHEAD = Key.Named.create("drillhead");

        public static final List<Key<Item>> ALL = ImmutableList.of(DRILLHEAD);

        private Items() {
        }

    }

    public static final class Blocks {

        public static final Key<Block> DRILL = Key.Named.create("drill");
        public static final Key<Block> CARGO = Key.Named.create("cargo");
        public static final Key<Block> ENGINE = Key.Named.create("engine");
        public static final Key<Block> CPU = Key.Named.create("cpu");
        public static final Key<Block> EJECTOR = Key.Named.create("ejector");

        public static final List<Key<Block>> ALL =
                ImmutableList.of(DRILL, CARGO, ENGINE, CPU, EJECTOR);

        private Blocks() {
        }

    }

    private TBMKeys() {
    }

}
