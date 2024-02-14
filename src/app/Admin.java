package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.Player;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.user.pages.*;
import app.utils.Enums;
import fileio.input.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * The type Admin.
 */
public final class Admin {
    @Getter
    private List<User> users = new ArrayList<>();
    private List<Song> songs = new ArrayList<>();
    private List<Podcast> podcasts = new ArrayList<>();
    @Getter
    private final List<Artist> artists = new ArrayList<>();
    @Getter
    private final List<Album> albums = new ArrayList<>();
    @Getter
    private final List<Host> hosts = new ArrayList<>();
    private int timestamp = 0;
    private final int LIMIT = 5;

    private static Admin instance = null;

    private Admin() {
    }

    /**
     * Gets instance.
     *
     * @return the admin
     */
    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }


    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                                         episodeInput.getDuration(),
                                         episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    public List<Song> getSongList() {
        return songs;
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }
    public List<Podcast> getPodcastList() {
        return podcasts;
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets a user from the list of users.
     *
     * @param username the username
     * @return the user
     */
    public User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Gets an artist from the list of artists.
     *
     * @param username the username
     * @return the artist
     */
    public Artist getArtist(final String username) {
        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                return artist;
            }
        }
        return null;
    }

    /**
     * Gets a host from the list of hosts.
     *
     * @param username the username
     * @return the host
     */
    public Host getHost(final String username) {
        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                return host;
            }
        }
        return null;
    }

    /**
     * Gets a podcast from the list of podcasts.
     *
     * @param name the name
     * @return the podcast
     */
    public Podcast getPodcast(final String name) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(name)) {
                return podcast;
            }
        }
        return null;
    }

    /**
     * Gets album from the list of albums.
     *
     * @param name the name
     * @return the album
     */
    public Album getAlbum(final String name) {
        for (Album album : albums) {
            if (album.getName().equals(name)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            if (user.getUserStatus() == Enums.UserStatus.ONLINE) {
                user.simulateTime(elapsed);
            }
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Gets online users.
     *
     * @return online users
     */
    public List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getUserStatus() == Enums.UserStatus.ONLINE) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }

    /**
     * Adds user.
     *
     * @param username the username
     * @param type the type
     * @param age the age
     * @param city the city
     * @return the string
     */
    public String addUser(final String username, final String type, final int age,
                                                                            final String city) {
        if (getUser(username) != null || getHost(username) != null
                                        || getArtist(username) != null) {
            return "The username " + username + " is already taken.";
        }

        switch (type) {
            case "artist":
                artists.add(new Artist(username, age, city));
                break;
            case "host":
                hosts.add(new Host(username, age, city));
                break;
            case "user":
                users.add(new User(username, age, city));
                break;
            default:
                return null;
        }
        return "The username " + username + " has been added successfully.";
    }

    /**
     * Prints the current page.
     *
     * @param username the username
     * @return the string from specific methods
     */
    public String printCurrentPage(final String username) {
        User user = getUser(username);

        if (user.getUserStatus() == Enums.UserStatus.OFFLINE) {
            return user.getUsername() + " is offline.";
        }

        PageFactory pageFactory = new PageFactory();
        return pageFactory.printPage(user);
    }

    /**
     * Gets all users.
     *
     * @return the string list
     */
    public List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();
        for (User user : users) {
            allUsers.add(user.getUsername());
        }

        for (Artist artist : artists) {
            allUsers.add(artist.getUsername());
        }

        for (Host host : hosts) {
            allUsers.add(host.getUsername());
        }

        return allUsers;
    }

    /**
     * Deletes artist.
     *
     * @param username the username
     * @return the string
     */
    public String deleteArtist(final String username) {
        for (User user : users) {
            Player player = user.getPlayer();

            if (user.getCurrentPage() == Enums.CurrentPage.ARTIST
                    && user.getNameArtistPage().equals(username)) {
                return username + " can't be deleted.";
            }

            if (player.getCurrentAudioFile() == null) {
                continue;
            }

            if (player.getSource().getType() == Enums.PlayerSourceType.LIBRARY
                    && player.getSource().getAudioFile().matchesArtist(username)) {
                return username + " can't be deleted.";
            }

            if (player.getSource().getType() == Enums.PlayerSourceType.PLAYLIST) {
                ArrayList<Song> playlistSongs = ((Playlist) player.getSource().
                                                                getAudioCollection()).getSongs();
                for (Song iterSong : playlistSongs) {
                    if (iterSong.matchesArtist(username)) {
                        return username + " can't be deleted.";
                    }
                }
            }

            if (player.getSource().getType() == Enums.PlayerSourceType.ALBUM
                    && player.getSource().getAudioCollection().getOwner().equals(username)) {
                return username + " can't be deleted.";
            }
        }

        Artist artist = getArtist(username);

        for (Album album : artist.getAlbums()) {
            for (Song song : album.getSongs()) {
                songs.remove(song);

                for (User user : users) {
                    user.getLikedSongs().remove(song);
                }
            }
            albums.remove(album);
        }
        artists.remove(artist);

        return username + " was successfully deleted.";
    }

    /**
     * Deletes host.
     *
     * @param username the username
     * @return the string
     */
    public String deleteHost(final String username) {
        for (User iterUser : users) {
            Player player = iterUser.getPlayer();

            if (iterUser.getCurrentPage() == Enums.CurrentPage.HOST
                    && iterUser.getNameHostPage().equals(username)) {
                return username + " can't be deleted.";
            }

            if (player.getCurrentAudioFile() == null) {
                continue;
            }

            if (player.getSource().getType() == Enums.PlayerSourceType.PODCAST
                    && player.getSource().getAudioCollection().matchesOwner(username)) {
                return username + " can't be deleted.";
            }
        }

        Host host = getHost(username);

        for (Podcast podcast : host.getPodcasts()) {
            podcasts.remove(podcast);
        }

        hosts.remove(host);

        return username + " was successfully deleted.";
    }

    /**
     * Deletes standard user.
     *
     * @param username the username
     * @return the string
     */
    public String deleteStandardUser(final String username) {
        User user = getUser(username);

        for (User iterUser : users) {
            Player player = iterUser.getPlayer();

            if (player.getCurrentAudioFile() == null) {
                continue;
            }

            if (player.getSource().getType() == Enums.PlayerSourceType.PLAYLIST
                    && player.getSource().getAudioCollection().matchesOwner(username)) {
                return username + " can't be deleted.";
            }
        }

        for (Playlist playlist : user.getFollowedPlaylists()) {
            playlist.setFollowers(playlist.getFollowers() - 1);
        }

        for (Song song : user.getLikedSongs()) {
            song.dislike();
        }

        for (User iterUser : users) {
            for (Playlist iterPlaylistToRemove : user.getPlaylists()) {
                iterUser.getFollowedPlaylists().remove(iterPlaylistToRemove);
            }
        }

        users.remove(user);

        return username + " was successfully deleted.";
    }

    /**
     * Deletes user.
     *
     * @param username the username
     * @return the string
     */
    public String deleteUser(final String username) {
        if (getArtist(username) != null) {
            return deleteArtist(username);
        }

        if (getUser(username) != null) {
            return deleteStandardUser(username);
        }

        if (getHost(username) != null) {
            return deleteHost(username);
        }

        return "The username " + username + " doesn't exist";
    }

    /**
     * Calculates the sum of song likes on an album.
     *
     * @param album the album
     */
    public void calculateAlbumLikes(final Album album) {
        int likes = 0;

        for (Song song : album.getSongs()) {
            likes += song.getLikes();
        }

        album.setAlbumLikes(likes);
    }

    /**
     * Gets top 5 albums.
     *
     * @return the string list
     */
    public List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(albums);
        List<String> topAlbums = new ArrayList<>();

        for (Album album : sortedAlbums) {
            calculateAlbumLikes(album);
        }

        sortedAlbums.sort(Comparator.comparingInt(Album::getAlbumLikes)
                                                    .reversed().thenComparing(Album::getName));

        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= LIMIT) {
                break;
            }

            topAlbums.add(album.getName());
            count++;
        }

        return topAlbums;
    }

    /**
     * Calculates the sum of song likes on all albums of an artist.
     *
     * @param artist the artist
     */
    public void calculateArtistAllLikes(final Artist artist) {
        int allLikes = 0;

        for (Album album : artist.getAlbums()) {
            calculateAlbumLikes(album);
            allLikes += album.getAlbumLikes();
        }

        artist.setAllLikes(allLikes);
    }

    /**
     * Gets top 5 artists.
     *
     * @return the string list
     */
    public List<String> getTop5Artists() {
        List<Artist> sortedArtists = new ArrayList<>(artists);
        List<String> topArtists = new ArrayList<>();

        for (Artist artist : sortedArtists) {
            calculateArtistAllLikes(artist);
        }

        sortedArtists.sort(Comparator.comparingInt(Artist::getAllLikes)
                                                .reversed().thenComparing(Artist::getUsername));

        int count = 0;
        for (Artist artist : sortedArtists) {
            if (count >= LIMIT) {
                break;
            }

            topArtists.add(artist.getUsername());
            count++;
        }

        return topArtists;
    }

    /**
     * Reset.
     */
    public void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
