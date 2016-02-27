package com.techshroom.mods.tbm.entity;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.Tutils.isClient;

import java.util.Set;

import com.techshroom.mods.tbm.machine.TBMMachine;
import com.techshroom.mods.tbm.machine.provider.TBMMachineProviders;
import com.techshroom.mods.tbm.util.BlockToEntityMap;
import com.techshroom.mods.tbm.util.CalledOnServerOnly;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TBMEntity extends Entity {

    /**
     * No extra collision border
     */
    private static final float NO_BORDER = 0.0f;
    private static final float FULL_BOX_SIZE = 1F;
    private static final float BOX_SIZE = FULL_BOX_SIZE;
    private static final float EMPTY_SIZE = 0.0001F;

    private static final String MACHINE_KEY = "machine";
    private static final String MACHINE_ID_KEY = "id";
    private static final String MACHINE_ENTITIES_KEY = "machine-entities";

    protected BlockPos lastBlockPos;
    private MovingState moving;
    private MovingState delayedSetMoving;
    private boolean waitingForGridAlignedPosition;
    private IBlockState state;
    protected TBMMachine machine;

    public TBMEntity(World w, IBlockState state) {
        super(w);
        if (isClient(w)) {
            setMovingClient(MovingState.HALTED);
        } else {
            setMoving(MovingState.HALTED);
        }
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

    public TBMMachine getMachine() {
        return this.machine;
    }

    public final void setMoving(MovingState moving) {
        checkState(!isClient(this.worldObj), "server only");
        if (this.firstUpdate) {
            // if it is our first update, always SML
            setMovingLogic(moving);
            return;
        }
        if (!moving.hasMotion()) {
            // must wait for logic until we hit an integer stopping point
            this.waitingForGridAlignedPosition = true;
            this.delayedSetMoving = moving;
            System.err.println("must wait");
            return;
        }
        boolean change = moving != this.moving;
        if (!change) {
            // nothing to do
            return;
        }
        // otherwise feel free to start!
        setMovingLogic(moving);
    }

    protected void setMovingLogic(MovingState moving) {
        checkState(!isClient(this.worldObj),
                "Must not update moving state with SML on client");
        setCommonMovingLogic(moving);
        if (moving.hasMotion()) {
            this.worldObj.setBlockToAir(getPosition());
        } else {
            if (!this.firstUpdate) {
                this.worldObj.setBlockState(getPosition(), getState());
            }
        }
        WorldServer wS = (WorldServer) this.worldObj;
        Set<? extends EntityPlayer> watchers =
                wS.getEntityTracker().getTrackingPlayers(this);
        watchers.stream().filter(EntityPlayerMP.class::isInstance)
                .map(EntityPlayerMP.class::cast).forEach(emp -> {
                    emp.playerNetServerHandler.sendPacket(
                            new S49PacketUpdateEntityNBT(this.getEntityId(),
                                    this.getNBTTagCompound()));
                });
    }

    public void setMovingClient(MovingState moving) {
        checkState(isClient(this.worldObj), "client only");
        setCommonMovingLogic(moving);
    }

    protected void setCommonMovingLogic(MovingState moving) {
        boolean change = this.moving != moving;
        this.moving = moving;
        if (change) {
            boolean isFirstUpdate = this.firstUpdate;
            this.firstUpdate = true; // disable move-on-setsize
            if (moving.hasMotion()) {
                setSize(BOX_SIZE, BOX_SIZE);
                // Has to be moved manually, but only on server.
                moveEntity(-0.5, 0, -0.5);
            } else {
                setSize(EMPTY_SIZE, EMPTY_SIZE);
            }
            this.firstUpdate = isFirstUpdate;
        }
    }

    @CalledOnServerOnly
    public abstract void onMoveToBlock(BlockPos pos);

    @Override
    public void onUpdate() {
        super.onUpdate();
        // System.err.println("update " + this + " @ " + this.worldObj);
        if (!getMoving().hasMotion() && !isClient(this.worldObj)) {
            BlockPos pos = getActualBlockPosition();
            if (!this.state.equals(this.worldObj.getBlockState(pos))) {
                // Extra entity.
                mod().log.warn("Removing extra entity at " + pos);
                setDead();
                return;
            }
        }
        BlockPos pos = getPosition();
        if (!pos.equals(this.lastBlockPos)) {
            // change probably
            if (!isClient(this.worldObj)) {
                onMoveToBlock(getPosition());
            }
            this.lastBlockPos = getPosition();
        }
        if (nearlyExactlyHere(pos) && !isClient(this.worldObj)) {
            if (this.waitingForGridAlignedPosition) {
                setMovingLogic(this.delayedSetMoving);
                this.waitingForGridAlignedPosition = false;
            }
        }
    }

    private boolean nearlyExactlyHere(BlockPos pos) {
        double error = 0.001;
        return Math.abs(pos.getX() - this.posX) < error
                && Math.abs(pos.getY() - this.posY) < error
                && Math.abs(pos.getZ() - this.posZ) < error;
    }

    public BlockPos getActualBlockPosition() {
        Vec3i tmp =
                BlockToEntityMap.getForWorld(this.worldObj).getReverse(this);
        if (tmp instanceof BlockPos) {
            return (BlockPos) tmp;
        }
        return new BlockPos(tmp);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public NBTTagCompound getNBTTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag(MACHINE_KEY, createMachineTagCompound());
        writeEntityToNBT(tag);
        // Add extra BB data.
        tag.setDouble("width", this.width);
        tag.setDouble("height", this.height);
        return tag;
    }

    // read client tag compound
    @SideOnly(Side.CLIENT)
    @Override
    public void clientUpdateEntityNBT(NBTTagCompound compound) {
        if (compound != null) {
            this.machine = createMachineFromTagCompound(
                    compound.getCompoundTag(MACHINE_KEY));
            readEntityFromNBT(compound);
            // Update moving state.
            setMovingClient(this.moving);
            // Additionally, we expect the size data
            setSize((float) compound.getDouble("width"),
                    (float) compound.getDouble("height"));
            System.err.println("size " + this.width + ":" + this.height);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        this.state =
                this.state.getBlock().getStateFromMeta(nbt.getInteger("state"));
        MovingState moving = MovingState.valueOf(nbt.getString("moving"));
        if (!isClient(this.worldObj)) {
            setMoving(moving);
        } else {
            setMovingClient(moving);
        }
    }

    private TBMMachine createMachineFromTagCompound(NBTTagCompound compound) {
        if (compound.hasNoTags()) {
            return null;
        }
        TBMMachine machine = TBMMachineProviders
                .getProviderOrFail(compound.getString(MACHINE_ID_KEY))
                .provideMachine();
        NBTTagList entities =
                compound.getTagList(MACHINE_ENTITIES_KEY, NBT.TAG_INT);
        for (int i = 0; i < entities.tagCount(); i++) {
            Entity ent = this.worldObj
                    .getEntityByID(((NBTTagInt) entities.get(i)).getInt());
            machine.trackEntity(ent);
        }
        return machine;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("state",
                this.state.getBlock().getMetaFromState(this.state));
        nbt.setString("moving", this.moving.name());
    }

    private NBTTagCompound createMachineTagCompound() {
        NBTTagCompound compound = new NBTTagCompound();
        if (this.machine == null) {
            return compound;
        }
        NBTTagList entities = new NBTTagList();
        this.machine.getTrackedEntities().forEach(e -> {
            entities.appendTag(new NBTTagInt(e.getEntityId()));
        });
        compound.setString(MACHINE_ID_KEY, this.machine.getId());
        compound.setTag(MACHINE_ENTITIES_KEY, entities);
        return compound;
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
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    public float getCollisionBorderSize() {
        return NO_BORDER;
    }

}
