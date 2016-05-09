package ch.hslu.mobpro.coffeetracker.player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import ch.hslu.mobpro.coffeetracker.player.storage.IExperienceStorage;

import static java.lang.Math.abs;

public class PlayerService extends Service implements IPlayerExperience, IPlayerLevel {

    private final IBinder experienceBinder = new ExperienceBinder();
    private final IBinder levelBinder = new LevelBinder();

    private IExperienceStorage storage = new Storage();

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
        storage.save(0);
    }


    @Override
    public void addExp(int exp) {
        int experience = abs(exp) + storage.getExperience();
        storage.save(experience);
    }

    @Override
    public int getCurrentExp() {
        return storage.getExperience();
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public String getLevelDescription() {
        return null;
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

    private class Storage implements IExperienceStorage {
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
    }
}