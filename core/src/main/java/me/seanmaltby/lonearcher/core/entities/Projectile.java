package me.seanmaltby.lonearcher.core.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import me.seanmaltby.lonearcher.core.Global;
import me.seanmaltby.lonearcher.core.ParticleEffectManager;
import me.seanmaltby.lonearcher.core.screens.GameScreen;
import me.seanmaltby.lonearcher.core.utils.GetNearbyQueryCallBack;

/**
 * A projectile is an entity that is fired from another entity, and will keep moving until it hits an enemy or goes off the screen.
 * Upon hitting an enemy, the enemy is damaged and the projectile dies.
 *
 * Upon intialization, the projectile will remain motionless and unable to cause damage.
 * To be fired and begin moving, the fire() method has to be called. This allows the projectile to initally be shown prior
 * to being fired, so for example, you could show an arrow loaded into a bow prior to being launched.
 *
 * A projectiles SpritePlayer should contain an animation called 'Fired' for after it's been fired.
 */
public class Projectile extends Entity
{
	private RangedEntity origin;
	private boolean fired = false;
	private int piercesLeft;

	private boolean explosive = false;

	public Projectile(String animationName, Vector2 position, float direction, World b2World, RangedEntity origin)
	{
		super(animationName, position, direction, b2World);
		setLayer(Projectile.PROJECTILE_LAYER);

		this.origin = origin;
		for(Fixture fixture : getBody().getFixtureList())
			fixture.setSensor(true);
		copyAttributes(origin);
		setAttribute(EntityAttribute.SPEED, 15f);

		//We have to reset the body shape to account for a change in size caused by the attribute copying
		PolygonShape shape = (PolygonShape) getBody().getFixtureList().get(0).getShape();
		shape.setAsBox(80 * Global.WORLD_TO_BOX * getAttributeFloat(EntityAttribute.SIZE),
				11 * Global.WORLD_TO_BOX * getAttributeFloat(EntityAttribute.SIZE));

		piercesLeft = getAttributeInteger(EntityAttribute.PIERCING);
	}

	@Override
	protected Body createBody(World b2World, Vector2 position, float direction)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.angle = direction;

		Body body = b2World.createBody(bodyDef);
		body.setUserData(this);

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(80 * Global.WORLD_TO_BOX * getAttributeFloat(EntityAttribute.SIZE),
				11 * Global.WORLD_TO_BOX * getAttributeFloat(EntityAttribute.SIZE));

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;

		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setUserData(this);

		polygonShape.dispose();

		return body;
	}

	@Override
	protected void updateBodySize()
	{
		for(Fixture fixture : getBody().getFixtureList())
		{
			PolygonShape polygonShape = (PolygonShape) fixture.getShape();
			polygonShape.setAsBox(80 * Global.WORLD_TO_BOX * getAttributeFloat(EntityAttribute.SIZE),
					11 * Global.WORLD_TO_BOX * getAttributeFloat(EntityAttribute.SIZE));
		}
	}

	@Override
	public void update(float delta)
	{
		if(fired)
			move(getDirection());
		super.update(delta);
		//Allow the projectile to go slightly off map before destruction so it doesn't appear as though it disappears when it hits the edge of the screen
		if(getPosition().x < -100 || getPosition().x > GameScreen.GAME_WIDTH + 100 || getPosition().y < -100 || getPosition().y > GameScreen.GAME_HEIGHT + 100)
		{
			kill();
		}
	}

	public void fire()
	{
		fired = true;
		getSprite().setAnimation("Fired");
	}

	@Override
	public void damage(LivingEntity other)
	{
		if(!fired)
			return;
		super.damage(other);
		if(explosive)
		{
			explosive = false;
			ParticleEffect explosion = new ParticleEffect();
			explosion.load(Gdx.files.internal("effects/explosion.par"), Gdx.files.internal("effects"));
			explosion.setPosition(getPosition().x, getPosition().y);
			explosion.start();
			ParticleEffectManager.addEffect(explosion);

			//Get nearby enemies and deal appropriate damage
			float radius = getBody().getFixtureList().get(0).getShape().getRadius() * 8;
			Vector2 position = getBody().getPosition();
			Array<Fixture> ignore = new Array<>(getBody().getFixtureList());
			ignore.addAll(other.getBody().getFixtureList());
			GetNearbyQueryCallBack queryCallBack = new GetNearbyQueryCallBack(ignore);
			getWorld().QueryAABB(queryCallBack, position.x - radius, position.y - radius, position.x + radius, position.y + radius);

			for(Body nearby : queryCallBack.bodies)
			{
				if(!(nearby.getUserData() instanceof LivingEntity))
					continue;
				LivingEntity nearbyEntity = (LivingEntity) nearby.getUserData();
				//Decrement health based on the distance from the source of the explosion
				float distance = other.getBody().getPosition().dst(nearby.getPosition());
				if(distance > radius)
					continue;
				float damage = (1 - (distance / radius)) * getAttributeFloat(EntityAttribute.DAMAGE);
				nearbyEntity.decrementHealth(damage);
			}

			if(Global.settings.getBoolean(Global.SOUND))
				Global.explosion.play();
		}
		if(Global.settings.getBoolean(Global.SOUND))
			Global.arrowHit.play();

		piercesLeft--;
		if(piercesLeft <= 0)
			kill();
	}

	public RangedEntity getOrigin()
	{
		return origin;
	}

	public boolean isFired()
	{
		return fired;
	}

	public boolean isExplosive()
	{
		return explosive;
	}

	public void setExplosive(boolean explosive)
	{
		this.explosive = explosive;
	}
}
