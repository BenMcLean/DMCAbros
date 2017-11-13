package net.benmclean.DMCAbros;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import net.benmclean.utils.AtlasRepacker;
import net.benmclean.utils.PaletteShader;

public class Assets implements Disposable {
    public static final int transparent = Color.rgba8888(0f, 0f, 0f, 0f);
    public static final int sky = Color.rgba8888(93 / 255f,148 / 255f,251 / 255f, 1f);
    public static final int brickDark = Color.rgba8888(204 / 255f,75 / 255f,9 / 255f, 1f);
    public static final int brickLight = Color.rgba8888(1f,186 / 255f,170 / 255f, 1f);
    public static final int bushes = Color.rgba8888(18 / 255f,124 / 255f,34 / 255f, 1f);
    public static final int clouds = Color.rgba8888(219 / 255f,252 / 255f,1f,1f);
    public static final int TILE_WIDTH = 16;
    public static final int TILE_HEIGHT = 16;
    public Texture one;
    public TextureAtlas atlas;
    public Skin skin;
    public ShaderProgram shader;
    public PaletteShader grey;

    public Assets() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.drawPixel(0, 0, -1);
        one = new Texture(pixmap);
        one.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        pixmap.dispose();

        shader = PaletteShader.makeShader();
        grey = new PaletteShader(new Color[]{Color.BLACK, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.WHITE}, shader);

        TextureAtlas dosAtlas = new TextureAtlas(Gdx.files.internal("DOS/uiskin.atlas"));
        skin = new Skin(
                Gdx.files.internal("DOS/uiskin.json"),
                AtlasRepacker.repackAtlas(
                        dosAtlas,
                        grey
                )
        );
        dosAtlas.dispose();

        atlas = new TextureAtlas("art.atlas");
    }

    public void dispose() {
        one.dispose();
        grey.dispose();
        shader.dispose();
        atlas.dispose();
    }

    public enum Character {
        Astronaut("Astronaut"),
        Blob("Blob"),
        Boy("Boy"),
        Chinese("Chinese"),
        Girl("Girl"),
        Knight("Knight"),
        Man("Man"),
        Skeleton("Skeleton"),
        Snake("Snake"),
        Spectre("Spectre"),
        Woman("Woman");
        private String string;

        Character(String string) {
            this.string = string;
        }

        public String toString() {
            return string;
        }
    }
}
