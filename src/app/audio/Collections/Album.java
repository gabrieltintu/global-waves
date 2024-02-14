package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Album extends AudioCollection {
    private final int releaseYear;
    private final  String description;
    private final ArrayList<Song> songs;
    private int albumLikes = 0;
    public void setAlbumLikes(final int albumLikes) {
        this.albumLikes = albumLikes;
    }

    public Album(final String name, final String owner, final int releaseYear,
                                        final String description, final ArrayList<Song> songs) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.description = description;
        this.songs = songs;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }
}
