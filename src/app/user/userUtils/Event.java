package app.user.userUtils;

import lombok.Getter;

@Getter
public final class Event {
    private final String name;
    private final String description;
    private final String date;

    /**
     * Instantiates a new event.
     *
     * @param name the event name
     * @param description the event description
     * @param date the event date
     */
    public Event(final String name, final String description, final String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }
}
