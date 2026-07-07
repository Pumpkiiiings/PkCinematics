package com.pumpkiiiings.pkcinematics.model;

import com.pumpkiiiings.pkcinematics.model.timeline.Timeline;

public class Cinematic {
    private final String id;
    private final Timeline timeline;

    public Cinematic(String id) {
        this.id = id;
        this.timeline = new Timeline();
    }

    public String getId() {
        return id;
    }

    public Timeline getTimeline() {
        return timeline;
    }
}
