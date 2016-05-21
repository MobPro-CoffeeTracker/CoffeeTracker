package ch.hslu.mobpro.coffeetracker.coffee;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class CoffeeStatisticAndroidTest {

    private ICoffeeStatistic service;
    private static final int MAX_ITERATION = 100;

    @Rule
    public final ServiceTestRule serviceTestRule = new ServiceTestRule();


    public CoffeeStatisticAndroidTest() throws TimeoutException {

        //Workaround Issue 180396: 	ServiceTestRule.bindService() returns null on 2nd invocation
        int it = 0;
        IBinder binder;
        while ((binder = serviceTestRule.bindService(
                new Intent(InstrumentationRegistry.getTargetContext(),
                        CoffeeStatisticService.class))) == null && it < MAX_ITERATION) {
            it++;
        }
        service =
                ((CoffeeStatisticService.StatisticBinder) binder).getService();

    }

    @Test
    public void testFavoriteSpot() {
        service.favoriteSpot();
    }

    @Test
    public void testCoffeePerHour() {
        service.coffeePerHour();
    }
}