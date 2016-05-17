package ch.hslu.mobpro.coffeetracker.coffee;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class CoffeeManager extends Service implements ICoffeeManager {

    private final IBinder managerBinder = new ManagerBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
            return managerBinder;
    }

    @Override
    public void drink() {

    }

    @Override
    public void clearAll() {

    }

    @Override
    public Map<Date, Location> getCoffee(Date start, Date end) {
        return null;
    }

    public class ManagerBinder extends Binder {
        ICoffeeManager getService() {
            return CoffeeManager.this;
        }
    }
}
