package ch.hslu.mobpro.coffeetracker.player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import ch.hslu.mobpro.coffeetracker.R;
import ch.hslu.mobpro.coffeetracker.player.storage.IExperienceStorage;
import ch.hslu.mobpro.coffeetracker.player.storage.ILevelStorage;

import static java.lang.Math.abs;
import static java.lang.Math.log;

public class PlayerService extends Service implements IPlayerExperience, IPlayerLevel {

    private final IBinder experienceBinder = new ExperienceBinder();
    private final IBinder levelBinder = new LevelBinder();
    private final static int MAX_LEVEL = 10;

    private IExperienceStorage experienceStorage = new Storage();
    private ILevelStorage levelStorage = (ILevelStorage) experienceStorage;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (intent.getAction().equals(Action.EXPERIENCE.toString())) {
            return experienceBinder;
        } else if (intent.getAction().equals(Action.LEVEL.toString())) {
            return levelBinder;
        } else {
            return null;
        }
    }

    @Override
    public void clearExp() {
        experienceStorage.save(0);
    }


    @Override
    public void addExp(int exp) {
        int experience = abs(exp) + experienceStorage.getExperience();
        experienceStorage.save(experience);
    }

    @Override
    public int getCurrentExp() {
        return experienceStorage.getExperience();
    }

    @Override
    public int getLevel() {
        double value = experienceStorage.getExperience() / 100;
        if (value < 1) {
            return 1;
        } else {
            return (int) Math.ceil(log(value) / log(2) + 2);
        }
    }

    @Override
    public String getLevelDescription() {
        int level = getLevel();
        if (level < 1) {
            return levelStorage.getUnknownLevelDescription();
        } else if (level >= MAX_LEVEL) {
            return levelStorage.getMaxLevelDescription();
        } else {
            return levelStorage.getLevelDescription(level);
        }
    }

    @Override
    public int expToNextLevel() {
        return (int) Math.pow(2, getLevel() - 1) * 100;
    }

    public class ExperienceBinder extends Binder {
        IPlayerExperience getService() {
            return PlayerService.this;
        }
    }

    public class LevelBinder extends Binder {
        IPlayerLevel getService() {
            return PlayerService.this;
        }
    }

    public enum Action {
        LEVEL,
        EXPERIENCE
    }

    private class Storage implements IExperienceStorage, ILevelStorage {
        private final static String EXPERIENCE = "EXPERIENCE";
        private final static String PLAYER_PREFERENCE = "PLAYER_PREFERENCE";

        @Override
        public void save(int experience) {
            getSharedPreferences(PLAYER_PREFERENCE, MODE_PRIVATE).edit().putInt(EXPERIENCE, experience).apply();
        }

        @Override
        public int getExperience() {
            return getSharedPreferences(PLAYER_PREFERENCE, MODE_PRIVATE).getInt(EXPERIENCE, 0);
        }

        @Override
        public String getMaxLevelDescription() {
            return getResources().getString(R.string.levelMax);
        }

        @Override
        public String getUnknownLevelDescription() {
            return getResources().getString(R.string.noLevel);
        }

        @Override
        public String getLevelDescription(int level) {
            String[] levels = getResources().getStringArray(R.array.levels);
            try {
                return levels[level];
            } catch (Exception err) {
                return getUnknownLevelDescription();
            }
        }
    }
}