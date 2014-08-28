package com.techshroom.mods.tbm;

import static com.techshroom.mods.tbm.TBMMod.*;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

import com.techshroom.mods.tbm.block.TBMCargo;
import com.techshroom.mods.tbm.block.TBMDrill;
import com.techshroom.mods.tbm.block.TBMEject;
import com.techshroom.mods.tbm.block.TBMEngine;
import com.techshroom.mods.tbm.block.tile.TBMCargoTile;
import com.techshroom.mods.tbm.block.tile.TBMDrillTile;
import com.techshroom.mods.tbm.block.tile.TBMEjectTile;
import com.techshroom.mods.tbm.block.tile.TBMEngineTile;
import com.techshroom.mods.tbm.entity.TBMDrillEntity;
import com.techshroom.mods.tbm.entity.TBMEjectEntity;
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
        registerBlock("drill", new TBMDrill(), TBMDrillTile.class);
        registerBlock("ejecter", new TBMEject(), TBMEjectTile.class);
        registerBlock("cargo", new TBMCargo(), TBMCargoTile.class);
        registerBlock("engine", new TBMEngine(), TBMEngineTile.class);
    }

    private void registerBlock(String storeName, Block b,
            Class<? extends TileEntity> tile) {
        store_put(storeName, b);
        GameRegistry.registerBlock(b, b.getUnlocalizedName());

        if (tile != null) {
            GameRegistry.registerTileEntity(tile, storeName);
        }
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
        EntityRegistry
                .registerGlobalEntityID(
                        TBMEjectEntity.class,
                        "eject",
                        store_put("eject-id",
                                EntityRegistry.findGlobalUniqueEntityId()));
        EntityRegistry
                .registerGlobalEntityID(
                        TBMDrillEntity.class,
                        "cargo",
                        store_put("cargo-id",
                                EntityRegistry.findGlobalUniqueEntityId()));
    }
}
