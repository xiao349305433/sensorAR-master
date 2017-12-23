package com.coredump.synergyar.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Created by fabio on 12/9/15.
 */
public class Model3DComponent implements Component, Poolable {
    public Model model;
    public float size;

    @Override
    public void reset() {
        size = 0.0f;
    }
}
