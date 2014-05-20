package me.seanmaltby.lonearcher.core;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Drawer;
import com.brashmonkey.spriter.Loader;
import me.seanmaltby.lonearcher.core.screens.GameScreen;
import me.seanmaltby.lonearcher.core.screens.MenuScreen;

/**
 * Class for statically storing objects that can be easily accesed globally
 */
public class Global
{
	public static SpriteBatch batch;

	public static TextureAtlas atlas;

	public static Skin uiSkin;

	public static MenuScreen menuScreen;

	public static GameScreen gameScreen;

	public static Drawer<Sprite> drawer;

	public static Loader<Sprite> loader;

	public static Data data;

	public static InputMultiplexer inputMultiplexer;

	public static ParticleEffect fireEffect;

	public static ParticleEffect poisonEffect;

	public static final float WORLD_TO_BOX = 1/100f;

	public static final float BOX_TO_WORLD = 100f;

	public static final float BODY_RADIUS = 50f;

	public static final int VIRTUAL_WIDTH = 1136;

	public static final int VIRTUAL_HEIGHT = 640;

	public static Preferences settings;

	public static final String IPHONE_HORIZONTAL = "DefaultTilt.Horizontal";

	public static final String IPHONE_TILTED = "DefaultTilt.Tilted";

	public static final String IPHONE_VERTICAL = "DefaultTilt.Vertical";

	public static final String PRESS_TO_AIM = "Aim.Press";

	public static final String ANALOG_STICK_AIM = "Aim.AnalogStick";

	public static final String SOUND = "Sounds.Sound";

	public static final String MUSIC = "Sounds.Music";
}
