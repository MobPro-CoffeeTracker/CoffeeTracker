package ch.hslu.mobpro.coffeetracker.coffee;

import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CoffeeDBStorageAndroidTest {

    private ICoffeeStorage coffeeStorage;

    public CoffeeDBStorageAndroidTest() throws Exception {
        coffeeStorage = new CoffeeDBStorage(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testGetAll() {
        Location location1 = new Location("testDummy");
        Location location2 = new Location("testDummy");

        coffeeStorage.storeLocation(new Date(), location1);
        coffeeStorage.storeLocation(new Date(), location2);

        Map<Date, Location> values = coffeeStorage.getAll();

        assertEquals(2, values.size());
    }

    @Test
    public void testStoreLocation() throws InterruptedException {
        Date date1 = new Date();
        Thread.sleep(100);
        Date date2 = new Date();
        Location location1 = new Location("testDummy");
        location1.setLongitude(1.01);
        location1.setLatitude(1.01);

        Location location2 = new Location("testDummy");
        location2.setLongitude(2.003);
        location2.setLatitude(2.003);

        coffeeStorage.storeLocation(date1, location1);
        coffeeStorage.storeLocation(date2, location2);

        Map<Date, Location> values = coffeeStorage.getAll();

        assertEquals(location2.getLatitude(), values.get(date2).getLatitude(), 0.000001);
        assertEquals(location2.getLongitude(), values.get(date2).getLongitude(), 0.000001);

        assertEquals(location1.getLatitude(), values.get(date1).getLatitude(), 0.000001);
        assertEquals(location1.getLongitude(), values.get(date1).getLongitude(), 0.000001);
    }

    @Test
    public void testClearAll() {
        coffeeStorage.storeLocation(new Date(), new Location("testdummy"));

        coffeeStorage.clearAll();

        assertEquals(0, coffeeStorage.getAll().size());
    }

    @After
    public void tearDown() {
        coffeeStorage.clearAll();
    }
}