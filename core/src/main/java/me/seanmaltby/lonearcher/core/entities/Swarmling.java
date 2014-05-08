package me.seanmaltby.lonearcher.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import me.seanmaltby.lonearcher.core.ai.*;

public class Swarmling extends MeleeEntity
{
	public Swarmling(Vector2 position, float direction, World b2World)
	{
		super("Puncher", position, direction, b2World);
		goals.add(new PlayerTargetGoal(this));
		goals.add(new MoveTowardsGoal(this));
		goals.add(new SeparationGoal(this));
		goals.add(new FaceTargetGoal(this));
		goals.add(new MeleeAttackGoal(this, 35));

		setAttribute(EntityAttribute.MAX_HEALTH, 5f);
		setAttribute(EntityAttribute.DAMAGE, 1f);
		setAttribute(EntityAttribute.SPEED, getAttributeFloat(EntityAttribute.SPEED) * 1.1f);
		setAttribute(EntityAttribute.SIZE, getAttributeFloat(EntityAttribute.SIZE) * .5f);
		setAttribute(EntityAttribute.WORTH, getAttributeFloat(EntityAttribute.WORTH) * .5f);
	}

	@Override
	protected String getCenterObjectName()
	{
		return "Body";
	}

	@Override
	protected String[] getWeaponBoneNames()
	{
		return new String[] {"LeftArm", "RightArm"};
	}
}
