package com.techshroom.mods.tbm;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class WorldFetchProxyClient extends WorldFetchProxy {

    @Override
    public World fetch(int dimID) {
        return FMLCommonHandler.instance().getMinecraftServerInstance()
                .worldServerForDimension(dimID);
    }
}