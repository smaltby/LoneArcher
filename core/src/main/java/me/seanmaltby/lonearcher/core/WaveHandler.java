package me.seanmaltby.lonearcher.core;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import me.seanmaltby.lonearcher.core.entities.*;
import me.seanmaltby.lonearcher.core.gui.GameLostGUI;
import me.seanmaltby.lonearcher.core.gui.NextWaveGUI;
import me.seanmaltby.lonearcher.core.gui.UpgradeGUI;
import me.seanmaltby.lonearcher.core.screens.GameScreen;
import me.seanmaltby.lonearcher.core.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaveHandler
{
	private GameScreen gameScreen;

	private int wave = 0;

	private boolean running;

	private List<LivingEntity> enemies;
	private int toSpawn;
	/**	Spawn chance every 1/60 of a second (the max frame rate) */
	private float spawnChance;
	private float healthMod;
	private float damageMod;

	private UpgradeGUI upgradeGUI;

	public WaveHandler(GameScreen gameScreen)
	{
		this.gameScreen = gameScreen;
		enemies = new ArrayList<>();
		upgradeGUI = new UpgradeGUI(gameScreen.getStage(), this);
	}

	public void start()
	{
		running = true;
		startWave();
	}

	public void stop()
	{
		running = false;
		wave = 0;
		toSpawn = 0;
		enemies.clear();
		upgradeGUI.close();
	}

	public void update(float delta)
	{
		if(!running)
			return;

		if(!gameScreen.getPlayer().isAlive())
		{
			stop();
			new GameLostGUI(gameScreen.getStage());
			return;
		}
		//Remove dead enemies from list
		Iterator<LivingEntity> enemiesIter = enemies.iterator();
		while(enemiesIter.hasNext())
		{
			if(!enemiesIter.next().isAlive())
				enemiesIter.remove();
		}
		//If all enemies are dead and no more enemies are set to spawn this wave
		if(enemies.size() == 0 && toSpawn == 0)
		{
			endWave();
			return;
		}

		attemptSpawnEnemy(delta);
	}

	private void endWave()
	{
		running = false;
		gameScreen.pauseInternal();

		upgradeGUI.open();
	}

	public void startWave()
	{
		running = true;
		gameScreen.resumeInternal();

		wave++;
		new NextWaveGUI(Global.gameScreen.getStage(), wave);

		toSpawn = MathUtils.ceilPositive((float) (10 * Math.pow(wave, 1 / 2d)));
		//Logarthmic function, starts out at .01, rises rapidly originally, then slows down. At ~.025 by wave 10.
		spawnChance = (float) (.01 * Math.log(wave/2d + .5) + .01);

		healthMod = 1 + wave * .1f;
		healthMod *= healthMod;

		damageMod = 1 + wave * .05f;

		Player player = gameScreen.getPlayer();
		player.setHealth(player.getAttributeFloat(EntityAttribute.MAX_HEALTH));
	}

	private void attemptSpawnEnemy(float delta)
	{
		if(toSpawn == 0)
			return;
		//Adjust the spawn chance based on delta time, so that it acts as though the delta time were always 1/60 of a second
		float adjustedSpawnChance = spawnChance * (delta / (1/60f));
		if(Math.random() < adjustedSpawnChance)
		{
			Vector2 location = chooseSpawnLocation();
			LivingEntity enemy = chooseEnemy(location, 0, gameScreen.getWorld());

			//Swarmlings are spawned in swarms of 4
			if(enemy instanceof Swarmling)
			{
				for(int i = 0; i < 3; i++)
				{
					float deltaX = MathUtils.random(-10, 10);
					float deltaY = MathUtils.random(-10, 10);
					prepareEnemy(new Swarmling(new Vector2(location).add(deltaX, deltaY), 0, gameScreen.getWorld()));
				}
			}

			//Prepare the enemy and add decrease the number of entities still to spawn.
			//Note that a swarm of swarmlings still counts as one enemy
			prepareEnemy(enemy);
			toSpawn--;
		}
	}

	private void prepareEnemy(LivingEntity enemy)
	{
		enemy.setAttribute(EntityAttribute.ELEMENT, chooseElement());
		enemy.setAttribute(EntityAttribute.MAX_HEALTH, enemy.getAttributeFloat(EntityAttribute.MAX_HEALTH) * healthMod);
		enemy.setAttribute(EntityAttribute.DAMAGE, enemy.getAttributeFloat(EntityAttribute.DAMAGE) * damageMod);
		enemies.add(enemy);
	}

	private Vector2 chooseSpawnLocation()
	{
		switch((int) (Math.random() * 4))
		{
			case 0:
				//Random positon at bottom of map
				return new Vector2(MathUtils.random() * GameScreen.GAME_WIDTH, 0);
			case 1:
				//Random positon at left of map
				return new Vector2(0, MathUtils.random() * GameScreen.GAME_HEIGHT);
			case 2:
				//Random positon at top of map
				return new Vector2(MathUtils.random() * GameScreen.GAME_WIDTH, GameScreen.GAME_HEIGHT);
			case 3:
				//Random positon at right of map
				return new Vector2(GameScreen.GAME_WIDTH, MathUtils.random() * GameScreen.GAME_HEIGHT);
			default:
				return null;
		}
	}

	private LivingEntity chooseEnemy(Vector2 location, float angle, World world)
	{
		return Utils.chooseWeighted(EntityType.values(), wave).createEntity(location, angle, world);
	}

	private Element chooseElement()
	{
		return Utils.chooseWeighted(Element.values(), wave);
	}

	public boolean isRunning()
	{
		return running;
	}
}
