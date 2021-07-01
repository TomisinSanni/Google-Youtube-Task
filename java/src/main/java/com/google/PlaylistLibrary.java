/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Tomisin
 */
public class PlaylistLibrary {

    private final List<Playlist> playlists;

    PlaylistLibrary() {
        this.playlists = new ArrayList<>();
    }

    
    
    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public Playlist getPlaylist(String playlistName) {
        int index = 0;
        for (int i = 0; i < playlists.size(); i++) {
            Playlist pl = playlists.get(i);
            if(pl.getName().equalsIgnoreCase(playlistName)){
                index = i;
            }
        }
        return playlists.get(index);
  }
    
    public void removePlaylist(String playlistName) {
        int index = 0;
        for (int i = 0; i < playlists.size(); i++) {
            Playlist pl = playlists.get(i);
            if(pl.getName().equalsIgnoreCase(playlistName)){
                index = i;
            }
        }
          playlists.remove(index);
    }
}
