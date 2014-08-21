package com.techshroom.mods.tbm;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TBMMod.ID, useMetadata = true, guiFactory = "com.techshroom.mods.tbm.gui.GuiFactory")
public final class TBMMod {
    static final String ID = "TBMMod";

    @Instance
    private static TBMMod INST;

    public static TBMMod mod() {
        return INST;
    }

    public Logger log;
    
    public String id() {
        return ID;
    }

    @EventHandler
    public void preinit(FMLPreInitializationEvent e) {
        log = e.getModLog();
        log.entry(e);
        log.exit();
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        log.entry(e);
        log.exit();
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent e) {
        log.entry(e);
        log.exit();
    }
}
