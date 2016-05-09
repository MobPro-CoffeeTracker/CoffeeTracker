package ch.hslu.mobpro.coffeetracker.player.storage;

import android.content.res.Resources;

public interface ILevelStorage {
    String getMaxLevelDescription();
    String getUnknownLevelDescription();
    String getLevelDescription(int level);
}
