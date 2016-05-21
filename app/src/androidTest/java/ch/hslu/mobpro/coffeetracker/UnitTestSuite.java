package ch.hslu.mobpro.coffeetracker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ch.hslu.mobpro.coffeetracker.coffee.CoffeeDBStorageAndroidTest;
import ch.hslu.mobpro.coffeetracker.coffee.CoffeeServiceAndroidTest;
import ch.hslu.mobpro.coffeetracker.coffee.GPSImplementationAndroidTest;
import ch.hslu.mobpro.coffeetracker.player.PlayerExperienceServiceAndroidTest;
import ch.hslu.mobpro.coffeetracker.player.PlayerLevelServiceAndroidTest;

// Runs all unit tests.
@RunWith(Suite.class)
//@Suite.SuiteClasses({PlayerExperienceServiceAndroidTest.class})
//@Suite.SuiteClasses({PlayerLevelServiceAndroidTest.class})
@Suite.SuiteClasses({PlayerExperienceServiceAndroidTest.class, PlayerLevelServiceAndroidTest.class, CoffeeDBStorageAndroidTest.class, GPSImplementationAndroidTest.class, CoffeeServiceAndroidTest.class})
public class UnitTestSuite {
}
