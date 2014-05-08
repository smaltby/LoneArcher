package me.seanmaltby.lonearcher.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import me.seanmaltby.lonearcher.core.ai.*;

public class Swordsman extends MeleeEntity
{
	public Swordsman(Vector2 position, float direction, World b2World)
	{
		super("Swordsman", position, direction, b2World);
		goals.add(new PlayerTargetGoal(this));
		goals.add(new MoveTowardsGoal(this));
		goals.add(new SeparationGoal(this));
		goals.add(new FaceTargetGoal(this));
		goals.add(new MeleeAttackGoal(this, 120f));

		setAttribute(EntityAttribute.MAX_HEALTH, 15f);
		setAttribute(EntityAttribute.DAMAGE, 5f);
		setAttribute(EntityAttribute.WORTH, getAttributeFloat(EntityAttribute.WORTH) * 2);
	}

	@Override
	protected String getCenterObjectName()
	{
		return "Body";
	}

	@Override
	protected String[] getWeaponBoneNames()
	{
		return new String[] {"SwordBone"};
	}
}