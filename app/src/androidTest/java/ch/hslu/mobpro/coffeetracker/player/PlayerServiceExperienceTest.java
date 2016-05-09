package ch.hslu.mobpro.coffeetracker.player;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PlayerServiceExperienceTest {

    @Rule
    public ServiceTestRule serviceTestRule = new ServiceTestRule();

    IPlayerExperience experience;

    @Before
    public void prepare() throws TimeoutException {
        Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), PlayerService.class);
        intent.setAction(PlayerService.Action.EXPERIENCE.toString());
        experience = ((PlayerService.ExperienceBinder) serviceTestRule.bindService(intent)).getService();
    }

    @After
    public void reset()
    {

    }

    @Test
    public void testClearExp() {
        experience.addExp(100);
        experience.clearExp();
        assertEquals(0, experience.getCurrentExp());
    }

    @Test
    public void testAddExp() {
        experience.addExp(100);
        assertEquals(100, experience.getCurrentExp());
        experience.addExp(-200);
        assertEquals(300, experience.getCurrentExp());
        experience.clearExp();
    }


    @Test
    public void testGetCurrentExp() throws TimeoutException {
        experience.addExp(100);
        int test = experience.getCurrentExp();
        assertEquals(100, experience.getCurrentExp());
        experience.clearExp();
    }
}