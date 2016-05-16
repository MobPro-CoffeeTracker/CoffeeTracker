package ch.hslu.mobpro.coffeetracker.player;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PlayerExperienceServiceAndroidTest {

    private IPlayerExperience service;
    private static final int MAX_ITERATION = 100;

    @Rule
    public final ServiceTestRule mExperienceService = new ServiceTestRule();

    public PlayerExperienceServiceAndroidTest() throws Exception
    {
        //Workaround Issue 180396: 	ServiceTestRule.bindService() returns null on 2nd invocation
        int it = 0;
        IBinder binder;

        while((binder = mExperienceService.bindService(
                new Intent(InstrumentationRegistry.getTargetContext(),
                        PlayerExperienceService.class))) == null && it < MAX_ITERATION){
            it++;
        }
        service = ((PlayerExperienceService.ExperienceBinder) binder).getService();
    }

    @After
    public void cleanUp()
    {
        service.clearExp();
    }

    @Test
    public void testClearExp() {
        service.addExp(100);
        service.clearExp();

        assertEquals(0, service.getCurrentExp());
    }

    @Test
    public void testaddExp() {
        service.addExp(100);

        assertEquals(100, service.getCurrentExp());
    }
}