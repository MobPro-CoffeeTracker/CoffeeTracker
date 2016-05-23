package ch.hslu.mobpro.coffeetracker.fragments;


import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import ch.hslu.mobpro.coffeetracker.R;
import ch.hslu.mobpro.coffeetracker.coffee.CoffeeStatisticService;
import ch.hslu.mobpro.coffeetracker.coffee.ICoffeeStatistic;
import ch.hslu.mobpro.coffeetracker.player.IPlayerExperience;
import ch.hslu.mobpro.coffeetracker.player.IPlayerLevel;
import ch.hslu.mobpro.coffeetracker.player.PlayerExperienceService;
import ch.hslu.mobpro.coffeetracker.player.PlayerLevelService;

/**
 * A simple {@link Fragment} subclass.
 */
public class Statistic_fragment extends Fragment {
    private ICoffeeStatistic coffeeStatistic;
    private IPlayerExperience playerExperience;
    private IPlayerLevel playerLevel;

    private ExperienceConnector experienceConnector = new ExperienceConnector();
    private CoffeeStatisticConnector coffeeConnector = new CoffeeStatisticConnector();
    private LevelConnector levelConnector = new LevelConnector();

    public Statistic_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(coffeeConnector);
        getActivity().unbindService(experienceConnector);
        getActivity().unbindService(levelConnector);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent coffee = new Intent(getActivity(), CoffeeStatisticService.class);
        getActivity().startService(coffee);
        getActivity().bindService(coffee, coffeeConnector, Context.BIND_AUTO_CREATE);

        Intent experience = new Intent(getActivity(), PlayerExperienceService.class);
        getActivity().startService(experience);
        getActivity().bindService(experience, experienceConnector, Context.BIND_AUTO_CREATE);

        Intent statistic = new Intent(getActivity(), PlayerLevelService.class);
        getActivity().startService(statistic);
        getActivity().bindService(statistic, levelConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.statistic_fragment, container, false);
    }

    private class CoffeeStatisticConnector implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            coffeeStatistic = ((CoffeeStatisticService.StatisticBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    }

    private class ExperienceConnector implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            playerExperience = ((PlayerExperienceService.ExperienceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    }

    private class LevelConnector implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            playerLevel = ((PlayerLevelService.LevelBinder) service).getService();
            updateStatistic();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    }

    private void updateStatistic() {
        TextView level = (TextView) getActivity().findViewById(R.id.level_stat);
        level.setText("Level " + playerLevel.getLevel() + " : " + playerLevel.getLevelDescription());

        TextView experience = (TextView) getActivity().findViewById(R.id.experience_stat);
        experience.setText("Experience: " + playerExperience.getCurrentExp() + " / " + playerLevel.expToNextLevel());

        ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.level_progress);
        progressBar.setMax(playerLevel.expToNextLevel());
        progressBar.setProgress(playerExperience.getCurrentExp());

        TextView coffee_per_hour = (TextView) getActivity().findViewById(R.id.coffee_per_hour);
        coffee_per_hour.setText("Coffee per hour: " + coffeeStatistic.coffeePerHour());

        TextView favorite_spot = (TextView) getActivity().findViewById(R.id.favorite_spot);
        Location location = coffeeStatistic.favoriteSpot();
        favorite_spot.setText("Favorite Spot: " + location.getLatitude() + "°N / " + location.getLongitude() + "°E");
    }
}
