package com.google;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.apache.maven.shared.utils.StringUtils;

public class VideoPlayer {

    private final VideoLibrary videoLibrary;
    private final PlaylistLibrary playlists;
    String videoPlaying = "NONE";
    String videoPaused = "NONE";

    public VideoPlayer() {
        this.videoLibrary = new VideoLibrary();
        this.playlists = new PlaylistLibrary();

    }

    public void numberOfVideos() {
        System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
    }

    public void showAllVideos() {
        System.out.println("Here's a list of all available videos:");

        //put titles in array arranged lexicographically
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < videoLibrary.getVideos().size(); i++) {
            titles.add(videoLibrary.getVideos().get(i).getTitle());
        }
        Collections.sort(titles); //sort it

        //now print each line according to titles array
        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            for (int j = 0; j < videoLibrary.getVideos().size(); j++) {
                if (videoLibrary.getVideos().get(j).getTitle().equalsIgnoreCase(title)) {
                    System.out.print("    " + videoLibrary.getVideos().get(j).getTitle()
                            + " (" + videoLibrary.getVideos().get(j).getVideoId() + ") "
                            + videoLibrary.getVideos().get(j).getTags().toString().replaceAll(",", ""));
                    if (videoLibrary.getVideos().get(j).isFlagged()) {
                        System.out.println(" - FLAGGED (reason: " + videoLibrary.getVideos().get(j).getReason() + ")");
                    } else {
                        System.out.println("");
                    }
                }
            }
        }
    }

    public void playVideo(String videoId) {

        if (videoExists(videoId) == true) {
            //store chosen video
            String chosenVideoTitle = videoLibrary.getVideo(videoId).getTitle();

            if (videoLibrary.getVideo(videoId).isFlagged()) {
                System.out.println("Cannot play video: Video is currently flagged (reason: " + videoLibrary.getVideo(videoId).getReason() + ")");
            } else {
                //check if video is playing now
                if (videoPlaying.equalsIgnoreCase("NONE")) {
                    System.out.println("Playing video: " + chosenVideoTitle);
                    videoPaused = "NONE";
                    videoPlaying = chosenVideoTitle;
                } else {
                    System.out.println("Stopping video: " + videoPlaying);
                    System.out.println("Playing video: " + chosenVideoTitle);
                    videoPlaying = chosenVideoTitle;
                    videoPaused = "NONE";
                }
            }
        } else {
            System.out.println("Cannot play video: Video does not exist");
        }

    }

    public void stopVideo() {
        if (videoPlaying.equalsIgnoreCase("NONE")) {
            System.out.println("Cannot stop video: No video is currently playing");
        } else {
            System.out.println("Stopping video: " + videoPlaying);
            videoPlaying = "NONE";

        }

    }

    public void playRandomVideo() {
        int noOfAvailableVideos = 0;

        for (int i = 0; i < videoLibrary.getVideos().size(); i++) {
            Video video = videoLibrary.getVideos().get(i);
            if (!video.isFlagged()) {
                noOfAvailableVideos++;
            }
        }

        if (noOfAvailableVideos > 0) {
            int random = generateRandomNumber(0, noOfAvailableVideos);
            String chosenVideoTitle = videoLibrary.getVideos().get(random).getTitle();

            //check if video is playing now
            if (videoPlaying.equalsIgnoreCase("NONE")) {
                System.out.println("Playing video: " + chosenVideoTitle);
                videoPaused = "NONE";
            } else {
                System.out.println("Stopping video: " + videoPlaying);
                System.out.println("Playing video: " + chosenVideoTitle);
                videoPlaying = chosenVideoTitle;
                videoPaused = "NONE";
            }
        } else {
            System.out.println("No videos available");
        }
    }

    public void pauseVideo() {
        if (videoPlaying.equalsIgnoreCase("NONE")) {
            System.out.println("Cannot pause video: No video is currently playing");
        } else {
            if (videoPaused.equalsIgnoreCase("NONE")) {
                System.out.println("Pausing video: " + videoPlaying);
                videoPaused = videoPlaying;
            } else {
                System.out.println("Video already paused: " + videoPaused);
            }
        }
    }

    public void continueVideo() {
        if (videoPlaying.equalsIgnoreCase("NONE")) {
            System.out.println("Cannot continue video: No video is currently playing");
        } else {
            if (videoPaused.equalsIgnoreCase("NONE")) {
                System.out.println("Cannot continue video: Video is not paused");
            } else {
                System.out.println("Continuing video: " + videoPaused);
                videoPaused = "NONE";
            }
        }
    }

    public void showPlaying() {
        if (videoPlaying.equalsIgnoreCase("NONE")) {
            System.out.println("No video is currently playing");
        } else {
            for (int i = 0; i < videoLibrary.getVideos().size(); i++) {
                if (videoLibrary.getVideos().get(i).getTitle().equalsIgnoreCase(videoPlaying)) {
                    System.out.print("Currently playing: " + videoLibrary.getVideos().get(i).getTitle()
                            + " (" + videoLibrary.getVideos().get(i).getVideoId() + ") "
                            + videoLibrary.getVideos().get(i).getTags().toString().replaceAll(",", ""));

                    //add paused label
                    if (videoPaused.equalsIgnoreCase(videoLibrary.getVideos().get(i).getTitle())) {
                        System.out.println(" - PAUSED");
                    }
                }
            }
        }
    }

    public void createPlaylist(String playlistName) {
        if (playlistExists(playlistName) == true) {
            System.out.println("Cannot create playlist: A playlist with the same name already exists");
        } else {
            playlists.getPlaylists().add(new Playlist(playlistName));
            System.out.println("Successfully created new playlist: " + playlistName);
        }
    }

    public void addVideoToPlaylist(String playlistName, String videoId) {
        Video chosenVideo = videoLibrary.getVideo(videoId);
        if (playlistExists(playlistName) == true) {
            if (videoExists(videoId) == true) {
                if (videoLibrary.getVideo(videoId).isFlagged()) {
                    System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: "
                            + videoLibrary.getVideo(videoId).getReason() + ")");
                } else {
                    if (videoInPlaylist(videoId, playlistName) == true) {
                        System.out.println("Cannot add video to " + playlistName + ": Video already added");
                    } else {
                        playlists.getPlaylist(playlistName).getVideos().add(chosenVideo);
                        System.out.println("Added video to " + playlistName + ": " + chosenVideo.getTitle());
                    }
                }
            } else {
                System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
            }
        } else {
            System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
        }
    }

    public void showAllPlaylists() {
        if (playlists.getPlaylists().isEmpty()) {
            System.out.println("No playlists exist yet");
        } else {
            System.out.println("Showing all playlists:");

            List<String> titles = new ArrayList<>();
            for (int i = 0; i < playlists.getPlaylists().size(); i++) {
                titles.add(playlists.getPlaylists().get(i).getName());
            }
            Collections.sort(titles);

            for (int i = 0; i < titles.size(); i++) {
                System.out.println(titles.get(i));
            }
        }
    }

    public void showPlaylist(String playlistName) {
        if (playlistExists(playlistName) == true) {
            if (playlists.getPlaylist(playlistName).getVideos().isEmpty()) {
                System.out.println("Showing playlist: " + playlistName);
                System.out.println("    No videos here yet");
            } else {
                System.out.println("Showing playlist: " + playlistName);
                for (int i = 0; i < playlists.getPlaylist(playlistName).getVideos().size(); i++) {
                    Video video = playlists.getPlaylist(playlistName).getVideos().get(i);
                    System.out.print(video.getTitle()
                            + " (" + video.getVideoId() + ") "
                            + video.getTags().toString().replaceAll(",", ""));
                    if (video.isFlagged()) {
                        System.out.println(" - FLAGGED (reason: " + video.getReason() + ")");
                    } else {
                        System.out.println("");
                    }
                }
            }
        } else {
            System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
        }
    }

    public void removeFromPlaylist(String playlistName, String videoId) {
        if (playlistExists(playlistName) == true) {
            if (videoExists(videoId) == true) {
                if (videoInPlaylist(videoId, playlistName) == true) {
                    Video chosenVideo = videoLibrary.getVideo(videoId);
                    playlists.getPlaylist(playlistName).getVideos().remove(chosenVideo);
                    System.out.println("Removed video from " + playlistName + ": " + chosenVideo.getTitle());
                } else {
                    System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
                }
            } else {
                System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
            }
        } else {
            System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
        }
    }

    public void clearPlaylist(String playlistName) {
        if (playlistExists(playlistName) == true) {
            playlists.getPlaylist(playlistName).getVideos().clear();
            System.out.println("Successfully removed all videos from " + playlistName);
        } else {
            System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
        }
    }

    public void deletePlaylist(String playlistName) {
        if (playlistExists(playlistName) == true) {
            playlists.removePlaylist(playlistName);
            System.out.println("Deleted playlist: " + playlistName);

        } else {
            System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
        }

    }

    public void searchVideos(String searchTerm) {
        //put titles that contain search term in array arranged lexicographically
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < videoLibrary.getVideos().size(); i++) {
            //compare both in lower case
            if (!videoLibrary.getVideos().get(i).isFlagged()) {
                if (videoLibrary.getVideos().get(i).getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                    titles.add(videoLibrary.getVideos().get(i).getTitle());
                }
            }
        }
        Collections.sort(titles);

        String result = "";
        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            for (int j = 0; j < videoLibrary.getVideos().size(); j++) {
                if (videoLibrary.getVideos().get(j).getTitle().equalsIgnoreCase(title)) {
                    String temp = "\n    " + (i + 1) + ") " + videoLibrary.getVideos().get(j).getTitle() + " (" + videoLibrary.getVideos().get(j).getVideoId() + ") " + videoLibrary.getVideos().get(j).getTags().toString().replaceAll(",", "");
                    result += temp;
                }
            }
        }

        if (titles.size() > 0) {
            System.out.print("Here are the results for " + searchTerm + ":");
            System.out.println(result);
            System.out.println("Would you like to play any of the above? If yes, specify the number of the video.\n"
                    + "If your answer is not a valid number, we will assume it's a no.");
            Scanner sc = new Scanner(new InputStreamReader(System.in));
            try {
                int value = Integer.parseInt(sc.nextLine());
                if (value < (titles.size() + 1)) {
                    if (value > 0) {
                        for (int j = 0; j < videoLibrary.getVideos().size(); j++) {
                            if (videoLibrary.getVideos().get(j).getTitle().equalsIgnoreCase(titles.get(value - 1))) {
                                playVideo(videoLibrary.getVideos().get(j).getVideoId());
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }

        } else {
            System.out.println("No search results for " + searchTerm);
        }

    }

    public void searchVideosWithTag(String videoTag) {
//put titles that contain search term in array arranged lexicographically
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < videoLibrary.getVideos().size(); i++) {
            if (!videoLibrary.getVideos().get(i).isFlagged()) {
                //compare both in lower case
                if (videoLibrary.getVideos().get(i).getTags().toString().toLowerCase().contains(videoTag.toLowerCase())) {
                    titles.add(videoLibrary.getVideos().get(i).getTitle());
                }
            }
        }
        Collections.sort(titles);

        String result = "";
        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            for (int j = 0; j < videoLibrary.getVideos().size(); j++) {
                if (videoLibrary.getVideos().get(j).getTitle().equalsIgnoreCase(title)) {
                    String temp = "\n    " + (i + 1) + ") " + videoLibrary.getVideos().get(j).getTitle() + " (" + videoLibrary.getVideos().get(j).getVideoId() + ") " + videoLibrary.getVideos().get(j).getTags().toString().replaceAll(",", "");
                    result += temp;
                }
            }
        }

        if (titles.size() > 0) {
            System.out.print("Here are the results for " + videoTag + ":");
            System.out.println(result);
            System.out.println("Would you like to play any of the above? If yes, specify the number of the video.\n"
                    + "If your answer is not a valid number, we will assume it's a no.");
            Scanner sc = new Scanner(new InputStreamReader(System.in));
            try {
                int value = Integer.parseInt(sc.nextLine());
                if (value < (titles.size() + 1)) {
                    if (value > 0) {
                        for (int j = 0; j < videoLibrary.getVideos().size(); j++) {
                            if (videoLibrary.getVideos().get(j).getTitle().equalsIgnoreCase(titles.get(value - 1))) {
                                playVideo(videoLibrary.getVideos().get(j).getVideoId());
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }

        } else {
            System.out.println("No search results for " + videoTag);
        }
    }

    public void flagVideo(String videoId) {
        if (videoExists(videoId)) {
            if (videoLibrary.getVideo(videoId).isFlagged()) {
                System.out.println("Cannot flag video: Video is already flagged");
            } else {
                videoLibrary.getVideo(videoId).setFlagged(true);
                if (videoPlaying.equalsIgnoreCase(videoLibrary.getVideo(videoId).getTitle())) {
                    stopVideo();
                }
                if (videoPaused.equalsIgnoreCase(videoLibrary.getVideo(videoId).getTitle())) {
                    System.out.println("Stopping video: " + videoPaused);
                    videoPlaying = "NONE";
                    videoPaused = "NONE";
                }
                System.out.println("Successfully flagged video: " + videoLibrary.getVideo(videoId).getTitle() + " (reason: " + videoLibrary.getVideo(videoId).getReason() + ")");
            }
        } else {
            System.out.println("Cannot flag video: Video does not exist");
        }
    }

    public void flagVideo(String videoId, String reason) {
        if (videoExists(videoId)) {
            if (videoLibrary.getVideo(videoId).isFlagged()) {
                System.out.println("Cannot flag video: Video is already flagged");
            } else {
                videoLibrary.getVideo(videoId).setFlagged(true, reason);
                if (videoPlaying.equalsIgnoreCase(videoLibrary.getVideo(videoId).getTitle())) {
                    stopVideo();
                }
                if (videoPaused.equalsIgnoreCase(videoLibrary.getVideo(videoId).getTitle())) {
                    videoPlaying = "NONE";
                    videoPaused = "NONE";
                }
                System.out.println("Successfully flagged video: " + videoLibrary.getVideo(videoId).getTitle() + " (reason: " + videoLibrary.getVideo(videoId).getReason() + ")");
            }
        } else {
            System.out.println("Cannot flag video: Video does not exist");
        }
    }

    public void allowVideo(String videoId) {
        if(videoExists(videoId)){
        if (videoLibrary.getVideo(videoId).isFlagged()) {
            videoLibrary.getVideo(videoId).setFlagged(false);
            System.out.println("Successfully removed flag from video: " + videoLibrary.getVideo(videoId).getTitle());
        } else {
            System.out.println("Cannot remove flag from video: Video is not flagged");
        }
        } else {
            System.out.println("Cannot remove flag from video: Video does not exist");
        }
    }

    //checking if a chosen video exists
    private boolean videoExists(String videoId) {
        boolean exists = false;
        for (int i = 0; i < videoLibrary.getVideos().size(); i++) {
            if (videoLibrary.getVideos().get(i).getVideoId().equalsIgnoreCase(videoId)) {
                exists = true;
            }
        }

        return exists;
    }

    //generate random number
    private int generateRandomNumber(int min, int max) {
        Random randomNum = new Random();
        int value = min + randomNum.nextInt(max);
        return value;
    }

    //checking if playlist already exists
    private boolean playlistExists(String playlistName) {
        boolean exists = false;
        for (int i = 0; i < playlists.getPlaylists().size(); i++) {
            if (playlists.getPlaylists().get(i).getName().equalsIgnoreCase(playlistName)) {
                exists = true;

            }
        }

        return exists;
    }

    //check if a video is already in a playlist
    private boolean videoInPlaylist(String videoId, String playlistName) {
        boolean exists = false;
        Playlist chosenPlaylist = playlists.getPlaylist(playlistName);

        for (int i = 0; i < chosenPlaylist.getVideos().size(); i++) {
            if (chosenPlaylist.getVideos().get(i).getVideoId().equalsIgnoreCase(videoId)) {
                exists = true;
            }

        }

        return exists;
    }
}
