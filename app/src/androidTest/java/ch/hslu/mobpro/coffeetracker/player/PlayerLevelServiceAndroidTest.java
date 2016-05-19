package ch.hslu.mobpro.coffeetracker.player;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(AndroidJUnit4.class)
public class PlayerLevelServiceAndroidTest {

    private IPlayerLevel service;
    private static final int MAX_ITERATION = 200;

    @Rule
    public final ServiceTestRule mLevelService = new ServiceTestRule();

    public PlayerLevelServiceAndroidTest() throws Exception {
        //Workaround Issue 180396: 	ServiceTestRule.bindService() returns null on 2nd invocation
        int it = 0;
        IBinder binder;

        while((binder = mLevelService.bindService(
                new Intent(InstrumentationRegistry.getTargetContext(),
                        PlayerLevelService.class))) == null && it < MAX_ITERATION){
            it++;
        }
        service = ((PlayerLevelService.LevelBinder) binder).getService();
    }


    @Test
    public void testExpToNextLevel() {
        assertEquals(100, service.expToNextLevel());
    }

    @Test
    public void testgetLevel() {
        assertEquals(1, service.getLevel());
    }

    @Test
    public void testLevelDescription() {
        assertNotNull(service.getLevelDescription());
    }
}