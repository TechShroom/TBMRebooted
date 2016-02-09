package com.techshroom.mods.tbm.machine.provider;

import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

import com.google.common.collect.ImmutableMap;

public final class TBMMachineProviders {

    private static final Map<String, TBMMachineProvider> providerMap;
    static {
        ImmutableMap.Builder<String, TBMMachineProvider> b =
                ImmutableMap.builder();
        ServiceLoader.load(TBMMachineProvider.class)
                .forEach(x -> b.put(x.getId(), x));
        providerMap = b.build();
    }

    public static Optional<TBMMachineProvider> getProvider(String id) {
        return Optional.ofNullable(providerMap.get(id));
    }

    public static TBMMachineProvider getProviderOrFail(String id) {
        return getProvider(id).orElseThrow(() -> new IllegalArgumentException(
                "Provider " + id + " does not exist"));
    }

    private TBMMachineProviders() {
        throw new AssertionError("...");
    }

}
