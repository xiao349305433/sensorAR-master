package com.coredump.synergyar.android;

import android.test.ActivityInstrumentationTestCase2;

import com.coredump.synergyar.android.adapters.OrientationAdapter;
import com.coredump.synergyar.android.adapters.OrientationAdapterImpl;

/**
 * Created by fernando on 10-Nov-15.
 */
public class OrientationTest extends ActivityInstrumentationTestCase2<SynergyActivity> {
    private OrientationAdapter mOrientationSensor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mOrientationSensor = new OrientationAdapterImpl(getActivity().getContext());
    }

    public OrientationTest() {
        super(SynergyActivity.class);
    }

    public void testPreconditions() {
        assertNotNull("mOrientationSensor is null", mOrientationSensor);
    }

    public void testGetLocation() {
        assertNull("Orientation is null", mOrientationSensor.getOrientation());
    }
}
