package ch.hslu.mobpro.coffeetracker.player;

interface ILevelStorage {
    String getMaxLevelDescription();
    String getUnknownLevelDescription();
    String getLevelDescription(int level);
}
