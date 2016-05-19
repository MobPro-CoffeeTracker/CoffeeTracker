package ch.hslu.mobpro.coffeetracker.coffee;

import android.location.Location;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ICoffeeManager {
    void drink();
    void clearAll();
    Map<Date, Location> getAllCoffee();
}
