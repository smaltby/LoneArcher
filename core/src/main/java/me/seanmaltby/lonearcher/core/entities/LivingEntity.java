package me.seanmaltby.lonearcher.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import me.seanmaltby.lonearcher.core.Art;
import me.seanmaltby.lonearcher.core.Global;
import me.seanmaltby.lonearcher.core.ai.Goal;

import java.util.ArrayList;
import java.util.List;

/**
 * An entity that can be killed. It has health, that when dropped below 0, causes the entity to die and be removed from the game.
 * It also has an attack to deal damage to enemy living entities.
 *
 * A living entity's SpritePlayer should contain an animation called 'Attack' for when it attacks.
 */
public abstract class LivingEntity extends Entity
{
	private float health;
	private LivingEntity target;

	public List<Goal> goals = new ArrayList<Goal>();

	private float freezeTime;
	private float freezeImmunity;
	private float fireTime;
	private float poisonTime;
	private float poisonStrength;

	public LivingEntity(String animationName, Vector2 position, float direction, World b2World)
	{
		super(animationName, position, direction, b2World);
		health = getAttributeFloat(EntityAttribute.MAX_HEALTH);
		getElement().applyElementAttributes(this);
	}

	@Override
	public void update(float delta)
	{
		if(isAI())
		{
			for(Goal goal : goals)
			{
				goal.execute();
			}
		}
		if(freezeTime > 0)
		{
			freezeTime -= delta;
			if(freezeTime <= 0)
				freezeImmunity = 2;
			getBody().setLinearVelocity(0, 0);
		} else if(freezeImmunity > 0)
		{
			freezeImmunity -= delta;
		}
		if(fireTime > 0)
		{
			fireTime -= delta;
			decrementHealth(2 * delta);
		}
		if(poisonTime > 0)
		{
			poisonTime -= delta;
			decrementHealth(poisonStrength * delta);
		}
		super.update(delta);
	}

	@Override
	public void draw(float delta)
	{
		super.draw(delta);
		if(freezeTime > 0)
		{
			Sprite ice = Art.getSprite("Ice");
			ice.setScale(getAttributeFloat(EntityAttribute.SIZE));

			ice.setPosition(getPosition().x - ice.getWidth() / 2, getPosition().y - ice.getHeight() / 2);

			ice.draw(Global.batch);
			Art.freeSprite(ice);
		} else if(fireTime > 0)
		{
			//Utils.setParticleEffectScale(Global.fireEffect, getAttributeFloat(EntityAttribute.SIZE));
			Global.fireEffect.setPosition(getPosition().x, getPosition().y);
			Global.fireEffect.draw(Global.batch, delta);
		} else if(poisonTime > 0)
		{
			//Utils.setParticleEffectScale(Global.poisonEffect, getAttributeFloat(EntityAttribute.SIZE));
			Global.poisonEffect.setPosition(getPosition().x, getPosition().y);
			Global.poisonEffect.draw(Global.batch, delta);
		}
	}

	@Override
	protected void handleDirtyAttribute(EntityAttribute dirtyAttribute, Object oldValue)
	{
		if(dirtyAttribute.equals(EntityAttribute.MAX_HEALTH))
		{
			float healthRatio = health / (Float) oldValue;
			setHealth(healthRatio * getAttributeFloat(EntityAttribute.MAX_HEALTH));
		}
		super.handleDirtyAttribute(dirtyAttribute, oldValue);
	}

	@Override
	public void kill()
	{
		super.kill();
		if(!(this instanceof Player))
		{
			Global.gameScreen.getPlayer().addMoney(getAttributeFloat(EntityAttribute.WORTH));
		}
	}

	/**
	 * Sets the health of this entity. If the health of the entity is less than 0 afterwards, the entity dies.
	 * @param newHealth	new health of entity
	 */
	public void setHealth(float newHealth)
	{
		health = MathUtils.clamp(newHealth, 0, getAttributeFloat(EntityAttribute.MAX_HEALTH));
		if(!isAlive())
			kill();
	}

	public void decrementHealth(float lostHealth)
	{
		setHealth(health - lostHealth);
	}

	public void incrementHealth(float gainedHealth)
	{
		setHealth(health + gainedHealth);
	}

	public float getHealth()
	{
		return health;
	}

	public boolean isAlive()
	{
		return health > 0;
	}

	public abstract void attack();

	public abstract boolean isAttacking();

	public boolean isAI()
	{
		return true;
	}

	public void setTarget(LivingEntity target)
	{
		this.target = target;
	}

	public LivingEntity getTarget()
	{
		return target;
	}

	public float getFreezeTime()
	{
		return freezeTime;
	}

	public void freeze(float freeze)
	{
		if(freezeTime <= 0 && freezeImmunity <= 0 && fireTime <= 0)
			freezeTime = freeze;
	}

	public float getFireTime()
	{
		return fireTime;
	}

	public void addFireTime(float increment)
	{
		if(freezeTime > 0)
		{
			freezeTime = 0;
			freezeImmunity = 2;
		} else
			fireTime += increment;
	}

	public float getPoisonTime()
	{
		return poisonTime;
	}

	public void addPoisonTime(float increment)
	{
		poisonTime += increment;
	}

	public float getPoisonStrength()
	{
		return poisonStrength;
	}

	public void setPoisonStrength(float poisonStrength)
	{
		this.poisonStrength = poisonStrength;
	}
}
