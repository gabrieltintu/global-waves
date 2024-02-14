package app.user.pages;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.user.userUtils.Announcement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class HostPage implements Page {
    private final Map<String, ArrayList<String>> podcasts = new LinkedHashMap<>();
    private final ArrayList<String> announcements = new ArrayList<>();

    public HostPage(final ArrayList<Podcast> podcasts,
                                            final ArrayList<Announcement> announcements) {
        for (Podcast podcast : podcasts) {
            ArrayList<String> episodes = new ArrayList<>();

            for (Episode episode : podcast.getEpisodes()) {
                if (Admin.getInstance().getPodcast(podcast.getName()) != null) {
                    episodes.add(episode.getName() + " - " + episode.getDescription());
                }
            }
            this.podcasts.put(podcast.getName() + ":\n\t", episodes);
        }

        for (Announcement announcement : announcements) {
            this.announcements.add(announcement.getName() + ":\n\t"
                                                          + announcement.getDescription() + "\n");
        }
    }

    /**
     * Prints the host page.
     *
     * @return the string
     */
    @Override
    public String printCurrentPage() {
        StringBuilder message = new StringBuilder();

        message.append("Podcasts:\n\t[");
        int isLast = 0;
        for (Map.Entry<String, ArrayList<String>> entry : podcasts.entrySet()) {
            isLast++;
            message.append(entry.getKey());
            message.append(entry.getValue());
            if (isLast != podcasts.size()) {
                message.append("\n, ");
            }
        }

        message.append("\n]\n\nAnnouncements:\n");

        for (String iter : announcements) {
            message.append("\t[").append(iter);
        }
        message.append("]");

        return message.toString();
    }
}
