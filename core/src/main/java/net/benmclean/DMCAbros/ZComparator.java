package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import java.util.Comparator;

/**
 * Some code was copied from https://github.com/RoaringCatGames/libgdx-ashley-box2d-example
 */
public class ZComparator implements Comparator<Entity> {
    private ComponentMapper<Components.TransformC> transformM;

    public ZComparator() {
        transformM = ComponentMapper.getFor(Components.TransformC.class);
    }

    @Override
    public int compare(Entity entityA, Entity entityB) {
        return (int) Math.signum(transformM.get(entityB).z -
                transformM.get(entityA).z);
    }
}
