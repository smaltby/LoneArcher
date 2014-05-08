package me.seanmaltby.lonearcher.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import me.seanmaltby.lonearcher.core.ai.*;

public class Puncher extends MeleeEntity
{
	public Puncher(Vector2 position, float direction, World b2World)
	{
		super("Puncher", position, direction, b2World);
		goals.add(new PlayerTargetGoal(this));
		goals.add(new MoveTowardsGoal(this));
		goals.add(new SeparationGoal(this));
		goals.add(new FaceTargetGoal(this));
		goals.add(new MeleeAttackGoal(this, 70f));

		setAttribute(EntityAttribute.DAMAGE, 2f);
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
