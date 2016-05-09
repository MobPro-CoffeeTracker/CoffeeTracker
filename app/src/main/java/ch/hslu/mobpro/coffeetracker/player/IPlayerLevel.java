package ch.hslu.mobpro.coffeetracker.player;


public interface IPlayerLevel {
    /**
     * Returns the Level of the PlayerService.
     *
     * @return Current Level
     */
    int getLevel();

    /**
     * Return the Description of the Current Level.
     *
     * @return Level Description
     */
    String getLevelDescription();

    /**
     * Return the Experience needed for the next Level.
     *
     * @return Experience needed for next Level.
     */
    int expToNextLevel();
}
