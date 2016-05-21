package ch.hslu.mobpro.coffeetracker.fragments;


import android.Manifest;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ch.hslu.mobpro.coffeetracker.R;
import ch.hslu.mobpro.coffeetracker.coffee.CoffeeService;
import ch.hslu.mobpro.coffeetracker.coffee.ICoffeeManager;
import ch.hslu.mobpro.coffeetracker.player.IPlayerExperience;
import ch.hslu.mobpro.coffeetracker.player.PlayerExperienceService;

public class Coffee_fragment extends Fragment {

    private ICoffeeManager coffeeManager;
    private IPlayerExperience playerExperience;

    private ExperienceConnecter experienceConnecter = new ExperienceConnecter();
    private CoffeeConnecter coffeeConnecter = new CoffeeConnecter();

    public Coffee_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent coffee = new Intent(getActivity(), CoffeeService.class);
        getActivity().startService(coffee);
        getActivity().bindService(coffee, coffeeConnecter, Context.BIND_AUTO_CREATE);

        Intent experience = new Intent(getActivity(), PlayerExperienceService.class);
        getActivity().startService(experience);
        getActivity().bindService(experience, experienceConnecter, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coffee_fragment, container, false);
        Button drink_button = (Button) view.findViewById(R.id.drink_coffee);
        drink_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drink();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(coffeeConnecter);
        getActivity().unbindService(experienceConnecter);
    }

    private void execute_drink() {
        int experience = 1;
        try {
            coffeeManager.drink();
            playerExperience.addExp(experience);
            Toast.makeText(getContext(), getResources().getQuantityString(R.plurals.coffee_drink_toast, experience, experience), Toast.LENGTH_SHORT).show();
        } catch (NullPointerException ex) {
            Log.e("coffee", ex.getMessage());
        }
    }

    private static final int PERMISSION_REQUEST_LOCATION = 0;

    private void drink() {

        if (getActivity().getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            execute_drink();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0) {
            switch (requestCode) {
                case PERMISSION_REQUEST_LOCATION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        execute_drink();
                    }
                    return;
                }
            }
        }
    }


    private class CoffeeConnecter implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            coffeeManager = ((CoffeeService.CoffeeBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    }

    private class ExperienceConnecter implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            playerExperience = ((PlayerExperienceService.ExperienceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    }
}
