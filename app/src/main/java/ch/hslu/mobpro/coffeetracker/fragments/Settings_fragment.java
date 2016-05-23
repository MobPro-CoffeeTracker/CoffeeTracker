package ch.hslu.mobpro.coffeetracker.fragments;


import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.hslu.mobpro.coffeetracker.R;
import ch.hslu.mobpro.coffeetracker.coffee.CoffeeService;
import ch.hslu.mobpro.coffeetracker.coffee.ICoffeeManager;
import ch.hslu.mobpro.coffeetracker.player.IPlayerExperience;
import ch.hslu.mobpro.coffeetracker.player.PlayerExperienceService;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings_fragment extends Fragment {
    private ICoffeeManager coffeeManager;
    private IPlayerExperience playerExperience;

    private ExperienceConnector experienceConnector = new ExperienceConnector();
    private CoffeeConnector coffeeConnector = new CoffeeConnector();

    public Settings_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent coffee = new Intent(getActivity(), CoffeeService.class);
        getActivity().startService(coffee);
        getActivity().bindService(coffee, coffeeConnector, Context.BIND_AUTO_CREATE);

        Intent experience = new Intent(getActivity(), PlayerExperienceService.class);
        getActivity().startService(experience);
        getActivity().bindService(experience, experienceConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(coffeeConnector);
        getActivity().unbindService(experienceConnector);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        Button clear_location = (Button) view.findViewById(R.id.clear_location);
        clear_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.delete_location_question);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearLocation();
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button clear_experience = (Button) view.findViewById(R.id.clear_experience);
        clear_experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.delete_experience_question);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearExperience();
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button clear_all = (Button) view.findViewById(R.id.clear_all);
        clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.delete_everything_question);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearAll();
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void clearLocation() {
        coffeeManager.clearAll();
    }

    private void clearExperience() {
        playerExperience.clearExp();
    }

    private void clearAll() {
        clearLocation();
        clearExperience();
    }

    private class CoffeeConnector implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            coffeeManager = ((CoffeeService.CoffeeBinder) service).getService();
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
}
