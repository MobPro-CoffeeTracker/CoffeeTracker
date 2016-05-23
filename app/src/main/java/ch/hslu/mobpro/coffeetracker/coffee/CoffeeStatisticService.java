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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CoffeeStatisticService extends Service implements ICoffeeStatistic {

    private final IBinder binder = new StatisticBinder();
    private ServiceConnection connection = new CoffeeServiceConnector();
    private ICoffeeManager coffeeService;

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, CoffeeService.class);
        startService(intent);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public double coffeePerHour() {
        List<Date> list = new ArrayList<>();
        list.addAll(coffeeService.getAllCoffee().keySet());
        if (list.size() >= 2) {
            Collections.sort(list);

            long difference = list.get(list.size() - 1).getTime() - list.get(0).getTime();
            double hours = difference / (60 * 60 * 1000);
            return list.size() / hours;
        } else {
            return 0;
        }
    }

    @Override
    public Location favoriteSpot() {
        Iterator iterator = coffeeService.getAllCoffee().values().iterator();
        Map<Location, Integer> map = new HashMap<>();
        int scale = 3;

        int max = 0;
        Location favorite = new Location("emptyList");

        while (iterator.hasNext()) {
            Location rounded = (Location) iterator.next();
            rounded.setLatitude(new BigDecimal(rounded.getLatitude()).setScale(scale, BigDecimal.ROUND_FLOOR).doubleValue());
            rounded.setLongitude(new BigDecimal(rounded.getLongitude()).setScale(scale, BigDecimal.ROUND_FLOOR).doubleValue());

            if (map.containsKey(rounded)) {
                map.put(rounded, map.get(rounded) + 1);
            } else {
                map.put(rounded, 1);
            }
            int count = map.get(rounded);

            if (count >= max) {
                max = count;
                favorite = rounded;
            }
        }

        return favorite;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class StatisticBinder extends Binder {
        public ICoffeeStatistic getService() {
            return CoffeeStatisticService.this;
        }
    }

    private class CoffeeServiceConnector implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            coffeeService = ((CoffeeService.CoffeeBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    }
}
