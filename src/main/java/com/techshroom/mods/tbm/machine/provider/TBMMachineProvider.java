package com.techshroom.mods.tbm.machine.provider;

import com.techshroom.mods.tbm.machine.TBMMachine;

public interface TBMMachineProvider {

    String getId();

    String getProvidedMachineName();

    TBMMachine provideMachine();

}
