package ch.hslu.mobpro.coffeetracker.player;

interface IExperienceStorage {

    void save(int experience);

    int getExperience();
}
