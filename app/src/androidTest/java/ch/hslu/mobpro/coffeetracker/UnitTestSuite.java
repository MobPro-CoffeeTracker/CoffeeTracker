package ch.hslu.mobpro.coffeetracker;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ch.hslu.mobpro.coffeetracker.player.PlayerExperienceServiceAndroidTest;
import ch.hslu.mobpro.coffeetracker.player.PlayerLevelServiceAndroidTest;

// Runs all unit tests.
@RunWith(Suite.class)
//@Suite.SuiteClasses({PlayerExperienceServiceAndroidTest.class})
//@Suite.SuiteClasses({PlayerLevelServiceAndroidTest.class})
@Suite.SuiteClasses({PlayerExperienceServiceAndroidTest.class, PlayerLevelServiceAndroidTest.class})
public class UnitTestSuite {}
