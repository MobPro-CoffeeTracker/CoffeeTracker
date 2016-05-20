package ch.hslu.mobpro.coffeetracker.player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import ch.hslu.mobpro.coffeetracker.R;

import static java.lang.Math.abs;

public class PlayerExperienceService extends Service implements IPlayerExperience {

    private final IBinder experienceBinder = new ExperienceBinder();

    private IExperienceStorage experienceStorage = new Storage();

    @Override
    public IBinder onBind(Intent intent) {
        return experienceBinder;
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

    public class ExperienceBinder extends Binder {
        public IPlayerExperience getService() {
            return PlayerExperienceService.this;
        }
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