package ch.hslu.mobpro.coffeetracker.coffee;

import android.location.Location;

import java.util.Date;
import java.util.List;
import java.util.Map;

interface ICoffeeStorage {
    void storeLocation(Date timestamp, Location location);

    void clearAll();

    Map<Date, Location> getAll();
}
