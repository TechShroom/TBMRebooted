package com.techshroom.mods.tbm.machine.constructing;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.techshroom.mods.tbm.machine.TBMMachine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class MachineDiscoverer {

    private final World container;
    private final TBMMachine machine;
    private final BlockPos start;

    public MachineDiscoverer(World container, TBMMachine machine,
            BlockPos start) {
        this.container = container;
        this.machine = machine;
        this.start = start;
    }

    public Stream<BlockPos> discover() {
        Set<BlockPos> blocks = new HashSet<>();
        Queue<BlockPos> toScan = new LinkedList<>();
        blocks.add(this.start);
        toScan.add(this.start);
        while (!toScan.isEmpty()) {
            BlockPos next = toScan.poll();
            System.err.println("Scanning " + next);
            Set<BlockPos> aroundNext = Stream.of(EnumFacing.VALUES)
                    .map(ef -> next.offset(ef)).collect(Collectors.toSet());
            Stream.Builder<BlockPos> newKeyPoints = Stream.builder();
            for (BlockPos around : aroundNext) {
                System.err.println("Checking " + around);
                if (blocks.contains(around)) {
                    continue;
                }
                IBlockState state = this.container.getBlockState(around);
                if (this.machine.isBlockAllowed(state)) {
                    System.err.println("OK " + around);
                    newKeyPoints.add(around);
                    blocks.add(around);
                }
            }
            newKeyPoints.build().forEach(toScan::add);
        }
        return blocks.stream();
    }

}
