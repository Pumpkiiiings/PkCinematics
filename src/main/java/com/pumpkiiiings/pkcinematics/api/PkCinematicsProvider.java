package com.pumpkiiiings.pkcinematics.api;

public class PkCinematicsProvider {
    private static PkCinematics instance;

    public static PkCinematics get() {
        if (instance == null) {
            throw new IllegalStateException("PkCinematics API is not initialized yet.");
        }
        return instance;
    }

    public static void register(PkCinematics api) {
        instance = api;
    }
    
    public static void unregister() {
        instance = null;
    }
}
