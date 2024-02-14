package app.user.userUtils;

import lombok.Getter;

@Getter
public final class Announcement {
    private final String name;
    private final String description;

    /**
     * Instantiates a new announcement.
     *
     * @param name the announcement name
     * @param description the announcement description
     */
    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
