package com.techshroom.mods.tbm;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class WorldFetchProxy_client extends WorldFetchProxy {

    @Override
    public World fetch(int dimID) {
        return FMLCommonHandler.instance().getMinecraftServerInstance()
                .worldServerForDimension(dimID);
    }
}