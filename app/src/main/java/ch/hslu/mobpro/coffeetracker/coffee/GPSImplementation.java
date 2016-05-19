package ch.hslu.mobpro.coffeetracker.coffee;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;


public class GPSImplementation extends Service implements IGPS {

    private Location lastLocation;
    private LocationManager locationManager;
    private LocationListener locationListener = new GPSListener();

    private final IBinder gpsBinder = new GPSBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public Location getLocation() {
        return lastLocation;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return gpsBinder;
    }

    public class GPSBinder extends Binder {
        IGPS getService() {
            return GPSImplementation.this;
        }
    }

    private class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (LocationOptimizer.isBetterLocation(location, lastLocation)) {
                lastLocation = location;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
