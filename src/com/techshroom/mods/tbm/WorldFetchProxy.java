package com.techshroom.mods.tbm;

import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;

public class WorldFetchProxy {
    public World fetch(int dimID) {
        return FMLCommonHandler.instance().getMinecraftServerInstance()
                .worldServerForDimension(dimID);
    }
}