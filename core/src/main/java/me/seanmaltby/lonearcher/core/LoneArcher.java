package me.seanmaltby.lonearcher.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.brashmonkey.spriter.SCMLReader;
import me.seanmaltby.lonearcher.core.screens.GameScreen;
import me.seanmaltby.lonearcher.core.screens.MenuScreen;
import com.brashmonkey.spriter.SpriterDrawer;
import com.brashmonkey.spriter.SpriterLoader;

import java.io.File;

public class LoneArcher extends Game
{
	private static LoneArcher instance;

	@SuppressWarnings("unchecked")
	@Override
	public void create()
	{
		instance = this;

		//Texture related globals
		Global.batch = new SpriteBatch();
		Global.atlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));
		Global.uiSkin = new Skin(Gdx.files.internal("uiskin.json"), Global.atlas);

		//Create the global input multiplexer
		Global.inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(Global.inputMultiplexer);

		//Initialize spriter and the entity animatiom
		SCMLReader reader = new SCMLReader(Gdx.files.internal("Entity.scml").read());
		Global.data = reader.getData();
		Global.loader = new SpriterLoader(Global.data);
		Global.loader.load(new File("Entity.scml"));
		Global.drawer = new SpriterDrawer(Global.loader, Global.batch, new ShapeRenderer());

		//Initialize screens
		Global.menuScreen = new MenuScreen();
		Global.gameScreen = new GameScreen();

		//Load particle effects
		Global.fireEffect = new ParticleEffect();
		Global.fireEffect.load(Gdx.files.internal("effects/fire.par"), Gdx.files.internal("effects"));
		Global.poisonEffect = new ParticleEffect();
		Global.poisonEffect.load(Gdx.files.internal("effects/poison.par"), Gdx.files.internal("effects"));

		setScreen(Global.menuScreen);
	}

	@Override
	public void render()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	@Override
	public void setScreen(Screen screen)
	{
		super.setScreen(screen);
	}

	public static LoneArcher getInstance()
	{
		return instance;
	}
}
