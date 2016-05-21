package ch.hslu.mobpro.coffeetracker.coffee;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CoffeeServiceAndroidTest {

    private ICoffeeManager service;
    private static final int MAX_ITERATION = 100;

    @Rule
    public final ServiceTestRule serviceTestRule = new ServiceTestRule();


    public CoffeeServiceAndroidTest() throws TimeoutException {

        //Workaround Issue 180396: 	ServiceTestRule.bindService() returns null on 2nd invocation
        int it = 0;
        IBinder binder;

        while ((binder = serviceTestRule.bindService(
                new Intent(InstrumentationRegistry.getTargetContext(),
                        CoffeeService.class))) == null && it < MAX_ITERATION) {
            it++;
        }
        service =
                ((CoffeeService.CoffeeBinder) binder).getService();

    }

    @After
    public void tearDown() {
        service.clearAll();
    }

    @Test
    public void testDrink() {
        service.drink();
    }

    @Test
    public void testClearAll() {
        service.clearAll();

        assertEquals(0, service.getAllCoffee().size());
    }

    @Test
    public void testGetAll() {
        service.drink();

        service.getAllCoffee();

        assertEquals(1, service.getAllCoffee().size());
    }
}