package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.techshroom.mods.tbm.block.tile.TBMCargoTile;

public class TBMCargo extends TBMBlockContainer<TBMCargoTile> {
    public TBMCargo() {
        super("cargo", "cargo_side-tex");
    }

    @Override
    protected TBMCargoTile g_createNewTileEntity(World w, int meta) {
        return new TBMCargoTile();
    }

    @Override
    protected boolean onBlockActivated(World w, int x, int y, int z,
            EntityPlayer p, TBMCargoTile tile) {
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
