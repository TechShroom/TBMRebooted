package com.techshroom.mods.tbm;

import static com.techshroom.mods.tbm.TBMMod.store;
import static com.techshroom.mods.tbm.TBMMod.store_get;

import com.techshroom.mods.tbm.block.TBMDrill;
import com.techshroom.mods.tbm.block.tile.TBMDrillTile;

import cpw.mods.fml.common.registry.GameRegistry;

public class TBMProxy {
    public final void preinit() {
        blockData();
        subpreinit();
    }

    protected void subpreinit() {
    }

    public final void init() {
        subinit();
    }

    protected void subinit() {
    }

    public final void postinit() {
        subpostinit();
    }

    protected void subpostinit() {
    }

    public boolean isClient() {
        return false;
    }

    private void blockData() {
        store.put("drill", new TBMDrill());
        
        TBMDrill drill = store_get("drill");
        
        GameRegistry.registerBlock(drill, drill.getUnlocalizedName());
        
        GameRegistry.registerTileEntity(TBMDrillTile.class, "drill");
    }
}
