package com.coredump.synergyar.entities.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Franz on 17/11/2015.
 */
public class PositionSystem extends EntitySystem {

    public double getDistanceBetweenObjects3DSpace(Vector3 modelA, Vector3 modelB) {

        double distance = Math.sqrt(Math.pow(modelA.x - modelB.x, 2) +
                                    Math.pow(modelA.y - modelB.y, 2) +
                                    Math.pow(modelA.z - modelB.z, 2));
        return distance;
    }

    public double getDistanceBetweenObjects2DSpace(Vector2 modelA, Vector2 modelB) {

        double distance = Math.sqrt(Math.pow(modelA.x - modelB.x, 2) +
                                    Math.pow(modelA.y - modelB.y, 2));
        return distance;
    }
}
