package kr.whatshoe.Util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by jaewoo on 2015-08-16.
 */
public class GPSListener implements LocationListener {
    private Context context;
    private Geocoder coder;
    private String currentloc="";
    private String currentCity="";
    private String currentLocality = "";
    private LocationManager manager;
    private Location location;
    private String provider;

    public GPSListener(Context context, LocationManager locationManager, String provider) {
        this.context = context;
        this.provider=provider;
        manager=locationManager;
        manager.requestLocationUpdates(provider, 1000, 0, this);

    }


    public String getCurrentLocation() {

        Location curLoc = manager.getLastKnownLocation(provider);
        currentloc = showLocationName(curLoc);
        return currentloc;
    }
    public String getCurrentCity() {
        Location curLoc = manager.getLastKnownLocation(provider);
        currentloc = showLocationName(curLoc);
        return currentCity;
    }
    public String getCurrentLocality() {
        Location curLoc = manager.getLastKnownLocation(provider);
        currentloc = showLocationName(curLoc);
        return currentLocality;
    }
    public Location getCurrentLocationData(){
        location = manager.getLastKnownLocation(provider);
        return location;
    }
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        currentloc=showLocationName(location);
        this.location = location;
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private String showLocationName(Location loc) {
        manager.requestSingleUpdate(provider, this,null);
        if (loc == null) {
            return null;
        }
        double latitude = loc.getLatitude();
        double longitude = loc.getLongitude();
        StringBuffer buff = new StringBuffer();
        coder = new Geocoder(context, Locale.KOREA);
        try {
            List<Address> addrs = coder.getFromLocation(latitude, longitude, 1);
            for (Address addr : addrs) {
                int index = addr.getMaxAddressLineIndex();
                for (int i = 0; i <= index; ++i) {
                    addr.getAddressLine(i);
                }
                currentCity = addr.getLocality();
                currentLocality = addr.getAdminArea();
                buff.append(addr.getThoroughfare());
            }
        } catch (IOException e) {
            Log.e("GPSListener.showLocationName", e.getStackTrace().toString());
        }
        return buff.toString();
    }
}
