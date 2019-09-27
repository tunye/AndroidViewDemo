package com.buaa.ct.core.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;

import com.buaa.ct.core.manager.RuntimeManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationUtil {
    public static boolean hasGeocoderService() {
        return Geocoder.isPresent();
    }

    public static void getCurLocation(final OnLocationListener locationListener) {
        getProviderLocation(LocationManager.NETWORK_PROVIDER, new OnLocationListener() {
            @Override
            public void getlocation(Location location) {
                locationListener.getlocation(location);
            }
        });
    }

    public static String getSuitableProvider() {
        LocationManager locationManager = (LocationManager) RuntimeManager.getInstance().getContext().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置定位精准度
        criteria.setAltitudeRequired(false);
        //是否要求海拔
        criteria.setBearingRequired(true);
        //是否要求方向
        criteria.setCostAllowed(false);
        //是否要求收费
        criteria.setSpeedRequired(true);
        //是否要求速度
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        //设置电池耗电要求
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
        //设置方向精确度
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
        //设置速度精确度
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        //设置水平方向精确度
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        //设置垂直方向精确度
        return locationManager.getBestProvider(criteria, true);
    }

    @SuppressLint("MissingPermission")
    public static void getProviderLocation(String provider, final OnLocationListener locationListener) {
        LocationManager locationManager = (LocationManager) RuntimeManager.getInstance().getContext().getSystemService(Context.LOCATION_SERVICE);
        Location curPos = locationManager.getLastKnownLocation(provider);
        if (curPos.getElapsedRealtimeNanos() > 3600 * 1000) {
            curPos = null;
        }
        if (curPos == null) {
            locationManager.requestSingleUpdate(provider, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locationListener.getlocation(location);
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
            }, Looper.getMainLooper());
        } else {
            locationListener.getlocation(curPos);
        }
    }

    public static Address getCurRegion(Location location) {
        Geocoder geocoder = new Geocoder(RuntimeManager.getInstance().getContext());
        if (hasGeocoderService() && location != null) {
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    if (!TextUtils.isEmpty(address.getLocality())) {
                        return address;
                    } else {
                        return null;
                    }
                }
            } catch (IOException e) {
            }
        }
        return null;
    }

    public static Address getRegionLocation(String feature) {
        Geocoder geocoder = new Geocoder(RuntimeManager.getInstance().getContext());
        if (hasGeocoderService()) {
            List<Address> addresses = new ArrayList<>();
            try {
                addresses = geocoder.getFromLocationName(feature, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!addresses.isEmpty()) {
                return addresses.get(0);
            }
        }
        return null;
    }

    public interface OnLocationListener {
        void getlocation(Location location);
    }
}
