package app.user.pages;

import app.audio.Collections.Album;
import app.user.userUtils.Event;
import app.user.userUtils.Merch;

import java.util.ArrayList;

public class ArtistPage implements Page {
    private final ArrayList<String> albums = new ArrayList<>();
    private final ArrayList<String> events = new ArrayList<>();
    private final ArrayList<String> merchArticles = new ArrayList<>();

    public ArtistPage(final ArrayList<Album> albums, final ArrayList<Event> events,
                                                        final ArrayList<Merch> merchArticles) {
        for (Album album : albums) {
            this.albums.add(album.getName());
        }

        for (Event event : events) {
            this.events.add(event.getName() + " - " + event.getDate() + ":\n\t"
                                                                + event.getDescription());
        }

        for (Merch merch : merchArticles) {
            this.merchArticles.add(merch.getName() + " - " + merch.getPrice() + ":\n\t"
                                                                + merch.getDescription());
        }
    }

    /**
     * Prints the artist page.
     *
     * @return the string
     */
    @Override
    public String printCurrentPage() {
        return "Albums:\n"
                + "\t" + albums + "\n"
                + "\nMerch:\n"
                + "\t" + merchArticles + "\n"
                + "\nEvents:\n"
                + "\t" + events;
    }
}
