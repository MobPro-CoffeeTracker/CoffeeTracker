package ch.hslu.mobpro.coffeetracker.player;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceLevelTest {

    @Mock
    IExperienceStorage storage;

    IPlayerLevel testObject;

    @Before
    public void prepare() {
        testObject = new PlayerService();
        MockitoAnnotations.initMocks(testObject);
    }

    @Test
    public void testGetLevel() throws Exception {
        Mockito.when(storage.getExperience()).thenReturn(0);
        assertEquals(0, testObject.getLevel());
    }
}