package com.coredump.synergyar.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by fabio on 12/8/15.
 */
public class Position3DComponent implements Component {
    public Vector3 currentPosition;
    public Vector3 oldPosition;
}
