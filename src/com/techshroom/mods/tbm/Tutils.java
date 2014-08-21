package com.techshroom.mods.tbm;

public final class Tutils {
    private Tutils() {
    }

    public static class MCAddress {
        private final String id, object;

        private MCAddress(String d, String obj) {
            id = d;
            object = obj;
        }

        @Override
        public String toString() {
            return id + ":" + object;
        }
    }
    
    public static MCAddress address(String id, String object){
        return new MCAddress(id, object);
    }
}
