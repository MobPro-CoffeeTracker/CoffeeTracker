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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import ch.hslu.mobpro.coffeetracker.R;
import ch.hslu.mobpro.coffeetracker.coffee.CoffeeService;
import ch.hslu.mobpro.coffeetracker.coffee.CoffeeStatisticService;
import ch.hslu.mobpro.coffeetracker.coffee.ICoffeeManager;
import ch.hslu.mobpro.coffeetracker.coffee.ICoffeeStatistic;

/**
 * A simple {@link Fragment} subclass.
 */
public class Maps_fragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap googleMap;
    private ICoffeeManager coffeeManager;
    private ICoffeeStatistic coffeeStatistic;
    private CoffeeConnector coffeeConnector = new CoffeeConnector();
    private CoffeeStatisticConnector coffeeStatisticConnector = new CoffeeStatisticConnector();

    public Maps_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent coffee = new Intent(getActivity(), CoffeeService.class);
        getActivity().startService(coffee);
        getActivity().bindService(coffee, coffeeConnector, Context.BIND_AUTO_CREATE);

        Intent statistic = new Intent(getActivity(), CoffeeStatisticService.class);
        getActivity().startService(statistic);
        getActivity().bindService(statistic, coffeeStatisticConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.maps_fragment, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        getActivity().unbindService(coffeeConnector);
        getActivity().unbindService(coffeeStatisticConnector);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private class CoffeeConnector implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            coffeeManager = ((CoffeeService.CoffeeBinder) service).getService();

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Iterator iterator = coffeeManager.getAllCoffee().keySet().iterator();
            while (iterator.hasNext()) {
                Date key = (Date) iterator.next();
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(coffeeManager.getAllCoffee().get(key).getLatitude(),
                                coffeeManager.getAllCoffee().get(key).getLongitude()))
                        .title(formatter.format(key)));
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    }

    private class CoffeeStatisticConnector implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            coffeeStatistic = ((CoffeeStatisticService.StatisticBinder) service).getService();
            Location location = coffeeStatistic.favoriteSpot();
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(13).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    }
}
