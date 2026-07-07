package com.pumpkiiiings.pkcinematics.model.timeline;

public class CameraKeyframe {
    private int tick;
    private String worldName;
    private double x, y, z;
    private float yaw, pitch;
    private float fov;
    private String interpolationType; // e.g., "LINEAR"

    public CameraKeyframe() {}

    public CameraKeyframe(int tick, String worldName, double x, double y, double z, float yaw, float pitch, float fov, String interpolationType) {
        this.tick = tick;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.fov = fov;
        this.interpolationType = interpolationType;
    }

    public int getTick() { return tick; }
    public void setTick(int tick) { this.tick = tick; }

    public String getWorldName() { return worldName; }
    public void setWorldName(String worldName) { this.worldName = worldName; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getZ() { return z; }
    public void setZ(double z) { this.z = z; }

    public float getYaw() { return yaw; }
    public void setYaw(float yaw) { this.yaw = yaw; }

    public float getPitch() { return pitch; }
    public void setPitch(float pitch) { this.pitch = pitch; }

    public float getFov() { return fov; }
    public void setFov(float fov) { this.fov = fov; }

    public String getInterpolationType() { return interpolationType; }
    public void setInterpolationType(String interpolationType) { this.interpolationType = interpolationType; }
}
