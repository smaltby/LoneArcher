package me.seanmaltby.lonearcher.core;

import com.badlogic.gdx.physics.box2d.*;
import me.seanmaltby.lonearcher.core.entities.*;

public class GameContactListener implements ContactListener
{
	@Override
	public void beginContact(Contact contact)
	{
		//Check if either fixture is attacking the other, and deal damage as necessary
		checkDamage(contact.getFixtureA(), contact.getFixtureB());
		checkDamage(contact.getFixtureB(), contact.getFixtureA());
	}

	private void checkDamage(Fixture attacker, Fixture attacked)
	{
		//Can't damage non living entities
		if(!(attacked.getUserData() instanceof LivingEntity))
			return;
		LivingEntity entity = (LivingEntity) attacked.getUserData();
		if(attacker.getUserData() instanceof Projectile)
		{
			Projectile projectile = (Projectile) attacker.getUserData();

			//If the projectile isn't on the same side as the entity being attacked
			if(projectile.getOrigin() instanceof Player != entity instanceof Player)
			{
				projectile.damage(entity);
			}
		} else if(attacker.getUserData() instanceof MeleeEntity.Weapon)
		{
			MeleeEntity.Weapon weapon = (MeleeEntity.Weapon) attacker.getUserData();
			if(entity instanceof Player)
			{
				weapon.damage(entity);
			}
		}
	}

	@Override
	public void endContact(Contact contact)
	{

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{

	}
}
