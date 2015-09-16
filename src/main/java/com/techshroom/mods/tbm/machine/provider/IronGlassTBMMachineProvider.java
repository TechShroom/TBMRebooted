package com.techshroom.mods.tbm.machine.provider;

import com.google.auto.service.AutoService;
import com.techshroom.mods.tbm.machine.IronGlassTBMMachine;
import com.techshroom.mods.tbm.machine.TBMMachine;

@AutoService(TBMMachineProvider.class)
public class IronGlassTBMMachineProvider implements TBMMachineProvider {

    @Override
    public String getId() {
        return "ironglass";
    }

    @Override
    public String getProvidedMachineName() {
        return "Iron Glass";
    }

    @Override
    public TBMMachine provideMachine() {
        return new IronGlassTBMMachine(getProvidedMachineName());
    }

}
