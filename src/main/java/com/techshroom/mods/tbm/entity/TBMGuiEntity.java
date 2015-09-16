package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.Tutils.isClient;

import com.techshroom.mods.tbm.gui.IGuiProvider;
import com.techshroom.mods.tbm.gui.IPlayerContainerProvider;

import codechicken.lib.math.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public abstract class TBMGuiEntity<C extends Container> extends TBMEntity
        implements IGuiProvider<C>, IPlayerContainerProvider {

    protected TBMGuiEntity(World w) {
        super(w);
    }

    public abstract int getGuiId();

    public boolean rightClick(EntityPlayer player, int fx, int fy, int fz) {
        if (player.isSneaking()) {
            // special case
            return false;
        }
        if (!isClient(this.worldObj))
            player.openGui(mod(), getGuiId(), this.worldObj, fx, fy, fz);
        return true;
    }

    @Override
    public boolean interactFirst(EntityPlayer p_130002_1_) {
        return rightClick(p_130002_1_, MathHelper.floor_double(this.posX),
                MathHelper.floor_double(this.posY),
                MathHelper.floor_double(this.posZ));
    }

}
