package app.searchBar;


import app.Admin;
import app.audio.LibraryEntry;
import app.user.Artist;
import app.user.Host;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtils.*;

/**
 * The type Search bar.
 */
public final class SearchBar {
    private List<LibraryEntry> results;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    @Getter
    private String lastSearchType;

    @Getter
    private LibraryEntry lastSelected;

    /**
     * Instantiates a new Search bar.
     *
     * @param user the user
     */
    public SearchBar(final String user) {
        this.results = new ArrayList<>();
        this.user = user;
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
    }

    /**
     * Search list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<LibraryEntry> search(final Filters filters, final String type) {
        List<LibraryEntry> entries;

        switch (type) {
            case "song":
                entries = new ArrayList<>(Admin.getInstance().getSongs());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getAlbum() != null) {
                    entries = filterByAlbum(entries, filters.getAlbum());
                }

                if (filters.getTags() != null) {
                    entries = filterByTags(entries, filters.getTags());
                }

                if (filters.getLyrics() != null) {
                    entries = filterByLyrics(entries, filters.getLyrics());
                }

                if (filters.getGenre() != null) {
                    entries = filterByGenre(entries, filters.getGenre());
                }

                if (filters.getReleaseYear() != null) {
                    entries = filterByReleaseYear(entries, filters.getReleaseYear());
                }

                if (filters.getArtist() != null) {
                    entries = filterByArtist(entries, filters.getArtist());
                }

                break;
            case "playlist":
                entries = new ArrayList<>(Admin.getInstance().getPlaylists());

                entries = filterByPlaylistVisibility(entries, user);

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getFollowers() != null) {
                    entries = filterByFollowers(entries, filters.getFollowers());
                }

                break;
            case "podcast":
                entries = new ArrayList<>(Admin.getInstance().getPodcasts());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                break;
            case "artist":
                entries = new ArrayList<>();
                Entry artistEntry;

                for (Artist artist : Admin.getInstance().getArtists()) {
                    artistEntry = new Entry(artist.getUsername());
                    entries.add(artistEntry);
                }

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                break;
            case "album":
                entries = new ArrayList<>(Admin.getInstance().getAlbums());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getDescription() != null) {
                    entries = filterByDescription(entries, filters.getDescription());
                }

                break;

            case "host":
                entries = new ArrayList<>();
                Entry hostEntry;

                for (Host host : Admin.getInstance().getHosts()) {
                    hostEntry = new Entry(host.getUsername());
                    entries.add(hostEntry);
                }

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
                break;
            default:
                entries = new ArrayList<>();
        }

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.results = entries;
        this.lastSearchType = type;
        return this.results;
    }

    /**
     * Select library entry.
     *
     * @param itemNumber the item number
     * @return the library entry
     */
    public LibraryEntry select(final Integer itemNumber) {
        if (this.results.size() < itemNumber) {
            results.clear();

            return null;
        } else {
            lastSelected =  this.results.get(itemNumber - 1);
            results.clear();

            return lastSelected;
        }
    }
}
