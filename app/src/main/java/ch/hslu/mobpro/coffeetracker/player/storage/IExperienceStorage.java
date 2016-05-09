package ch.hslu.mobpro.coffeetracker.player.storage;

public interface IExperienceStorage {

    void save(int experience);

    int getExperience();
}
