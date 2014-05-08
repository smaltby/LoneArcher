package me.seanmaltby.lonearcher.core.entities;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.brashmonkey.spriter.Player;
import me.seanmaltby.lonearcher.core.Global;
import me.seanmaltby.lonearcher.core.utils.Utils;

import java.util.Map;

/**
 * A basic entity that is updated and drawn on each pass of the rendering loop.
 * It contains a position, a direction, a sprite, and varios attributes that are intialized with default values and
 * can be edited later by subclasses. Note that not all attributes are necessarily used by subclasses.
 *
 * An entity's SpritePlayer should contain an animation called 'Idle', which serves as the default animation.
 */
public abstract class Entity
{
	private com.brashmonkey.spriter.Player sprite;

	private Body body;

	private Map<EntityAttribute, Object> attributes = EntityAttribute.getDefaultAttributes();

	private int layer = STANDARD_LAYER;

	public static final int PLAYER_LAYER = 0;
	public static final int STANDARD_LAYER = 1;
	public static final int PROJECTILE_LAYER = 2;

	public Entity(String animationName, Vector2 position, float direction, World b2World)
	{
		//-90 degrees because animation files were accidentally made offset 90 degrees
		direction -= MathUtils.PI / 2;

		//Initialize sprite
		sprite = new Player(Global.data.getEntity(animationName));
		sprite.setScale(getAttributeFloat(EntityAttribute.SIZE));
		sprite.setAngle(direction * MathUtils.radDeg);
		sprite.setPosition(position.x, position.y);

		body = createBody(b2World, Utils.toBox2dCoords(position), direction);

		//Finally, add the entity to the list of entities so it is updated and drawn periodically
		Global.gameScreen.addEntity(this);
	}

	/**
	 * Creates the body of the entity. Both the body and all it's fixtures should have their UserData set to this Entity.
	 * @param b2World	Box2D world
	 * @param position	initial position
	 * @param direction	initial direction
	 * @return			body of entity
	 */
	protected Body createBody(World b2World, Vector2 position, float direction)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.angle = direction;

		Body body = b2World.createBody(bodyDef);
		body.setUserData(this);

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(getAttributeFloat(EntityAttribute.SIZE) * Global.BODY_RADIUS * Global.WORLD_TO_BOX);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;

		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setUserData(this);

		circleShape.dispose();

		return body;
	}

	/**
	 * Updates the entity's body's size
	 */
	protected void updateBodySize()
	{
		for(Fixture fixture : body.getFixtureList())
		{
			fixture.getShape().setRadius(getAttributeFloat(EntityAttribute.SIZE) * Global.BODY_RADIUS * Global.WORLD_TO_BOX);
		}
	}

	/**
	 * This is called on every frame, before rendering.
	 * When overriden, make sure to call super.update(delta) so functionality of superclass isn't lost.
	 * @param delta		delta time
	 */
	public void update(float delta)
	{
		//Spriter intends for sprites to be run at 1000 frames per second, so multiply 1000 by delta time
		//and set that as the frame speed. Then, multiply by attack speed so the entity attacks correctly.
		sprite.speed = (int) (1000 * delta * getAttributeFloat(EntityAttribute.ATTACK_SPEED));
		//-90 because animation files were accidentally made offset 90 degrees
		sprite.setAngle(body.getAngle() * MathUtils.radDeg - 90);
		sprite.setPosition(getPosition().x, getPosition().y);
		sprite.update();
	}

	/**
	 * This is called on every frame, during rendering. It renders the entities animation.
	 * When overriden, make sure to call super.draw() so functionality of superclass isn't lost.
	 */
	public void draw(float delta)
	{
		Global.batch.setColor(getElement().getColor());
		Global.drawer.draw(sprite);
	}

	/**
	 * Kills the entity, so it is no longer updated or drawn, and so that all references to it are removed.
	 */
	public void kill()
	{
		Global.gameScreen.removeEntity(this);
	}

	/**
	 * Destroys the entity. This should generally not be called from anywhere but the GameScreen.
	 * Distinct from the kill method because it won't be called while contacts are being resolved or
	 * while the list of all entity's are being looped over, so it's free to do much more.
	 */
	public void destroy()
	{
		getWorld().destroyBody(body);
	}

	/**
	 * Damages another entity.
	 * @param other		entity to damage
	 */
	public void damage(LivingEntity other)
	{
		other.decrementHealth(getAttributeFloat(EntityAttribute.DAMAGE));
		getElement().damage(other);

		Body otherBody = other.getBody();
		Vector2 deltaPosition = new Vector2(otherBody.getPosition()).sub(body.getPosition());
		deltaPosition.nor().scl(getAttributeFloat(EntityAttribute.KNOCKBACK));
		otherBody.applyLinearImpulse(deltaPosition, otherBody.getWorldCenter(), true);
	}

	public void move(Vector2 direction, float speed)
	{
		//Get the velocities required to approach the desired velocity
		direction.nor();
		float xDesiredSpeed = direction.x * speed;
		float yDesiredSpeed = direction.y * speed;
		float xVelocity = Interpolation.linear.apply(body.getLinearVelocity().x, xDesiredSpeed, .1f) - body.getLinearVelocity().x;
		float yVelocity = Interpolation.linear.apply(body.getLinearVelocity().y, yDesiredSpeed, .1f) - body.getLinearVelocity().y;

		//Convert to the force required to approach that velocity
		float xImpulse = body.getMass() * xVelocity;
		float yImpulse = body.getMass() * yVelocity;

		//Apply that impulse
		body.applyLinearImpulse(new Vector2(xImpulse, yImpulse), body.getWorldCenter(), true);
	}

	public void move(Vector2 direction)
	{
		move(direction, getAttributeFloat(EntityAttribute.SPEED));
	}

	public void move(float angle, float speed)
	{
		move(new Vector2(MathUtils.cos(angle), MathUtils.sin(angle)), speed);
	}

	public void move(float angle)
	{
		move(angle, getAttributeFloat(EntityAttribute.SPEED));
	}

	protected void handleDirtyAttribute(EntityAttribute dirtyAttribute, Object oldValue)
	{
		if(dirtyAttribute.equals(EntityAttribute.SIZE))
		{
			sprite.setScale(getAttributeFloat(EntityAttribute.SIZE));

			updateBodySize();
		} else if(dirtyAttribute.equals(EntityAttribute.ELEMENT))
			getElement().applyElementAttributes(this);
	}

	public void setAttribute(EntityAttribute attribute, Object value)
	{
		Object oldValue =  attributes.get(attribute);
		attributes.put(attribute, value);
		handleDirtyAttribute(attribute, oldValue);
	}

	public void copyAttributes(Entity other)
	{
		for(Map.Entry<EntityAttribute, Object> entry : other.attributes.entrySet())
			setAttribute(entry.getKey(), entry.getValue());
	}

	@SuppressWarnings({"unchecked, unused"})
	public <T> T getAttribute(EntityAttribute attribute, Class<T> type)
	{
		return (T) attributes.get(attribute);
	}

	public Float getAttributeFloat(EntityAttribute attribute)
	{
		return getAttribute(attribute, Float.class);
	}

	public Integer getAttributeInteger(EntityAttribute attribute)
	{
		return getAttribute(attribute, Integer.class);
	}

	public Element getElement()
	{
		return getAttribute(EntityAttribute.ELEMENT, Element.class);
	}

	public com.brashmonkey.spriter.Player getSprite()
	{
		return sprite;
	}

	public World getWorld()
	{
		return body.getWorld();
	}

	public Body getBody()
	{
		return body;
	}

	public Vector2 getPosition()
	{
		return Utils.toWorldCoordinates(body.getPosition());
	}

	public void setPosition(Vector2 position)
	{
		getBody().setTransform(Utils.toBox2dCoords(position), body.getAngle());
	}

	public float getDirection()
	{
		return body.getAngle();
	}

	public void setDirection(float direction)
	{
		body.setTransform(body.getPosition().x, body.getPosition().y, direction);
	}

	public Integer getLayer()
	{
		return layer;
	}

	public void setLayer(int layer)
	{
		this.layer = layer;
	}
}
