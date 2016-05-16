package ch.hslu.mobpro.coffeetracker.player;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class PlayerExperienceServiceTest {

    @Mock
    IExperienceStorage experienceStorage;

    @Mock
    ILevelStorage levelStorage;

    @InjectMocks
    IPlayerExperience testObject = new PlayerExperienceService();

    @Test
    public void testClearExp() throws Exception {
        testObject.clearExp();
        Mockito.verify(experienceStorage).save(0);
    }

    @Test
    public void testAddPositiveExp() throws Exception {
        Mockito.when(experienceStorage.getExperience()).thenReturn(500);

        testObject.addExp(500);

        Mockito.verify(experienceStorage, times(1)).save(1000);
    }
    @Test
    public void testAddZeroExp() throws Exception {
        Mockito.when(experienceStorage.getExperience()).thenReturn(500);

        testObject.addExp(0);

        Mockito.verify(experienceStorage, times(1)).save(500);
    }
    @Test
    public void testAddNegativeExp() throws Exception {
        Mockito.when(experienceStorage.getExperience()).thenReturn(500);

        testObject.addExp(-500);

        Mockito.verify(experienceStorage, times(1)).save(1000);
    }

    @Test
    public void testGetCurrentExp() throws Exception {
        testObject.getCurrentExp();

        Mockito.verify(experienceStorage, times(1)).getExperience();
    }
}