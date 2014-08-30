package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.techshroom.mods.tbm.block.tile.TBMEngineTile;

public class TBMEngine extends TBMBlockContainer<TBMEngineTile> {
    public TBMEngine() {
        super("engine", "engine-tex");
    }

    @Override
    protected TBMEngineTile g_createNewTileEntity(World w, int meta) {
        return new TBMEngineTile();
    }

    @Override
    protected boolean onBlockActivated(World w, int x, int y, int z,
            EntityPlayer p, TBMEngineTile tile) {
        tile.fireGUIOpenRequest(p);
        return true;
    }

    @Override
    public IIcon getIcon(int sd, int mt) {
        ForgeDirection dir = ForgeDirection.getOrientation(sd);
        if (dir == ForgeDirection.DOWN || dir == ForgeDirection.UP) {
            return ((TBMDrill) store_get("drill")).notTheDrillTexture();
        }
        return blockIcon;
    }
}
