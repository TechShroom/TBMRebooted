package com.techshroom.mods.tbm.util;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Optional;

/**
 * Only allows modification once. After that, it is locked down. Does not allow
 * nulls.
 */
public class ImmutableAfterSetReference<T> {

    private volatile Optional<T> ref = Optional.empty();

    public ImmutableAfterSetReference() {
    }

    public boolean isLockedDown() {
        return this.ref.isPresent();
    }

    public void set(T ref) {
        checkNotNull(ref);
        checkState(!isLockedDown(), "must be unlocked to set value");
        this.ref = Optional.of(ref);
    }

    public Optional<T> get() {
        return this.ref;
    }

}
