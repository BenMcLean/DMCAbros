package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Some code was copied from https://github.com/RoaringCatGames/libgdx-ashley-box2d-example
 */
public class Components {
    public static class TransformComponent implements Component {
        public final Vector3 position = new Vector3();
        public final Vector2 scale = new Vector2(1.0f, 1.0f);
        public float rotation = 0.0f;
        public boolean isHidden = false;
    }

    public static class BodyComponent implements Component {
        public Body body;
    }

    public static class TextureRegionComponent implements Component {
        public TextureRegion region = null;
    }
}
