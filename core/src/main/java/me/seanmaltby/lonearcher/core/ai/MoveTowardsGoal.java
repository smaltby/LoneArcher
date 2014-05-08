package me.seanmaltby.lonearcher.core.ai;

import com.badlogic.gdx.math.MathUtils;
import me.seanmaltby.lonearcher.core.entities.LivingEntity;

public class MoveTowardsGoal extends Goal
{
	private LivingEntity entity;

	public MoveTowardsGoal(LivingEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public void execute()
	{
		LivingEntity target = entity.getTarget();
		if(target == null || !target.isAlive())
			return;

		float angle = MathUtils.atan2(target.getPosition().y - entity.getPosition().y, target.getPosition().x - entity.getPosition().x);
		entity.move(angle);
	}
}
