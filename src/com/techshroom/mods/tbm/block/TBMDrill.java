package com.techshroom.mods.tbm.block;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.Tutils.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.techshroom.mods.tbm.Tutils.MetadataConstants;
import com.techshroom.mods.tbm.block.tile.TBMDrillTile;

public class TBMDrill extends TBMBlockContainer<TBMDrillTile> {
    private IIcon nonFront;

    public TBMDrill() {
        super("drill", "drill-tex");
    }

    @Override
    protected TBMDrillTile g_createNewTileEntity(World w, int meta) {
        return new TBMDrillTile();
    }

    @Override
    protected boolean onBlockActivated(World w, int x, int y, int z,
            EntityPlayer p, TBMDrillTile tile) {
        tile.fireGUIOpenRequest(p);
        return true;
    }

    @Override
    public void onBlockPlacedBy(World w, int x, int y, int z,
            EntityLivingBase ent, ItemStack stack) {
        super.onBlockPlacedBy(w, x, y, z, ent, stack);
        w.setBlockMetadataWithNotify(x, y, z,
                getMetadataForBlock(x, y, z, ent), MetadataConstants.SEND);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);
        nonFront =
                reg.registerIcon(address(mod().id(), "drill_close-tex")
                        .toString());
    }

    @Override
    public IIcon getIcon(int sd, int mt) {
        return sd == mt ? blockIcon : nonFront;
    }

    public IIcon notTheDrillTexture() {
        return nonFront;
    }
}
