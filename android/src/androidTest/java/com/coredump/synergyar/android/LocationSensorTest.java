package com.coredump.synergyar.android;

import com.coredump.synergyar.android.adapters.LocationAdapter;
import com.coredump.synergyar.android.adapters.LocationAdapterImpl;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Franz on 07/11/2015.
 */
public class LocationSensorTest extends ActivityInstrumentationTestCase2<SynergyActivity> {

    private LocationAdapter mLocationSensor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLocationSensor = new LocationAdapterImpl(getActivity().getContext());
    }

    public LocationSensorTest() {
        super(SynergyActivity.class);
    }

    public void testPreconditions() {
        assertNotNull("mLocationSensor is null", mLocationSensor);
    }

    public void testGetLocation() {
        assertNull("Last location is null", mLocationSensor.getLocation());
    }
}
