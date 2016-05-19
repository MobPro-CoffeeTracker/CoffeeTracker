package ch.hslu.mobpro.coffeetracker.coffee;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.Map;

import ch.hslu.mobpro.coffeetracker.player.PlayerExperienceService;

public class CoffeeManager extends Service implements ICoffeeManager {

    private final IBinder managerBinder = new ManagerBinder();

    private ICoffeeStorage storage;

    private ServiceConnection connection = new GPSServiceConnector();
    private IGPS gps;

    @Override
    public void onCreate() {
        super.onCreate();
        storage = new CoffeeDBStorage(getApplicationContext());

        Intent intent = new Intent(this, GPSImplementation.class);
        startService(intent);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return managerBinder;
    }

    @Override
    public void drink() {
        storage.storeLocation(new Date(), gps.getLocation());
    }

    @Override
    public void clearAll() {
        storage.clearAll();
    }

    @Override
    public Map<Date, Location> getAllCoffee() {
        return storage.getAll();
    }

    public class ManagerBinder extends Binder {
        ICoffeeManager getService() {
            return CoffeeManager.this;
        }
    }

    private class GPSServiceConnector implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
           // GPSImplementation.GPSBinder binder = (PlayerExperienceService.ExperienceBinder) service;
            gps = ((GPSImplementation.GPSBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    }
}
