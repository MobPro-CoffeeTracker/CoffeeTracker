package ch.hslu.mobpro.coffeetracker.player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ch.hslu.mobpro.coffeetracker.player.storage.IExperienceStorage;
import ch.hslu.mobpro.coffeetracker.player.storage.ILevelStorage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceExperienceTest {

    @Mock
    IExperienceStorage experienceStorage;

    @Mock
    ILevelStorage levelStorage;

    @InjectMocks
    IPlayerExperience testObject = new PlayerService();

    @Test
    public void testClearExp() throws Exception {
        testObject.clearExp();
        Mockito.verify(experienceStorage).save(0);
    }

    @Test
    public void testAddExp() throws Exception {
        Mockito.when(experienceStorage.getExperience()).thenReturn(500);

        testObject.addExp(500);

        Mockito.verify(experienceStorage, times(1)).save(1000);
    }

    @Test
    public void testGetCurrentExp() throws Exception {
        testObject.getCurrentExp();

        Mockito.verify(experienceStorage, times(1)).getExperience();
    }
}