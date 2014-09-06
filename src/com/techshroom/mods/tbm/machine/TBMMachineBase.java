package com.techshroom.mods.tbm.machine;

import com.techshroom.mods.tbm.Tutils;
import com.techshroom.mods.tbm.net.messageForClients.MessageSomeMachineInfo;

public abstract class TBMMachineBase {
    public TBMEntityMachineBase asEntityBase() {
        return this instanceof TBMEntityMachineBase ? Tutils
                .<TBMEntityMachineBase> cast(this) : null;
    }

    public TBMTileMachineBase asTileBase() {
        return this instanceof TBMTileMachineBase ? Tutils
                .<TBMTileMachineBase> cast(this) : null;
    }

    public abstract void doOpenGUI();

    public abstract void loadFromMessage(MessageSomeMachineInfo infoMessage);
}
