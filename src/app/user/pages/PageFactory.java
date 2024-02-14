package app.user.pages;

import app.Admin;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.Artist;
import app.user.Host;
import app.user.User;

import java.util.ArrayList;

public final class PageFactory {

    /**
     * Prints page.
     *
     * @param user the user
     * @return the string
     */
    public String printPage(final User user) {
        ArrayList<Song> likedSongs = user.getLikedSongs();
        ArrayList<Playlist> followedPlaylists = user.getFollowedPlaylists();

        switch (user.getCurrentPage()) {
            case HOME:
                Page homePage = new HomePage(likedSongs, followedPlaylists);

                return homePage.printCurrentPage();
            case LIKED:
                LikedPage likedPage = new LikedPage(likedSongs, followedPlaylists);

                return likedPage.printCurrentPage();
            case ARTIST:
                Artist artist = Admin.getInstance().getArtist(user.getNameArtistPage());
                Page artistPage = new ArtistPage(artist.getAlbums(),
                                                    artist.getEvents(), artist.getMerchArticles());

                return artistPage.printCurrentPage();
            case HOST:
                Host host = Admin.getInstance().getHost(user.getNameHostPage());
                Page hostPage = new HostPage(host.getPodcasts(), host.getAnnouncements());

                return hostPage.printCurrentPage();
            default:
                return null;
        }

    }
}
