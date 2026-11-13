package ca.sheridancollege.khvostiv.beans;

public enum Genre {
    BALLADE("Ballade"),
    NOCTURNE("Nocturne"),
    SONATA("Sonata"),
    PRELUDE("Prelude"),
    ETUDE("Etude"),
    WALTZ("Waltz");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
