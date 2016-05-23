package ch.hslu.mobpro.coffeetracker.coffee;

import android.location.Location;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class CoffeeStatisticServiceTest {

    @Mock
    ICoffeeManager coffeeService;

    @Mock
    Location location1;

    @Mock
    Location location2;

    @Mock
    Location location3;

    @InjectMocks
    ICoffeeStatistic coffeeStatistic = new CoffeeStatisticService();

    @Test
    public void testCoffeePerHour() throws Exception {
        GregorianCalendar date1 = new GregorianCalendar(2000, 1, 1);
        GregorianCalendar date2 = new GregorianCalendar(2000, 1, 2);
        GregorianCalendar date3 = new GregorianCalendar(2000, 1, 6);

        Map<Date, Location> values = new HashMap<>();
        values.put(date1.getTime(), new Location("dummy"));
        values.put(date2.getTime(), new Location("dummy"));
        values.put(date3.getTime(), new Location("dummy"));

        when(coffeeService.getAllCoffee()).thenReturn(values);

        assertEquals(0.025, coffeeStatistic.coffeePerHour(), 0.0000001);
        values.clear();
        when(coffeeService.getAllCoffee()).thenReturn(values);

        assertEquals(0, coffeeStatistic.coffeePerHour(), 0.0000001);
    }

    @Test
    public void testFavoriteSpot() throws Exception {
        Map<Date, Location> values = new HashMap<>();
        when(location1.getLatitude()).thenReturn(0.1111111, 0.111);
        when(location1.getLongitude()).thenReturn(0.1111111, 0.111);

        when(location2.getLatitude()).thenReturn(0.1122222, 0.111);
        when(location2.getLongitude()).thenReturn(0.1122222, 0.111);

        when(location3.getLatitude()).thenReturn(0.22222, 0.222);
        when(location3.getLongitude()).thenReturn(0.22222, 0.222);

        values.put(new GregorianCalendar(2000, 1, 1).getTime(), location1);
        values.put(new GregorianCalendar(2000, 1, 2).getTime(), location2);
        values.put(new GregorianCalendar(2000, 1, 3).getTime(), location3);

        when(coffeeService.getAllCoffee()).thenReturn(values);

        Location favorite = coffeeStatistic.favoriteSpot();

        verify(location1).setLongitude(0.111);
        verify(location1).setLatitude(0.111);
        verify(location2).setLongitude(0.112);
        verify(location2).setLatitude(0.112);
        verify(location3).setLongitude(0.222);
        verify(location3).setLatitude(0.222);

        assertEquals(0.111, favorite.getLongitude(), 0.0000001);
        assertEquals(0.111, favorite.getLatitude(), 0.0000001);
    }
}