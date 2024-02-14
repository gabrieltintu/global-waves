package app.user.pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;

import java.util.ArrayList;
import java.util.Comparator;

public class HomePage implements Page {
    private final ArrayList<String> songs = new ArrayList<>();
    private final ArrayList<String> playlists = new ArrayList<>();
    private static final Integer MAX_RESULTS = 5;

    /**
     * Calculates the sum of song likes on a playlist.
     *
     * @param playlist the album
     */
    public static void calculatePlaylistLikes(final Playlist playlist) {
        int likes = 0;

        for (Song song : playlist.getSongs()) {
            likes += song.getLikes();
        }

        playlist.setPlaylistLikes(likes);
    }

    public HomePage(final ArrayList<Song> likedSongs,
                                            final ArrayList<Playlist> followedPlaylists) {
        ArrayList<Song> likedSongsCopy = new ArrayList<>(likedSongs);
        likedSongsCopy.sort(Comparator.comparingInt(Song::getLikes).reversed());

        for (Song song : likedSongsCopy) {
            this.songs.add(song.getName());
        }

        while (songs.size() > MAX_RESULTS) {
            songs.remove(songs.size() - 1);
        }

        ArrayList<Playlist> followedPlaylistsCopy = new ArrayList<>(followedPlaylists);

        for (Playlist playlist : followedPlaylistsCopy) {
            calculatePlaylistLikes(playlist);
        }

        followedPlaylistsCopy.sort(Comparator.comparingInt(Playlist::getPlaylistLikes).reversed());

        for (Playlist playlist : followedPlaylistsCopy) {
            this.playlists.add(playlist.getName());
        }

        while (playlists.size() > MAX_RESULTS) {
            playlists.remove(playlists.size() - 1);
        }
    }

    /**
     * Prints the home page.
     *
     * @return the string
     */
    @Override
    public String printCurrentPage() {
        return "Liked songs:\n"
                + "\t" + songs + "\n"
                + "\nFollowed playlists:\n"
                + "\t" + playlists;
    }
}
