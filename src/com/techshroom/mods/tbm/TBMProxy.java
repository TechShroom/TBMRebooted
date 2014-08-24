package com.techshroom.mods.tbm;

import static com.techshroom.mods.tbm.TBMMod.*;

import com.techshroom.mods.tbm.block.TBMDrill;
import com.techshroom.mods.tbm.block.TBMEject;
import com.techshroom.mods.tbm.block.tile.TBMDrillTile;
import com.techshroom.mods.tbm.entity.TBMDrillEntity;
import com.techshroom.mods.tbm.gui.GuiHandler;
import com.techshroom.mods.tbm.item.ItemDrillHead;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class TBMProxy {
    public final void preinit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(mod(), new GuiHandler());
        blockData();
        itemData();
        entityData();
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
        TBMDrill drill = store_put("drill", new TBMDrill());
        TBMEject eject = store_put("ejecter", new TBMEject());

        GameRegistry.registerBlock(drill, drill.getUnlocalizedName());
        GameRegistry.registerBlock(eject, eject.getUnlocalizedName());

        GameRegistry.registerTileEntity(TBMDrillTile.class, "drill");
    }

    private void itemData() {
        store_put("drillhead", new ItemDrillHead());
    }

    private void entityData() {
        EntityRegistry
                .registerGlobalEntityID(
                        TBMDrillEntity.class,
                        "drill",
                        store_put("drill-id",
                                EntityRegistry.findGlobalUniqueEntityId()));
    }
}
