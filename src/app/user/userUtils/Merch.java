package app.user.userUtils;

import lombok.Getter;

@Getter
public final class Merch {
    private final String name;
    private final String description;
    private final int price;

    /**
     * Instantiates a new merch.
     *
     * @param name the merch name
     * @param description the merch description
     * @param price the merch price
     */
    public Merch(final String name, final String description, final int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
