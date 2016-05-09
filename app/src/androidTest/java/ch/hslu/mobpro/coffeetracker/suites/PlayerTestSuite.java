package ch.hslu.mobpro.coffeetracker.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ch.hslu.mobpro.coffeetracker.player.PlayerServiceExperienceTest;

// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses({PlayerServiceExperienceTest.class})
public class PlayerTestSuite {
}