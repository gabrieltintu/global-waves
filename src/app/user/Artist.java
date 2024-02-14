package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.player.Player;
import app.user.userUtils.Event;
import app.user.userUtils.Merch;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class Artist extends User {
    private final ArrayList<Album> albums;
    private final ArrayList<Event> events;
    private final ArrayList<Merch> merchArticles;
    private int allLikes = 0;
    private static final Integer MAX_YEAR = 2023;
    private static final Integer MIN_YEAR = 1900;
    private static final Integer MAX_MONTH = 12;
    private static final Integer MAX_DAY = 31;
    private static final Integer FEB_MONTH = 2;
    private static final Integer FEB_MAX_DAY = 28;


    public void setAllLikes(final int allLikes) {
        this.allLikes = allLikes;
    }

    /**
     * Instantiates a new artist.
     *
     * @param username the username
     * @param age the age
     * @param city the city
     */
    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        this.events = new ArrayList<>();
        this.merchArticles = new ArrayList<>();
        this.albums = new ArrayList<>();
    }

    /**
     * Gets album.
     *
     * @param name the album name
     * @return the album or null if album does not exist
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
     * Gets event.
     *
     * @param name the event name
     * @return the event or null if event does not exist
     */
    public Event getEvent(final String name) {
        for (Event event : events) {
            if (event.getName().equals(name)) {
                return event;
            }
        }
        return null;
    }

    /**
     * Gets merch.
     *
     * @param name the merch name
     * @return the merch or null if merch does not exist
     */
    public Merch getMerch(final String name) {
        for (Merch merch : merchArticles) {
            if (merch.getName().equals(name)) {
                return merch;
            }
        }
        return null;
    }

    /**
     * Adds an album.
     *
     * @param songs the songs in the album
     * @param name the name of the album
     * @param username the username
     * @param releaseYear the release year
     * @param description the description
     * @return the string
     */
    public static String addAlbum(final ArrayList<Song> songs, final String name,
                                  final String username, final int releaseYear,
                                  final String description) {
        if (Admin.getInstance().getHost(username) != null
                || Admin.getInstance().getUser(username) != null) {
            return username + " is not an artist.";
        }

        if (Admin.getInstance().getArtist(username) == null) {
            return "The username " + username + " doesn't exist.";
        }

        Artist artist = Admin.getInstance().getArtist(username);

        if (artist.getAlbum(name) != null) {
            return username + " has another album with the same name.";
        }

        Map<String, Integer> frequencyCounter = new HashMap<>();

        for (Song song : songs) {
            frequencyCounter.put(song.getName(),
                                    frequencyCounter.getOrDefault(song.getName(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : frequencyCounter.entrySet()) {
            if (entry.getValue() >= 2) {
                return username + " has the same song at least twice in this album.";
            }
        }

        Admin.getInstance().getAlbums().add(new Album(name, username, releaseYear,
                                                                            description, songs));
        artist.getAlbums().add(new Album(name, username, releaseYear, description, songs));

        return username + " has added new album successfully.";
    }

    /**
     * Removes album.
     *
     * @param username the username
     * @param name the name
     * @return the string
     */
    public static String removeAlbum(final String username, final String name) {
        if (Admin.getInstance().getHost(username) != null
                || Admin.getInstance().getUser(username) != null) {
            return username + " is not an artist.";
        }

        if (Admin.getInstance().getArtist(username) == null) {
            return "The username " + username + " doesn't exist.";

        }

        Artist artist = Admin.getInstance().getArtist(username);

        if (artist.getAlbum(name) == null) {
            return username + " doesn't have an album with the given name.";
        }

        for (User user : Admin.getInstance().getUsers()) {
            Player player = user.getPlayer();

            if (player.getCurrentAudioFile() == null) {
                continue;
            }

            if (player.getSource().getType() == Enums.PlayerSourceType.LIBRARY
                    && player.getSource().getAudioFile().matchesAlbum(name)) {
                return username + " can't delete this album.";
            }

            if (player.getSource().getType() == Enums.PlayerSourceType.PLAYLIST) {
                for (Song song : ((Playlist) player.getSource().getAudioCollection()).getSongs()) {
                    if (song.matchesAlbum(name)) {
                        return username + " can't delete this album.";
                    }
                }
            }

            if (player.getSource().getType() == Enums.PlayerSourceType.ALBUM
                    && player.getSource().getAudioCollection().getName().equals(name)) {
                return username + " can't delete this album.";
            }
        }

        artist.getAlbums().remove(artist.getAlbum(name));
        Admin.getInstance().getAlbums().remove(Admin.getInstance().getAlbum(name));

        return username + " deleted the album successfully.";
    }

    /**
     * Shows albums.
     *
     * @return the array list of album output
     */
    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> albumOutputs = new ArrayList<>();

        for (Album album : albums) {
            albumOutputs.add(new AlbumOutput(album));
        }

        return albumOutputs;
    }

    /**
     * Adds event.
     *
     * @param username the username
     * @param name the name
     * @param description the description
     * @param date the date
     * @return the string
     */
    public static String addEvent(final String username, final String name,
                                                    final String description, final String date) {
        if (Admin.getInstance().getHost(username) != null
                || Admin.getInstance().getUser(username) != null) {
            return username + " is not an artist.";
        }

        if (Admin.getInstance().getArtist(username) == null) {
            return "The username " + username + " doesn't exist.";
        }

        Artist artist = Admin.getInstance().getArtist(username);

        if (artist.getEvent(name) != null) {
            return username + " has another event with the same name.";
        }

        String[] dateSplit = date.split("-");

        int day = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int year = Integer.parseInt(dateSplit[2]);

        if (year < MIN_YEAR || year > MAX_YEAR || day > MAX_DAY
                || month > MAX_MONTH || (month == FEB_MONTH && day > FEB_MAX_DAY)) {
            return "Event for " + username + " does not have a valid date.";
        }

        artist.getEvents().add(new Event(name, description, date));

        return username + " has added new event successfully.";
    }

    /**
     * Removes event.
     *
     * @param username the username
     * @param name the name
     * @return the string
     */
    public static String removeEvent(final String username, final String name) {
        if (Admin.getInstance().getHost(username) != null
                || Admin.getInstance().getUser(username) != null) {
            return username + " is not an artist.";
        }

        if (Admin.getInstance().getArtist(username) == null) {
            return "The username " + username + " doesn't exist.";
        }

        Artist artist = Admin.getInstance().getArtist(username);

        if (artist.getEvent(name) == null) {
            return username + " has no event with the given name.";
        }

        artist.getEvents().remove(artist.getEvent(name));
        return username + " deleted the event successfully.";
    }

    /**
     * Adds merch.
     *
     * @param username the username
     * @param name the name
     * @param description the description
     * @param price the price
     * @return the string
     */
    public static String addMerch(final String username, final String name,
                                                    final String description, final int price) {
        if (Admin.getInstance().getHost(username) != null
                || Admin.getInstance().getUser(username) != null) {
            return username + " is not an artist.";
        }

        if (Admin.getInstance().getArtist(username) == null) {
            return "The username " + username + " doesn't exist.";
        }

        Artist artist = Admin.getInstance().getArtist(username);

        if (artist.getMerch(name) != null) {
            return username + " has merchandise with the same name.";
        }

        if (price < 0) {
            return "Price for merchandise can not be negative.";
        }

        artist.getMerchArticles().add(new Merch(name, description, price));

        return username + " has added new merchandise successfully.";
    }
}
