package me.seanmaltby.lonearcher.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.brashmonkey.spriter.Timeline;
import me.seanmaltby.lonearcher.core.utils.Utils;

import java.util.HashSet;
import java.util.Set;

public abstract class MeleeEntity extends LivingEntity
{
	private Weapon weapon;
	protected Body weaponBody;

	private long lastFrame = 0;

	public MeleeEntity(String animationName, Vector2 position, float direction, World b2World)
	{
		super(animationName, position, direction, b2World);
		weapon = new Weapon();
		weaponBody = createWeaponBody(b2World);
		updateWeaponPosition();
	}

	@Override
	public void update(float delta)
	{
		super.update(delta);
		updateWeaponPosition();
		if(lastFrame > getSprite().getTime())
		{
			getSprite().setAnimation("Idle");
			lastFrame = 0;
			weapon.endAttacking();
		} else
		{
			lastFrame = getSprite().getTime();
		}
	}

	@Override
	public void attack()
	{
		getSprite().setAnimation("Attack");
	}

	@Override
	public boolean isAttacking()
	{
		return getSprite().getAnimation().name.equals("Attack");
	}

	@Override
	public void destroy()
	{
		super.destroy();
		weaponBody.getWorld().destroyBody(weaponBody);
	}

	/**
	 * Creates the body representing this entity's weapon.
	 * Both the body and all it's fixtures should have their UserData set to this entity's weapon.
	 * Furthermore, all fixtures should generally be sensors.
	 * @param b2World	the Box2D world
	 * @return			body of the weapon
	 */
	protected Body createWeaponBody(World b2World)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;

		weaponBody = b2World.createBody(bodyDef);
		weaponBody.setUserData(weapon);

		PolygonShape polygonShape = new PolygonShape();

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		fixtureDef.shape = polygonShape;

		for(int i = 0; i < getWeaponBoneNames().length; i++)
		{
			Fixture fixture = weaponBody.createFixture(fixtureDef);
			fixture.setUserData(weapon);
		}

		polygonShape.dispose();

		return weaponBody;
	}

	/**
	 * Updates the entity's weapon's position to be correct. Called on every frame.
	 */
	protected void updateWeaponPosition()
	{
		weaponBody.setTransform(getBody().getPosition(), 0);

		Timeline.Key.Object body = getSprite().getObject(getCenterObjectName());
		Vector2 center = new Vector2(body.position.x, body.position.y);
		for(int i = 0; i < getWeaponBoneNames().length; i++)
		{
			String boneName = getWeaponBoneNames()[i];
			Timeline.Key.Bone bone = getSprite().getBone(boneName);
			float[] vertices = Utils.grahamScanSpriterObject(getSprite(), bone, center);

			Fixture fixture = weaponBody.getFixtureList().get(i);
			PolygonShape polygon = (PolygonShape) fixture.getShape();
			polygon.set(vertices);
		}
	}

	/**
	 * Gets the name of the spiter object in the center of the spriter player
	 * @return		name of spriter object
	 */
	protected abstract String getCenterObjectName();

	/**
	 * Gets the names of the spiter bones that should be used as weapons
	 * @return		array of names of spriter bones
	 */
	protected abstract String[] getWeaponBoneNames();

	/**
	 * Utility class that provides info on the weapon to the contact listeners when handling attacks.
	 * Provided as user data to the box2d body and fixtures used to represent the weapon(s) of this entity.
	 */
	public class Weapon
	{
		private Set<LivingEntity> alreadyDamaged = new HashSet<>();

		public void damage(LivingEntity other)
		{
			if(isAttacking() && !alreadyDamaged.contains(other))
			{
				MeleeEntity.this.damage(other);
				alreadyDamaged.add(other);
			}
		}

		private void endAttacking()
		{
			alreadyDamaged.clear();
		}
	}
}
