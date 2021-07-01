package com.google;

import java.util.Collections;
import java.util.List;

/**
 * A class used to represent a video.
 */
class Video {

    private final String title;
    private final String videoId;
    private final List<String> tags;
    private boolean flagged;
    private String reason;

    Video(String title, String videoId, List<String> tags, boolean flagged, String reason) {
        this.title = title;
        this.videoId = videoId;
        this.tags = Collections.unmodifiableList(tags);
        this.flagged = flagged;
        this.reason = reason;
    }

    /**
     * Returns the title of the video.
     */
    String getTitle() {
        return title;
    }

    /**
     * Returns the video id of the video.
     */
    String getVideoId() {
        return videoId;
    }

    /**
     * Returns a readonly collection of the tags of the video.
     */
    List<String> getTags() {
        return tags;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public String getReason() {
        return reason;
    }

    public void setFlagged(boolean flagged, String reason) {
        this.flagged = flagged;
        this.reason = reason;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
}
