package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.gui.GuiTBMCPU;
import com.techshroom.mods.tbm.gui.container.ContainerTBMCPU;
import com.techshroom.mods.tbm.machine.TBMMachine;
import com.techshroom.mods.tbm.machine.constructing.MachineDiscoverer;
import com.techshroom.mods.tbm.util.BlockToEntityMap;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TBMCPUEntity extends TBMGuiEntity<ContainerTBMCPU> {

    public TBMCPUEntity(World w) {
        super(w, store.get(TBMKeys.Blocks.CPU).get().getDefaultState());
    }

    public TBMCPUEntity(World w, TBMMachine machine) {
        this(w);
        this.machine = machine;
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
        // Halt and drop all entities.
        this.machine.getTrackedEntities().stream()
                .filter(TBMEntity.class::isInstance).map(TBMEntity.class::cast)
                .forEach(tbm -> {
                    tbm.setMoving(MovingState.HALTED);
                    this.machine.untrackEntity(tbm);
                });
    }

    public void guiStart() {
        // Flush state.
        guiStop();
        // Discover and collect entities to track.
        BlockToEntityMap map = BlockToEntityMap.getForWorld(this.worldObj);
        new MachineDiscoverer(this.worldObj, this.machine,
                getActualBlockPosition()).discover().map(map::get)
                        .forEach(this.machine::trackEntity);
        this.machine.getTrackedEntities().stream()
                .filter(TBMEntity.class::isInstance).map(TBMEntity.class::cast)
                .forEach(tbm -> {
                    // Update machine in all TBMs
                    tbm.machine = this.machine;
                    tbm.setMoving(MovingState.UP);
                });
    }

    @Override
    public void onMoveToBlock(BlockPos pos) {
    }

}
