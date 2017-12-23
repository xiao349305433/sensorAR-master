package com.coredump.synergyar.android.sensors.geolocation;

import android.location.Location;
/**
 * @author francisco
 * @version 0.0,1
 * Created by Franz on 30/10/2015.
 */
public interface LocationSensorListener {
    void locationChanged(Location location);
}