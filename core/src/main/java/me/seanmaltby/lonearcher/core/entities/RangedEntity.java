package me.seanmaltby.lonearcher.core.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public abstract class RangedEntity extends LivingEntity
{
	private Projectile nockedProjetile;
	private boolean nocked = false;
	private long lastFrame = 0;

	private boolean explosiveArrows = false;

	public RangedEntity(String animationName, Vector2 position, float direction, World b2World)
	{
		super(animationName, position, direction, b2World);
	}

	@Override
	public void attack()
	{
		nockedProjetile = createProjectile();
		nockProjectile(nockedProjetile, getDirection());
		nocked = true;
	}

	@Override
	public boolean isAttacking()
	{
		return nocked;
	}

	@Override
	public void update(float delta)
	{
		super.update(delta);
		if(nocked)
		{
			//Fire arrow as soon as the animation ends and begins looping around again
			if(lastFrame > getSprite().getTime())
			{
				fireArrows();
				nocked = false;
				lastFrame = 0;
			} else
			{
				//If the animation is still going, nock the arrow again to account for the entity moving/rotating
				nockProjectile(nockedProjetile, getDirection());
				lastFrame = getSprite().getTime();
			}
		}
	}

	protected abstract Projectile createProjectile();

	protected abstract int getProjectileDistanceFromSelf();

	private void nockProjectile(Projectile projectile, float angle)
	{
		if(explosiveArrows)
			projectile.setExplosive(true);

		float distFromEntity = getProjectileDistanceFromSelf() * getSprite().getScale();
		float deltaX = MathUtils.cos(angle) * distFromEntity;
		float deltaY = MathUtils.sin(angle) * distFromEntity;
		projectile.setPosition(new Vector2(getPosition()).add(deltaX, deltaY));
		projectile.setDirection(angle);
	}

	private void fireArrows()
	{
		nockedProjetile.fire();
		nockedProjetile = null;

		//Create and fire extra arrows at offset angles if this entity has multishot
		// - 1 because the first projectile just got fired
		int projectiles = getAttributeInteger(EntityAttribute.PROJECTILES) - 1;
		if(projectiles > 1)
		{
			float deltaAngle = 20f / (projectiles / 2f);
			float angle = 0f;
			for(int i = 0; i < projectiles; i++)
			{
				if(i % 2 == 0)
					if(angle > 0)
						angle += deltaAngle;
					else
						angle -= deltaAngle;
				else
					angle *= -1;
				Projectile projectile = createProjectile();
				nockProjectile(projectile, getDirection() + angle * MathUtils.degRad);
				projectile.fire();
			}
		}
	}

	@Override
	public void kill()
	{
		super.kill();
		if(nockedProjetile != null)
			nockedProjetile.kill();
	}

	public boolean isExplosiveArrows()
	{
		return explosiveArrows;
	}

	public void setExplosiveArrows(boolean explosiveArrows)
	{
		this.explosiveArrows = explosiveArrows;
	}
}
