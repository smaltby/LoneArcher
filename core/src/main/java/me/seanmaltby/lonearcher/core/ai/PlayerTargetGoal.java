package me.seanmaltby.lonearcher.core.ai;

import me.seanmaltby.lonearcher.core.Global;
import me.seanmaltby.lonearcher.core.entities.LivingEntity;

public class PlayerTargetGoal extends Goal
{
	private LivingEntity entity;

	public PlayerTargetGoal(LivingEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public void execute()
	{
		entity.setTarget(Global.gameScreen.getPlayer());
	}
}
