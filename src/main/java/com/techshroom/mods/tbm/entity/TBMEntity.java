package com.techshroom.mods.tbm.entity;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.techshroom.mods.tbm.Tutils.isClient;

import com.techshroom.mods.tbm.machine.TBMMachine;
import com.techshroom.mods.tbm.util.CalledOnServerOnly;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TBMEntity extends Entity {

    /**
     * No extra collision border
     */
    private static final float NO_BORDER = 0.0f;
    private static final float FULL_BOX_SIZE = 1F;
    private static final float BOX_SIZE = FULL_BOX_SIZE;
    private static final float EMPTY_SIZE = 0.1F;

    protected BlockPos lastBlockPos;
    private MovingState moving;
    private MovingState delayedSetMoving;
    private boolean waitingForGridAlignedPosition;
    private IBlockState state;
    protected TBMMachine machine;

    public TBMEntity(World w, IBlockState state) {
        super(w);
        setMoving(MovingState.HALTED);
        checkNotNull(state);
        this.state = state;
        this.isImmuneToFire = true;
    }

    @Override
    public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn,
            float rotationPitchIn) {
        this.lastBlockPos = pos;
        super.moveToBlockPosAndAngles(pos, rotationYawIn, rotationPitchIn);
    }

    public IBlockState getState() {
        return this.state;
    }

    public MovingState getMoving() {
        return this.moving;
    }

    public final void setMoving(MovingState moving) {
        if (isClient(this.worldObj)) {
            // on client we never update the moving state with this
            return;
        }
        if (this.firstUpdate) {
            // if it is our first update, always SML
            setMovingLogic(moving);
            return;
        }
        boolean change = moving != this.moving;
        if (!change) {
            // nothing to do
            return;
        }
        if (!moving.hasMotion()) {
            // must wait for logic until we hit an integer stopping point
            this.waitingForGridAlignedPosition = true;
            this.delayedSetMoving = moving;
            System.err.println("must wait");
            return;
        }
        // otherwise feel free to start!
        setMovingLogic(moving);
    }

    protected void setMovingLogic(MovingState moving) {
        checkState(!isClient(this.worldObj),
                "Must not update moving state with SML on client");
        this.moving = moving;
        boolean isFirstUpdate = this.firstUpdate;
        this.firstUpdate = true; // disable move-on-setsize
        if (moving.hasMotion()) {
            this.worldObj.setBlockToAir(getPosition());
            setSize(BOX_SIZE, BOX_SIZE);
        } else {
            if (!isFirstUpdate) {
                this.worldObj.setBlockState(getPosition(), getState());
            }
            setSize(EMPTY_SIZE, EMPTY_SIZE);
        }
        this.firstUpdate = isFirstUpdate;
    }

    @CalledOnServerOnly
    public abstract void onMoveToBlock(BlockPos pos);

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!getPosition().equals(this.lastBlockPos)) {
            // change probably
            if (!isClient(this.worldObj)) {
                onMoveToBlock(getPosition());
            }
            this.lastBlockPos = getPosition();
            if (this.waitingForGridAlignedPosition) {
                setMovingLogic(this.delayedSetMoving);
                this.waitingForGridAlignedPosition = false;
            }
        }
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public NBTTagCompound getNBTTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        writeEntityToNBT(tag);
        return tag;
    }

    // read client tag compound
    @SideOnly(Side.CLIENT)
    @Override
    public void func_174834_g(NBTTagCompound compound) {
        if (compound != null) {
            readEntityFromNBT(compound);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        this.state =
                this.state.getBlock().getStateFromMeta(nbt.getInteger("state"));
        // not setMoving, we expect all state to be good
        this.moving = MovingState.valueOf(nbt.getString("moving"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("state",
                this.state.getBlock().getMetaFromState(this.state));
        nbt.setString("moving", this.moving.name());
    }

    public ResourceLocation getEntityTexture() {
        return TextureMap.locationBlocksTexture;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.moving.hasMotion();
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
        return true;
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
