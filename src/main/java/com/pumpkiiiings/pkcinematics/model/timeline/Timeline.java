package com.pumpkiiiings.pkcinematics.model.timeline;

import java.util.ArrayList;
import java.util.List;

public class Timeline {
    private int durationTicks;
    private final CameraTrack cameraTrack;
    private final ActionTrack actionTrack;

    public Timeline() {
        this.cameraTrack = new CameraTrack();
        this.actionTrack = new ActionTrack();
        this.durationTicks = 0;
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public void setDurationTicks(int durationTicks) {
        this.durationTicks = durationTicks;
    }
    
    public void calculateDuration() {
        int maxCameraTick = 0;
        for (CameraKeyframe kf : cameraTrack.getKeyframes()) {
            if (kf.getTick() > maxCameraTick) maxCameraTick = kf.getTick();
        }
        int maxActionTick = 0;
        for (Integer t : actionTrack.getAllActions().keySet()) {
            if (t > maxActionTick) maxActionTick = t;
        }
        this.durationTicks = Math.max(maxCameraTick, maxActionTick);
    }

    public CameraTrack getCameraTrack() {
        return cameraTrack;
    }

    public ActionTrack getActionTrack() {
        return actionTrack;
    }
}
