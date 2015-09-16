package com.techshroom.mods.tbm.entity;

import com.techshroom.mods.tbm.machine.TBMMachine;
import com.techshroom.mods.tbm.util.CalledOnServerOnly;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class TBMEntity extends Entity {

    /**
     * No extra collision border
     */
    private static final float NO_BORDER = 0.0f;
    private static final float FULL_BOX_SIZE = 1f;
    private static final float BOX_SIZE = FULL_BOX_SIZE;
    protected TBMMachine machine;

    public TBMEntity(World w) {
        super(w);
        setSize(BOX_SIZE, BOX_SIZE);
    }

    public abstract Block blockBase();

    @CalledOnServerOnly
    public abstract void onMoveToBlock(BlockPos pos);
    
    @Override
    public void onUpdate() {
        // TODO Auto-generated method stub
        super.onUpdate();
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
    }

    public ResourceLocation getEntityTexture() {
        return TextureMap.locationBlocksTexture;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void applyEntityCollision(Entity p_70108_1_) {
        // super.applyEntityCollision(p_70108_1_);
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    public boolean hitByEntity(Entity p_85031_1_) {
        return super.hitByEntity(p_85031_1_);
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    public float getCollisionBorderSize() {
        return NO_BORDER;
    }
}
