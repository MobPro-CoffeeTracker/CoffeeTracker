package ch.hslu.mobpro.coffeetracker.player;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.support.annotation.IntegerRes;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.hslu.mobpro.coffeetracker.player.storage.IExperienceStorage;
import ch.hslu.mobpro.coffeetracker.player.storage.ILevelStorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceLevelTest {

    @Mock
    IExperienceStorage experienceStorage;

    @Mock
    ILevelStorage levelStorage;

    @InjectMocks
    IPlayerLevel testObject = new PlayerService();

    @Before
    public void before() {
    }

    @Test
    public void testGetLevel() throws Exception {

        Mockito.when(experienceStorage.getExperience()).thenReturn(-1, 0, 1, 99, 100, 101, 199, 200, 400, 800);

        assertEquals(1, testObject.getLevel()); //-1
        assertEquals(1, testObject.getLevel()); //0
        assertEquals(1, testObject.getLevel()); //1
        assertEquals(1, testObject.getLevel()); //99
        assertEquals(2, testObject.getLevel()); //100
        assertEquals(2, testObject.getLevel()); //101
        assertEquals(2, testObject.getLevel()); //199
        assertEquals(3, testObject.getLevel()); //200
        assertEquals(4, testObject.getLevel()); //400
        assertEquals(5, testObject.getLevel()); //800
    }


    @Test
    public void testGetLevelDescription() throws Exception {
        Mockito.when(experienceStorage.getExperience()).thenReturn(Integer.MIN_VALUE, 0, 400, Integer.MAX_VALUE);

        testObject.getLevelDescription();
        testObject.getLevelDescription();
        testObject.getLevelDescription();
        testObject.getLevelDescription();

        Mockito.verify(levelStorage, atLeastOnce()).getLevelDescription(anyInt());
        Mockito.verify(levelStorage, times(1)).getMaxLevelDescription();
        Mockito.verify(levelStorage, never()).getUnknownLevelDescription();
        //Mockito.when(levelStorage.getDescription(0))
    }

    @Test
    public void testExpToNextLevel() throws Exception {
        Mockito.when(experienceStorage.getExperience()).thenReturn(0, 100, 200, 400, 800);

        assertEquals(100, testObject.expToNextLevel()); //0
        assertEquals(200, testObject.expToNextLevel()); //100
        assertEquals(400, testObject.expToNextLevel()); //200
        assertEquals(800, testObject.expToNextLevel()); //400
        assertEquals(1600, testObject.expToNextLevel()); //800
    }
}