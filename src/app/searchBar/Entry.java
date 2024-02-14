package app.searchBar;

import app.audio.LibraryEntry;
import lombok.Setter;

@Setter
public class Entry extends LibraryEntry {
    /**
     * Instantiates a new artist entry.
     *
     * @param name the name
     */
    public Entry(final String name) {
        super(name);
    }
}
