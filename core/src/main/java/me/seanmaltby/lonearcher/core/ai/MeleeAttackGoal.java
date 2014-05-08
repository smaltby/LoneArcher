package me.seanmaltby.lonearcher.core.ai;

import me.seanmaltby.lonearcher.core.entities.LivingEntity;

public class MeleeAttackGoal extends Goal
{
	private LivingEntity entity;
	private float range;

	public MeleeAttackGoal(LivingEntity entity, float range)
	{
		this.entity = entity;
		this.range = range;
	}

	@Override
	public void execute()
	{
		if(entity.isAttacking())
			return;

		LivingEntity target = entity.getTarget();
		if(target == null || !target.isAlive())
			return;

		if(entity.getPosition().dst(target.getPosition()) < range)
		{
			entity.attack();
		}
	}
}
