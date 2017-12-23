package com.coredump.synergyar.util;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.coredump.synergyar.entities.components.Position3DComponent;

import java.util.Comparator;

/**
 * Created by fabio on 12/9/15.
 */
public class ZCoordComparator implements Comparator<Entity>{
    private ComponentMapper<Position3DComponent> mPosition3DMapper;

    public ZCoordComparator() {
        mPosition3DMapper = ComponentMapper.getFor(Position3DComponent.class);
    }

    @Override
    public int compare(Entity e1, Entity e2) {
        return (int)Math.signum(mPosition3DMapper.get(e1).currentPosition.z - mPosition3DMapper.get(e2).currentPosition.z);
    }
}

