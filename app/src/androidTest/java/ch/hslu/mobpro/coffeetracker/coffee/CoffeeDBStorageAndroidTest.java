package ch.hslu.mobpro.coffeetracker.coffee;

import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
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
    }

    @Test
    public void testStoreLocation() throws InterruptedException {
        Date date1 = new Date();
        Thread.sleep(100);
        Date date2 = new Date();
        Location location1 = new Location("testDummy");
        location1.setLongitude(1.0);
        location1.setLatitude(1.0);

        Location location2 = new Location("testDummy");
        location2.setLongitude(2.0);
        location2.setLatitude(2.0);

        coffeeStorage.storeLocation(date1, location1);
        coffeeStorage.storeLocation(date2, location2);

        Map<Date, Location> values = coffeeStorage.getAll();

        assertEquals(location2.getLatitude(), values.get(date2).getLatitude(), 0);
        assertEquals(location2.getLongitude(), values.get(date2).getLongitude(), 0);

        assertEquals(location1.getLatitude(), values.get(date1).getLatitude(), 0);
        assertEquals(location1.getLongitude(), values.get(date1).getLongitude(), 0);
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