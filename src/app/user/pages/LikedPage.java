package app.user.pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;

import java.util.ArrayList;

public class LikedPage implements Page {
    private final ArrayList<String> songs = new ArrayList<>();
    private final ArrayList<String> playlists = new ArrayList<>();

    public LikedPage(final ArrayList<Song> likedSongs,
                                            final ArrayList<Playlist> followedPlaylists) {
        ArrayList<Song> likedSongsCopy = new ArrayList<>(likedSongs);

        for (Song song : likedSongsCopy) {
            this.songs.add(song.getName() + " - " + song.getArtist());
        }

        ArrayList<Playlist> followedPlaylistsCopy = new ArrayList<>(followedPlaylists);

        for (Playlist playlist : followedPlaylistsCopy) {
            this.playlists.add(playlist.getName() + " - " + playlist.getOwner());
        }

    }

    /**
     * Prints the liked content page.
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
