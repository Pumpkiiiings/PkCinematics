package com.pumpkiiiings.pkcinematics.model.timeline;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CameraTrack {
    private final List<CameraKeyframe> keyframes;

    public CameraTrack() {
        this.keyframes = new ArrayList<>();
    }

    public void addKeyframe(CameraKeyframe keyframe) {
        this.keyframes.add(keyframe);
        this.keyframes.sort(new Comparator<CameraKeyframe>() {
            @Override
            public int compare(CameraKeyframe a, CameraKeyframe b) {
                return Integer.compare(a.getTick(), b.getTick());
            }
        });
    }
    
    public void removeKeyframe(CameraKeyframe keyframe) {
        this.keyframes.remove(keyframe);
    }

    public List<CameraKeyframe> getKeyframes() {
        return keyframes;
    }
    
    /**
     * Gets the interpolated point for a specific tick.
     */
    public CameraKeyframe getInterpolatedPoint(int tick) {
        if (keyframes.isEmpty()) return null;
        if (keyframes.size() == 1) return keyframes.get(0);
        
        CameraKeyframe prev = null;
        CameraKeyframe next = null;
        
        for (CameraKeyframe kf : keyframes) {
            if (kf.getTick() <= tick) {
                prev = kf;
            }
            if (kf.getTick() > tick && next == null) {
                next = kf;
            }
        }
        
        if (prev == null) return keyframes.get(0);
        if (next == null) return prev; // Reached the end
        
        // Calculate progress (0.0 to 1.0)
        double progress = (double) (tick - prev.getTick()) / (next.getTick() - prev.getTick());
        
        // Simple linear interpolation for now
        // In the future, this would use prev.getInterpolationType() and maybe Bezier/Catmull
        double x = prev.getX() + (next.getX() - prev.getX()) * progress;
        double y = prev.getY() + (next.getY() - prev.getY()) * progress;
        double z = prev.getZ() + (next.getZ() - prev.getZ()) * progress;
        
        float yaw = prev.getYaw() + (next.getYaw() - prev.getYaw()) * (float) progress;
        float pitch = prev.getPitch() + (next.getPitch() - prev.getPitch()) * (float) progress;
        float fov = prev.getFov() + (next.getFov() - prev.getFov()) * (float) progress;
        
        return new CameraKeyframe(tick, prev.getWorldName(), x, y, z, yaw, pitch, fov, "LINEAR");
    }
}
