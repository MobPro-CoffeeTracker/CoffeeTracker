package ch.hslu.mobpro.coffeetracker.coffee;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class GPSImplementationAndroidTest {

    private IGPS service;
    private static final int MAX_ITERATION = 100;

    @Rule
    public final ServiceTestRule mGPSService = new ServiceTestRule();


    public GPSImplementationAndroidTest() throws TimeoutException {

        //Workaround Issue 180396: 	ServiceTestRule.bindService() returns null on 2nd invocation
        int it = 0;
        IBinder binder;

        while ((binder = mGPSService.bindService(
                new Intent(InstrumentationRegistry.getTargetContext(),
                        GPSImplementation.class))) == null && it < MAX_ITERATION) {
            it++;
        }
        service =
                ((GPSImplementation.GPSBinder) binder).getService();

    }

    @Test
    public void testGetLocation() {
        assertNotNull(service.getLocation());
    }
}