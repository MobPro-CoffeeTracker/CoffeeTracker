package ch.hslu.mobpro.coffeetracker.player;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import ch.hslu.mobpro.coffeetracker.R;

import static java.lang.Math.log;

public class PlayerLevelService extends Service implements IPlayerLevel {


    private final IBinder levelBinder = new LevelBinder();
    private final static int MAX_LEVEL = 10;
    private ILevelStorage levelStorage = new Storage();
    private IPlayerExperience experience;
    private ServiceConnection connection = new ExperienceServiceConnector();


    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(this, PlayerExperienceService.class);
        startService(intent);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public int getLevel() {
        double value = experience.getCurrentExp() / 100;
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

    public class LevelBinder extends Binder {
        IPlayerLevel getService() {
            return PlayerLevelService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return levelBinder;
    }

    private class Storage implements ILevelStorage {

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

    private class ExperienceServiceConnector implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            PlayerExperienceService.ExperienceBinder binder = (PlayerExperienceService.ExperienceBinder) service;
            experience = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    }
}
