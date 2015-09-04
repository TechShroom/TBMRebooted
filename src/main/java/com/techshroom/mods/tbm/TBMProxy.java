package com.techshroom.mods.tbm;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.TBMMod.store_put;

import com.techshroom.mods.tbm.block.TBMCPU;
import com.techshroom.mods.tbm.block.TBMCargo;
import com.techshroom.mods.tbm.block.TBMDrill;
import com.techshroom.mods.tbm.block.TBMEject;
import com.techshroom.mods.tbm.block.TBMEngine;
import com.techshroom.mods.tbm.block.tile.TBMCPUTile;
import com.techshroom.mods.tbm.block.tile.TBMCargoTile;
import com.techshroom.mods.tbm.block.tile.TBMDrillTile;
import com.techshroom.mods.tbm.block.tile.TBMEjectTile;
import com.techshroom.mods.tbm.block.tile.TBMEngineTile;
import com.techshroom.mods.tbm.entity.TBMCPUEntity;
import com.techshroom.mods.tbm.entity.TBMCargoEntity;
import com.techshroom.mods.tbm.entity.TBMDrillEntity;
import com.techshroom.mods.tbm.entity.TBMEjectEntity;
import com.techshroom.mods.tbm.entity.TBMEngineEntity;
import com.techshroom.mods.tbm.gui.GuiHandler;
import com.techshroom.mods.tbm.item.ItemDrillHead;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TBMProxy {

    protected Configuration conf;

    public final void preinit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(mod(), new GuiHandler());
        blockData();
        itemData();
        entityData();
        this.conf = store_put("config",
                new Configuration(event.getSuggestedConfigurationFile()));
        this.conf.load();
        store_put("use-chest-model",
                this.conf.getBoolean("use-chest-model", "models", false,
                        "true to use the chest model for the cargo block, "
                                + "false for the full block model.\n"));
        subpreinit();
        this.conf.save();
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
        registerBlock("cpu", new TBMCPU(), TBMCPUTile.class);
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

    /*
     * 256 is from the ender crystal distance, should be right for us
     */
    private static final int BLOCK_VIS_RANGE = 256;
    /*
     * MAX_VALUE is from the ender crystal and falling block update speed,
     * should be right for us
     */
    private static final int BLOCK_UPDATE_SPEED = Integer.MAX_VALUE;

    private void entityData() {
        registerEntity(TBMCPUEntity.class, "cpu");
        registerEntity(TBMCargoEntity.class, "cargo");
        registerEntity(TBMEjectEntity.class, "ejecter");
        registerEntity(TBMDrillEntity.class, "drill");
        registerEntity(TBMEngineEntity.class, "engine");
    }

    private int entCounter = 0;

    private void registerEntity(Class<? extends Entity> eClass, String id) {
        EntityRegistry.registerModEntity(eClass, id, this.entCounter++, mod(),
                BLOCK_VIS_RANGE, BLOCK_UPDATE_SPEED, true);
    }
}
