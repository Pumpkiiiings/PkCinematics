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
        int maxCameraTick = cameraTrack.getKeyframes().stream()
                .mapToInt(CameraKeyframe::getTick)
                .max()
                .orElse(0);
        int maxActionTick = actionTrack.getAllActions().keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
        this.durationTicks = Math.max(maxCameraTick, maxActionTick);
    }

    public CameraTrack getCameraTrack() {
        return cameraTrack;
    }

    public ActionTrack getActionTrack() {
        return actionTrack;
    }
}
