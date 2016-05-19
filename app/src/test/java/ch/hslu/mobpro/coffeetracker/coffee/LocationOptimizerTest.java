package ch.hslu.mobpro.coffeetracker.coffee;

import android.location.Location;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocationOptimizerTest {

    @Mock
    Location location1 = mock(Location.class);

    @Mock
    Location location2 = mock(Location.class);

    @Test
    public void testIsSameProvider() throws Exception {
        String provider1 = "provider1";
        String provider2 = "provider2";

        assertTrue(LocationOptimizer.isSameProvider(provider1, provider1));
        assertTrue(LocationOptimizer.isSameProvider(null, null));
        assertFalse(LocationOptimizer.isSameProvider(provider1, null));
        assertFalse(LocationOptimizer.isSameProvider(null, provider2));
        assertFalse(LocationOptimizer.isSameProvider(provider1, provider2));
    }

    @Test
    public void testCalculateTimeDelta() throws Exception {
        when(location1.getTime()).thenReturn((long) 100, (long) 200, (long) 100);
        when(location2.getTime()).thenReturn((long) 200, (long) 100, (long) 100);

        assertEquals((long) 100, LocationOptimizer.calculateTimeDelta(location2, location1));
        assertEquals((long) -100, LocationOptimizer.calculateTimeDelta(location2, location1));
        assertEquals((long) 0, LocationOptimizer.calculateTimeDelta(location2, location1));
    }

    @Test
    public void testIsSignificantlyNewer() throws Exception {
        when(location1.getTime()).thenReturn((long) 0, (long) 0);
        when(location2.getTime()).thenReturn((long) LocationOptimizer.TIME_OUT, (long) LocationOptimizer.TIME_OUT + 1);

        assertFalse(LocationOptimizer.isSignificantlyNewer(location2, location1));
        assertTrue(LocationOptimizer.isSignificantlyNewer(location2, location1));
    }

    @Test
    public void testIsSignificantlyOlder() throws Exception {
        when(location1.getTime()).thenReturn((long) 0, (long) 0);
        when(location2.getTime()).thenReturn((long) LocationOptimizer.TIME_OUT, (long) LocationOptimizer.TIME_OUT + 1);

        assertFalse(LocationOptimizer.isSignificantlyOlder(location1, location2));
        assertTrue(LocationOptimizer.isSignificantlyOlder(location1, location2));
    }

    @Test
    public void testCalculateAccuracyDelta() throws Exception {
        when(location1.getAccuracy()).thenReturn((float) 0.0, (float) 0.0);
        when(location2.getAccuracy()).thenReturn((float) 100.0, (float) 100.0);

        assertEquals(-100, LocationOptimizer.calculateAccuracyDelta(location1, location2), 1);
        assertEquals(100, LocationOptimizer.calculateAccuracyDelta(location2, location1), 1);
    }

    @Test
    public void testIsLessAccurate() throws Exception {
        when(location1.getAccuracy()).thenReturn((float) 0.0, (float) 100.0);
        when(location2.getAccuracy()).thenReturn((float) 100.0, (float) 0.0);

        assertTrue(LocationOptimizer.isLessAccurate(location2, location1));
        assertFalse(LocationOptimizer.isLessAccurate(location2, location1));
    }

    @Test
    public void testIsMoreAccurate() throws Exception {
        when(location1.getAccuracy()).thenReturn((float) 0.0, (float) 100.0);
        when(location2.getAccuracy()).thenReturn((float) 100.0, (float) 0.0);

        assertFalse(LocationOptimizer.isMoreAccurate(location2, location1));
        assertTrue(LocationOptimizer.isMoreAccurate(location2, location1));
    }

    @Test
    public void testIsSignificantlyLessAccurate() throws Exception {
        when(location1.getAccuracy()).thenReturn((float) 0.0, (float) 0.0);
        when(location2.getAccuracy()).thenReturn((float) 200.0, (float) 201);

        assertFalse(LocationOptimizer.isSignificantlyLessAccurate(location2, location1));
        assertTrue(LocationOptimizer.isSignificantlyLessAccurate(location2, location1));
    }

    @Test
    public void testNewer() throws Exception {
        when(location1.getTime()).thenReturn((long) 0, (long) 1);
        when(location2.getTime()).thenReturn((long) 1, (long) 0);

        assertTrue(LocationOptimizer.isNewer(location2, location1));
        assertFalse(LocationOptimizer.isNewer(location2, location1));
    }


    @Test
    public void testIsBetterLocation() throws Exception {

        assertTrue(LocationOptimizer.isBetterLocation(location2, null));

        //isSignificantlyNewer
        when(location1.getTime()).thenReturn((long) 0);
        when(location2.getTime()).thenReturn((long) LocationOptimizer.TIME_OUT + 1);
        assertTrue(LocationOptimizer.isBetterLocation(location2, location1));

        //isMoreAccurate
        when(location1.getAccuracy()).thenReturn((float) 1);
        when(location2.getAccuracy()).thenReturn((float) 0);
        assertTrue(LocationOptimizer.isBetterLocation(location2, location1));
    }
}