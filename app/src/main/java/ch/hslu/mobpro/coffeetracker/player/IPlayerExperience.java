package ch.hslu.mobpro.coffeetracker.player;

public interface IPlayerExperience {
    /**
     * Reset all Experience to 0.
     */
    void clearExp();

    /**
     * Adds the specified absolute amount of Experience Points.
     *
     * @param exp Experience
     */
    void addExp(final int exp);

    /**
     * Returns the Current Amount of Experience.
     *
     * @return Experience
     */
    int getCurrentExp();


}
