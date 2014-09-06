package com.techshroom.mods.tbm;

import static com.techshroom.mods.tbm.Tutils.cast;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import org.apache.logging.log4j.Logger;

import com.techshroom.mods.tbm.debug.KillAllCommmand;
import com.techshroom.mods.tbm.net.NetProxy;
import com.techshroom.mods.tbm.net.NetProxy.Client;
import com.techshroom.mods.tbm.net.NetProxy.Server;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = TBMMod.ID, useMetadata = true,
        guiFactory = "com.techshroom.mods.tbm.gui.GuiFactory")
public final class TBMMod {
    static final String ID = "TBMMod";

    @Instance
    private static TBMMod INST;
    @SidedProxy(clientSide = "com.techshroom.mods.tbm.TBMCProxy",
            serverSide = "com.techshroom.mods.tbm.TBMProxy")
    private static TBMProxy PROX;
    @SidedProxy(clientSide = "com.techshroom.mods.tbm.WorldFetchProxy_client",
            serverSide = "com.techshroom.mods.tbm.WorldFetchProxy")
    private static WorldFetchProxy WORLD_FETCHER;
    private static final CreativeTabs tabBlock = new CreativeTabs("tbmBlocks") {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock((Block) store_get("drill"));
        }
    };
    private static final CreativeTabs tabItem = new CreativeTabs("tbmItems") {
        @Override
        public Item getTabIconItem() {
            return store_get("drillhead");
        }
    };
    public static final Map<String, Object> store =
            new HashMap<String, Object>();

    public static <T> T store_get(String key) {
        return cast(store.get(key));
    }

    public static <T> T store_put(String key, T object) {
        if (store.containsKey(key)) {
            INST.log.info("replacing key " + key + ", " + store.get(key)
                    + " --replace-> " + object);
        }
        store.put(key, object);
        return object;
    }

    public static TBMMod mod() {
        return INST;
    }

    public static TBMProxy proxy() {
        return PROX;
    }

    public static WorldFetchProxy worldFetcher() {
        return WORLD_FETCHER;
    }

    static {
        store.put("mod", mod());
        store.put("proxy", proxy());
        store.put("proxy_wold", worldFetcher());
        store.put("id", ID);
        store.put("itemTab", tabItem);
        store.put("blockTab", tabBlock);
    }

    public Logger log;

    public String id() {
        return ID;
    }

    @EventHandler
    public void preinit(FMLPreInitializationEvent e) {
        log = e.getModLog();
        log.entry(e);
        store.put("log", log);
        if (PROX.isClient()) {
            netmanager = new Client();
        } else {
            netmanager = new Server();
        }
        netmanager.doNetRegistrationsForSide();
        proxy().preinit(e);
        log.exit();
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        log.entry(e);
        proxy().init();
        log.exit();
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent e) {
        log.entry(e);
        proxy().postinit();
        log.exit();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        try {
            event.registerServerCommand(new KillAllCommmand());
        } catch (Throwable t) {
        }
    }

    private static int guiCounter = 0;

    public static int requestGUIId() {
        return guiCounter++;
    }

    static {
        store_put("drill-gui-id", requestGUIId());
        store_put("cargo-gui-id", requestGUIId());
        store_put("engine-gui-id", requestGUIId());
        store_put("cpu-gui-id", requestGUIId());
    }

    private static NetProxy netmanager;

    public NetProxy netmanager() {
        return netmanager;
    }
}
