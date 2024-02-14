package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Collections.PodcastOutput;
import app.audio.Files.Episode;
import app.player.Player;
import app.user.userUtils.Announcement;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Host extends User {
    private final ArrayList<Podcast> podcasts;
    private final ArrayList<Announcement> announcements;

    /**
     * Instantiates a new host.
     *
     * @param username the username
     * @param age the age
     * @param city the city
     */
    public Host(final String username, final int age, final String city) {
        super(username, age, city);
        this.podcasts = new ArrayList<>();
        this.announcements = new ArrayList<>();
    }

    /**
     * Gets announcement from the list.
     *
     * @param name the announcement name
     * @return the announcement
     */
    public Announcement getAnnouncement(final String name) {
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                return announcement;
            }
        }
        return null;
    }

    /**
     * Gets podcast from the list.
     *
     * @param name the podcast name
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
     * Adds podcast.
     *
     * @param username the username
     * @param name the name
     * @param episodes the episodes
     * @return the string
     */
    public static String addPodcast(final String username, final String name,
                                                            final ArrayList<Episode> episodes) {
        if (Admin.getInstance().getArtist(username) != null
                || Admin.getInstance().getUser(username) != null) {
            return username + " is not a host.";
        }

        if (Admin.getInstance().getHost(username) == null) {
            return "The username " + username + " doesn't exist.";
        }
        Host host = Admin.getInstance().getHost(username);

        if (host.getPodcast(name) != null) {
            return username + " has another podcast with the same name.";
        }

        Admin.getInstance().getPodcastList().add(new Podcast(name, username, episodes));
        host.getPodcasts().add(new Podcast(name, username, episodes));

        return username + " has added new podcast successfully.";
    }

    /**
     * Removes podcast.
     *
     * @param username the username
     * @param name the name
     * @return the string
     */
    public static String removePodcast(final String username, final String name) {
        if (Admin.getInstance().getArtist(username) != null
                || Admin.getInstance().getUser(username) != null) {
            return username + " is not a host.";
        }

        if (Admin.getInstance().getHost(username) == null) {
            return "The username " + username + " doesn't exist.";
        }
        Host host = Admin.getInstance().getHost(username);

        if (host.getPodcast(name) == null) {
            return username + " doesn't have a podcast with the given name.";
        }
        for (User user : Admin.getInstance().getUsers()) {
            Player player = user.getPlayer();

            if (player.getCurrentAudioFile() == null) {
                continue;
            }

            if (player.getSource().getType() == Enums.PlayerSourceType.PODCAST
                    && player.getSource().getAudioCollection().matchesName(name)) {
                return username + " can't delete this podcast.";
            }
        }

        host.getPodcasts().remove(host.getPodcast(name));
        Admin.getInstance().getPodcasts().remove(Admin.getInstance().getPodcast(name));

        return username + " deleted the podcast successfully.";
    }

    /**
     * Shows podcasts.
     *
     * @return the array list of podcast output
     */
    public ArrayList<PodcastOutput> showPodcasts() {
        ArrayList<PodcastOutput> podcastOutputs = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastOutputs.add(new PodcastOutput(podcast));
        }

        return podcastOutputs;
    }

    /**
     * Adds announcement.
     *
     * @param username the username
     * @param name the name
     * @param description the description
     * @return the string
     */
    public static String addAnnouncement(final String username, final String name,
                                                                    final String description) {
        if (Admin.getInstance().getArtist(username) != null
                || Admin.getInstance().getUser(username) != null) {
            return username + " is not a host.";
        }

        if (Admin.getInstance().getHost(username) == null) {
            return "The username " + username + " doesn't exist.";
        }

        Host host = Admin.getInstance().getHost(username);

        if (host.getAnnouncement(name) != null) {
            return username + " has another announcement with the same name.";
        }

        host.getAnnouncements().add(new Announcement(name, description));

        return username + " has successfully added new announcement.";
    }

    /**
     * Removes announcement.
     *
     * @param username the username
     * @param name the name
     * @return the string
     */
    public static String removeAnnouncement(final String username, final String name) {
        if (Admin.getInstance().getArtist(username) != null
                || Admin.getInstance().getUser(username) != null) {
            return username + " is not a host.";
        }

        if (Admin.getInstance().getHost(username) == null) {
            return "The username " + username + " doesn't exist.";
        }

        Host host = Admin.getInstance().getHost(username);

        if (host.getAnnouncement(name) == null) {
            return username + " has no announcement with the given name.";
        }

        host.getAnnouncements().remove(host.getAnnouncement(name));

        return username + " has successfully deleted the announcement.";

    }
}
