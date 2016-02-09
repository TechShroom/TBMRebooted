package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.gui.GuiTBMCPU;
import com.techshroom.mods.tbm.gui.container.ContainerTBMCPU;
import com.techshroom.mods.tbm.machine.TBMMachine;
import com.techshroom.mods.tbm.machine.provider.TBMMachineProviders;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class TBMCPUEntity extends TBMGuiEntity<ContainerTBMCPU> {

    private static final String MACHINE_KEY = "machine";
    private static final String MACHINE_ID_KEY = "id";
    private static final String MACHINE_ENTITIES_KEY = "machine-entities";
    private TBMMachine machine;
    private transient Set<UUID> trackOnUpdate;

    public TBMCPUEntity(World w) {
        super(w, store.get(TBMKeys.Blocks.CPU).get().getDefaultState());
    }

    public TBMCPUEntity(World w, TBMMachine machine) {
        this(w);
        this.machine = machine;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.trackOnUpdate != null) {
            this.trackOnUpdate.forEach(e -> {
                this.worldObj.getLoadedEntityList().stream()
                        .filter(x -> x.getUniqueID().equals(e)).findFirst()
                        .ifPresent(this.machine::trackEntity);
            });
            this.trackOnUpdate = null;
        }
    }

    @Override
    public Container container(EntityPlayer player) {
        return new ContainerTBMCPU(player);
    }

    @Override
    public GuiScreen guiScreen(ContainerTBMCPU c) {
        return new GuiTBMCPU(this);
    }

    @Override
    public int getGuiId() {
        return store.get(TBMKeys.GuiId.CPU).getAsInt();
    }

    public void guiStop() {
    }

    public void guiStart() {
    }

    @Override
    public void onMoveToBlock(BlockPos pos) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setTag(MACHINE_KEY, createMachineTagCompound());
        super.writeEntityToNBT(nbt);
    }

    private NBTTagCompound createMachineTagCompound() {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList entities = new NBTTagList();
        this.machine.getTrackedEntities().forEach(e -> {
            NBTTagList uuid = new NBTTagList();
            uuid.appendTag(
                    new NBTTagLong(e.getUniqueID().getMostSignificantBits()));
            uuid.appendTag(
                    new NBTTagLong(e.getUniqueID().getLeastSignificantBits()));
            entities.appendTag(uuid);
        });
        compound.setString(MACHINE_ID_KEY, this.machine.getId());
        compound.setTag(MACHINE_ENTITIES_KEY, entities);
        return compound;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.machine =
                createMachineFromTagCompound(nbt.getCompoundTag(MACHINE_KEY));
    }

    private TBMMachine createMachineFromTagCompound(NBTTagCompound compound) {
        TBMMachine machine = TBMMachineProviders
                .getProviderOrFail(compound.getString(MACHINE_ID_KEY))
                .provideMachine();
        NBTTagList entities =
                compound.getTagList(MACHINE_ENTITIES_KEY, NBT.TAG_LIST);
        this.trackOnUpdate = new HashSet<>(entities.tagCount());
        for (int i = 0; i < entities.tagCount(); i++) {
            NBTTagList uuid = (NBTTagList) entities.get(i);
            this.trackOnUpdate
                    .add(new UUID(((NBTTagLong) uuid.get(0)).getLong(),
                            ((NBTTagLong) uuid.get(1)).getLong()));
        }
        return machine;
    }

}
