package ch.hslu.mobpro.coffeetracker.coffee;


import android.location.Location;

class LocationOptimizer {

    public static final int TIME_OUT = 1000 * 60 * 2;

    public static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public static long calculateTimeDelta(Location newLocation, Location oldLocation) {
        return newLocation.getTime() - oldLocation.getTime();
    }

    public static boolean isNewer(Location newLocation, Location oldLocation) {
        return calculateTimeDelta(newLocation, oldLocation) > 0;
    }

    public static boolean isSignificantlyNewer(Location newLocation, Location oldLocation) {
        return calculateTimeDelta(newLocation, oldLocation) > TIME_OUT;
    }

    public static boolean isSignificantlyOlder(Location newLocation, Location oldLocation) {
        return calculateTimeDelta(newLocation, oldLocation) < -TIME_OUT;
    }

    public static float calculateAccuracyDelta(Location newLocation, Location oldLocation) {
        return (newLocation.getAccuracy() - oldLocation.getAccuracy());
    }

    public static boolean isLessAccurate(Location newLocation, Location oldLocation) {
        return calculateAccuracyDelta(newLocation, oldLocation) > 0;
    }

    public static boolean isMoreAccurate(Location newLocation, Location oldLocation) {
        return calculateAccuracyDelta(newLocation, oldLocation) < 0;
    }

    public static boolean isSignificantlyLessAccurate(Location newLocation, Location oldLocation) {
        return calculateAccuracyDelta(newLocation, oldLocation) > 200;
    }

    public static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null)
            return true;

        if (isSignificantlyNewer(location, currentBestLocation) ||
                isMoreAccurate(location, currentBestLocation) ||
                isNewer(location, currentBestLocation) && !isLessAccurate(location, currentBestLocation) ||
                isNewer(location, currentBestLocation) && !isSignificantlyLessAccurate(location, currentBestLocation) && isSameProvider(location.getProvider(),
                        currentBestLocation.getProvider())) {
            // A new location is always better than no location
            // If it's been more than two minutes since the current location, use the new location
            // because the user has likely moved

            return !isSignificantlyOlder(location, currentBestLocation);
        }
        return false;
    }
}
