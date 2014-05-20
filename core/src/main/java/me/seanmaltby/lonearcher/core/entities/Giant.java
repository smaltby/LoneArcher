package me.seanmaltby.lonearcher.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import me.seanmaltby.lonearcher.core.ai.*;

public class Giant extends MeleeEntity
{
	public Giant(Vector2 position, float direction, World b2World)
	{
		super("Puncher", position, direction, b2World);
		goals.add(new PlayerTargetGoal(this));
		goals.add(new MoveTowardsGoal(this));
		goals.add(new SeparationGoal(this));
		goals.add(new FaceTargetGoal(this));
		goals.add(new MeleeAttackGoal(this, 120));

		setAttribute(EntityAttribute.MAX_HEALTH, 40f);
		setAttribute(EntityAttribute.DAMAGE, 4f);
		setAttribute(EntityAttribute.KNOCKBACK, 10f);
		setAttribute(EntityAttribute.ATTACK_SPEED, getAttributeFloat(EntityAttribute.ATTACK_SPEED) * 1.25f);
		setAttribute(EntityAttribute.SPEED, getAttributeFloat(EntityAttribute.SPEED) * .8f);
		setAttribute(EntityAttribute.SIZE, getAttributeFloat(EntityAttribute.SIZE) * 2f);
		setAttribute(EntityAttribute.WORTH, getAttributeFloat(EntityAttribute.WORTH) * 4);
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
