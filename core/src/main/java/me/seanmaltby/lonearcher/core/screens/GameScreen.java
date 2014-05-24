package me.seanmaltby.lonearcher.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import me.seanmaltby.lonearcher.core.*;
import me.seanmaltby.lonearcher.core.entities.Entity;
import me.seanmaltby.lonearcher.core.entities.Player;
import me.seanmaltby.lonearcher.core.gui.GameHUD;

import java.util.*;

public class GameScreen implements Screen
{
	public static int GAME_WIDTH = 2000;
	public static int GAME_HEIGHT = 2000;

	private World b2World;
	private Stage stage;
	private GameHUD gameHUD;
	private WaveHandler waveHandler;

	private Player player;
	private Set<Entity> entities;

	//Keeps track of additions and removals of entities. This is needed because entities are often added while entites
	//are being updated, and directly adding these new entities to the set of entities would cause a concurrent modification.
	//The same goes for removals.
	private Set<Entity> entitiesToAdd;
	private Set<Entity> entitiesToRemove;

	private OrthographicCamera camera;

	private boolean pausedInternal = false;
	private boolean pausedExternal = false;

	public GameScreen()
	{
		stage = new Stage(new StretchViewport(Global.VIRTUAL_WIDTH, Global.VIRTUAL_HEIGHT));

		//Camera size is based off of aspect ratio to avoid stretching. Some devices may display more or less of the world
		//than others on the vertical axis as a result of this, but not by a significant amount.
		float aspectRatio = ((float) Gdx.graphics.getWidth()) / Gdx.graphics.getHeight();
		camera = new OrthographicCamera(Global.VIRTUAL_WIDTH, Global.VIRTUAL_WIDTH / aspectRatio);
	}

	public void newGame()
	{
		waveHandler = new WaveHandler(this);

		b2World = new World(new Vector2(0, 0), false);
		b2World.setContactListener(new GameContactListener());

		gameHUD = new GameHUD(stage);

		//Order entities by the layer they're in, so they get drawn in the correct order
		entities = new TreeSet<>(new Comparator<Entity>()
		{
			@Override
			public int compare(Entity o1, Entity o2)
			{
				int compare = o1.getLayer().compareTo(o2.getLayer());
				//Don't allow them to ever be equal, as sets don't allow duplicate elements
				if(compare == 0)
					compare = ((Integer) o1.hashCode()).compareTo(o2.hashCode());
				return compare;
			}
		});
		entitiesToAdd = new HashSet<>();
		entitiesToRemove = new HashSet<>();
		player = new Player(new Vector2(GAME_WIDTH / 2, GAME_HEIGHT / 2), 0, b2World);
		updateCamera();

		//Start music
		if(Global.settings.getBoolean(Global.MUSIC))
			Global.gameMusic1.play();

		//Start the game
		waveHandler.start();
	}

	private void updateCamera()
	{
		//Have the camera track the player, but clamp the camera's position to the bounds of the game,
		//ensuring it doesn't look outside those bounds
		float halfScreenWidth = (camera.viewportWidth * camera.zoom) / 2;
		float halfScreenHeight = (camera.viewportHeight * camera.zoom) / 2;
		float x = MathUtils.clamp(player.getPosition().x, halfScreenWidth, GAME_WIDTH - halfScreenWidth);
		float y = MathUtils.clamp(player.getPosition().y, halfScreenHeight, GAME_HEIGHT - halfScreenHeight);

		camera.position.set(x, y, 0);
		camera.update();

		//Set the batch's projection matrix so it draws correctly to the camera
		Global.batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render(float delta)
	{
		if(!(pausedInternal || pausedExternal))
		{
			waveHandler.update(delta);

			//Update world
			b2World.step(delta, 6, 2);
			//Update entities
			for (Entity entity : entities)
			{
				//Update entity
				entity.update(delta);
			}

			//Add and remove all necessary entities
			entities.addAll(entitiesToAdd);
			entitiesToAdd.clear();
			entities.removeAll(entitiesToRemove);
			for (Entity entity : entitiesToRemove)
				entity.destroy();
			entitiesToRemove.clear();

			updateCamera();
		}

		//Draw background
		drawBackground();

		//Draw entities
		Global.batch.begin();
		for(Entity entity : entities)
		{
			entity.draw(delta);
		}
		Global.batch.end();

		if(!(pausedInternal || pausedExternal))
		{
			//Update and draw particle effects
			ParticleEffectManager.render(delta);
		}

//		Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
//		debugRenderer.render(b2World, camera.combined.scl(Global.BOX_TO_WORLD));

		//Update and draw stage
		stage.act(delta);
		stage.draw();
	}

	private void drawBackground()
	{
		//Draw background, which is made up of a tiled texture that scrolls as the camera pans around
		Global.batch.begin();
		Sprite background = Art.getSprite("CrackedTexture");
		float halfScreenWidth = (camera.viewportWidth * camera.zoom) / 2;
		float halfScreenHeight = (camera.viewportHeight * camera.zoom) / 2;
		float minX = camera.position.x - halfScreenWidth;
		float minY = camera.position.y - halfScreenHeight;
		float maxX = camera.position.x + halfScreenWidth;
		float maxY = camera.position.y + halfScreenHeight;
		//Ensure that the background starts drawing at an interval divisible by the textures width and height, so that it scrolls correctly
		minX -= minX % background.getWidth();
		minY -= minY % background.getHeight();
		//Keep drawing the texture until the whole viewport has been drawn over
		for(float x = minX; x <= maxX; x+=background.getWidth())
		{
			for(float y = minY; y <= maxY; y+=background.getHeight())
			{
				background.setPosition(x, y);
				background.draw(Global.batch);
			}
		}
		Art.freeSprite(background);
		Global.batch.end();
	}

	/**
	 * Adds the entity to the list of entities so it is updated and drawn on each pass of the rendering loop.
	 * Generally, this is only used from the Entity class itself, which calls this method for itself in it's constructor.
	 * @param entity	entity to add
	 */
	public void addEntity(Entity entity)
	{
		entitiesToAdd.add(entity);
	}

	/**
	 * Remove the entity to the list of entities so it is no longer updated and drawn on each pass of the rendering loop.
	 * Generally, this is only used from the Entity class itself, which calls this method for itself in it's kill() method.
	 * @param entity	entity to remove
	 */
	public void removeEntity(Entity entity)
	{
		entitiesToRemove.add(entity);
	}

	public Player getPlayer()
	{
		return player;
	}

	public OrthographicCamera getCamera()
	{
		return camera;
	}

	public Set<Entity> getEntities()
	{
		return entities;
	}

	public Stage getStage()
	{
		return stage;
	}

	public World getWorld()
	{
		return b2World;
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		Global.inputMultiplexer.addProcessor(stage);
		newGame();
	}

	@Override
	public void hide()
	{
		stage.clear();
		Global.inputMultiplexer.removeProcessor(stage);
		Global.gameMusic1.stop();
	}

	/**
	 * Pause method for internal use. Needs to be seperate from the default pause method, as that can be resumed
	 * when the window is opened from a close state, or an app is reopened. This is not ideal if the game still has
     * has a window open that requires the game to be paused.
	 */
	public void pauseInternal()
	{
		pausedInternal = true;
	}

	/**
	 * Resume method for internal use. Needs to be seperate from the default resume method for the reasons described
	 * in the documentation of the pauseInternal method.
	 */
	public void resumeInternal()
	{
		pausedInternal = false;
	}

	@Override
	public void pause()
	{
		pausedExternal = true;
	}

	@Override
	public void resume()
	{
		pausedExternal = false;
	}

	@Override
	public void dispose()
	{

	}
}
