package ch.hslu.mobpro.coffeetracker.coffee;

import android.location.Location;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class CoffeeManagerTest {

    @Mock
   ICoffeeStorage storage;

    @Mock
    IGPS gps;

    @InjectMocks
    ICoffeeManager coffeeManager = new CoffeeManager();

    @Test
    public void testClearAll() throws Exception {
        coffeeManager.clearAll();
        verify(storage, atLeastOnce()).clearAll();
    }
    @Test
    public void testDrink() throws Exception {
        Location location = new Location("dummy");
        when(gps.getLocation()).thenReturn(location);

        coffeeManager.drink();
        verify(storage, times(1)).storeLocation((Date) any(), eq(location));
    }
    @Test
    public void testGetAll() throws Exception {
        coffeeManager.getAllCoffee();
        verify(storage, times(1)).getAll();
    }
}