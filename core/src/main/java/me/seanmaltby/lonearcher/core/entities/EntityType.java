package me.seanmaltby.lonearcher.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import me.seanmaltby.lonearcher.core.utils.Weighted;

public enum EntityType implements Weighted
{
	PUNCHER(1, 1), SWORDSMAN(2, .2f), GIANT(3, .125f), SWARMLING(3, .075f);

	private int minWave;
	private float weight;

	EntityType(int minWave, float weight)
	{
		this.minWave = minWave;
		this.weight = weight;
	}

	public int getMinWave()
	{
		return minWave;
	}

	public float getWeight()
	{
		return weight;
	}

	public LivingEntity createEntity(Vector2 position, float direction, World b2World)
	{
		switch(this)
		{
			case PUNCHER:
				return new Puncher(position, direction, b2World);
			case SWORDSMAN:
				return new Swordsman(position, direction, b2World);
			case GIANT:
				return new Giant(position, direction, b2World);
			case SWARMLING:
				return new Swarmling(position, direction, b2World);
			default:
				throw new RuntimeException("This can never occur");
		}
	}
}
