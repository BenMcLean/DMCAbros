package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

/**
 * Some code was copied from https://www.gamedevelopment.blog/ashley-and-box2d-tutorial/
 */
public class CollisionSystem extends IteratingSystem {
    ComponentMapper<Components.CollisionC> cm;
    ComponentMapper<Components.PlayerC> pm;

    @SuppressWarnings("unchecked")
    public CollisionSystem() {
        // only need to worry about player collisions
        super(Family.all(Components.CollisionC.class, Components.PlayerC.class).get());

        cm = ComponentMapper.getFor(Components.CollisionC.class);
        pm = ComponentMapper.getFor(Components.PlayerC.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // get player collision component
        Components.CollisionC cc = cm.get(entity);

        Entity collidedEntity = cc.collisionEntity;
        if (collidedEntity != null) {
            Components.TypeC type = collidedEntity.getComponent(Components.TypeC.class);
            if (type != null) {
                switch (type) {
                    case Mob:
                        //do player hit enemy thing
                        System.out.println("player hit enemy");
                        break;
                    case Brick:
                        //do player hit brick thing
                        System.out.println("player hit brick");
                        break;
                }
                cc.collisionEntity = null; // collision handled reset component
            }
        }
    }
}
