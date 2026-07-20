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
        
        // Get surrounding points for Catmull-Rom
        int prevIndex = keyframes.indexOf(prev);
        int nextIndex = keyframes.indexOf(next);
        CameraKeyframe p0 = prevIndex > 0 ? keyframes.get(prevIndex - 1) : prev;
        CameraKeyframe p3 = nextIndex < keyframes.size() - 1 ? keyframes.get(nextIndex + 1) : next;
        
        // Calculate progress (0.0 to 1.0)
        double progress = (double) (tick - prev.getTick()) / (next.getTick() - prev.getTick());
        
        // Apply Easing
        double t = applyEasing(progress, prev.getEasingType());
        
        double x, y, z;
        float yaw, pitch, fov;
        
        if ("CATMULL_ROM".equalsIgnoreCase(prev.getInterpolationType())) {
            x = catmullRom(p0.getX(), prev.getX(), next.getX(), p3.getX(), t);
            y = catmullRom(p0.getY(), prev.getY(), next.getY(), p3.getY(), t);
            z = catmullRom(p0.getZ(), prev.getZ(), next.getZ(), p3.getZ(), t);
            yaw = catmullRomAngle(p0.getYaw(), prev.getYaw(), next.getYaw(), p3.getYaw(), t);
            pitch = catmullRomAngle(p0.getPitch(), prev.getPitch(), next.getPitch(), p3.getPitch(), t);
            fov = (float) catmullRom(p0.getFov(), prev.getFov(), next.getFov(), p3.getFov(), t);
        } else {
            // LINEAR path interpolation
            x = prev.getX() + (next.getX() - prev.getX()) * t;
            y = prev.getY() + (next.getY() - prev.getY()) * t;
            z = prev.getZ() + (next.getZ() - prev.getZ()) * t;
            
            // Linear angle requires shortest path
            yaw = prev.getYaw() + normalizeAngle(next.getYaw() - prev.getYaw()) * (float) t;
            pitch = prev.getPitch() + normalizeAngle(next.getPitch() - prev.getPitch()) * (float) t;
            fov = prev.getFov() + (next.getFov() - prev.getFov()) * (float) t;
        }
        
        return new CameraKeyframe(tick, prev.getWorldName(), x, y, z, yaw, pitch, fov, prev.getInterpolationType(), prev.getEasingType());
    }

    private double applyEasing(double t, String type) {
        if (type == null) return t;
        switch (type.toUpperCase()) {
            case "EASE_IN": return t * t;
            case "EASE_OUT": return t * (2 - t);
            case "SMOOTH": return t * t * (3 - 2 * t);
            case "LINEAR":
            default: return t;
        }
    }

    private double catmullRom(double p0, double p1, double p2, double p3, double t) {
        double t2 = t * t;
        double t3 = t2 * t;
        return 0.5 * (
            (2 * p1) +
            (-p0 + p2) * t +
            (2 * p0 - 5 * p1 + 4 * p2 - p3) * t2 +
            (-p0 + 3 * p1 - 3 * p2 + p3) * t3
        );
    }

    private float normalizeAngle(float angle) {
        angle = angle % 360;
        if (angle < -180) angle += 360;
        if (angle > 180) angle -= 360;
        return angle;
    }

    private float catmullRomAngle(float p0, float p1, float p2, float p3, double t) {
        p0 = p1 + normalizeAngle(p0 - p1);
        p2 = p1 + normalizeAngle(p2 - p1);
        p3 = p2 + normalizeAngle(p3 - p2);
        return (float) catmullRom(p0, p1, p2, p3, t);
    }
}
