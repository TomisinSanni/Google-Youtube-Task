/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tomisin
 */
class Playlist {

    private final String name;    
    private final List<Video> videos;
    
    public Playlist(String name){
        this.name = name;
        this.videos = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Video> getVideos() {
        return videos;
    }
    
    
}
