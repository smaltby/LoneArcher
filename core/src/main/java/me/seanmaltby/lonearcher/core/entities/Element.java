package me.seanmaltby.lonearcher.core.entities;

import com.badlogic.gdx.graphics.Color;
import me.seanmaltby.lonearcher.core.utils.Weighted;

public enum Element implements Weighted
{
	NORMAL(new Color(0, 0.15f, 0, 1), 1, 1), POWER(new Color(0.37f, 0, 0, 1), 2, .1f), FAST(new Color(0, 0.47f, 0, 1), 3, .125f),
	POISON(new Color(0.29f, 0.78f, 0, 1), 5, .04f), ICE(new Color(0, 1, 1, 1), 5, .04f), FIRE(new Color(1, 0.29f, 0, 1), 5, .04f),
	DARK(new Color(0.1f, 0.1f, 0.1f, 1), 7, .02f);

	private Color color;
	private int minWave;
	private float weight;

	Element(Color color, int minWave, float weight)
	{
		this.color = color;
		this.minWave = minWave;
		this.weight = weight;
	}

	public Color getColor()
	{
		return color;
	}

	public int getMinWave()
	{
		return minWave;
	}

	public float getWeight()
	{
		return weight;
	}

	public void applyElementAttributes(Entity entity)
	{
		switch(this)
		{
			case NORMAL:
				break;
			case FAST:
				entity.setAttribute(EntityAttribute.MAX_HEALTH, entity.getAttributeFloat(EntityAttribute.MAX_HEALTH) * 1.5f);
				entity.setAttribute(EntityAttribute.SPEED, entity.getAttributeFloat(EntityAttribute.SPEED) * 1.3f);
				break;
			case POWER:
				entity.setAttribute(EntityAttribute.MAX_HEALTH, entity.getAttributeFloat(EntityAttribute.MAX_HEALTH) * 2.5f);
				entity.setAttribute(EntityAttribute.DAMAGE, entity.getAttributeFloat(EntityAttribute.DAMAGE) * 1.4f);
				entity.setAttribute(EntityAttribute.SPEED, entity.getAttributeFloat(EntityAttribute.SPEED) * .9f);
				break;
			case POISON:
				entity.setAttribute(EntityAttribute.MAX_HEALTH, entity.getAttributeFloat(EntityAttribute.MAX_HEALTH) * 2f);
				entity.setAttribute(EntityAttribute.SPEED, entity.getAttributeFloat(EntityAttribute.SPEED) * 1.2f);
				break;
			case ICE:
				entity.setAttribute(EntityAttribute.MAX_HEALTH, entity.getAttributeFloat(EntityAttribute.MAX_HEALTH) * 2f);
				entity.setAttribute(EntityAttribute.SPEED, entity.getAttributeFloat(EntityAttribute.SPEED) * 1.2f);
				break;
			case FIRE:
				entity.setAttribute(EntityAttribute.MAX_HEALTH, entity.getAttributeFloat(EntityAttribute.MAX_HEALTH) * 2f);
				entity.setAttribute(EntityAttribute.SPEED, entity.getAttributeFloat(EntityAttribute.SPEED) * 1.2f);
				break;
			case DARK:
				entity.setAttribute(EntityAttribute.MAX_HEALTH, entity.getAttributeFloat(EntityAttribute.MAX_HEALTH) * 4f);
				entity.setAttribute(EntityAttribute.DAMAGE, entity.getAttributeFloat(EntityAttribute.DAMAGE) * 1.5f);
				entity.setAttribute(EntityAttribute.SPEED, entity.getAttributeFloat(EntityAttribute.SPEED) * 1.3f);
				entity.setAttribute(EntityAttribute.KNOCKBACK, (entity.getAttributeFloat(EntityAttribute.SPEED) + 5f) * 1.5f);
				break;
		}
	}

	public void damage(LivingEntity other)
	{
		switch(this)
		{
			case NORMAL:
				break;
			case FAST:
				break;
			case POWER:
				break;
			case POISON:
				other.addPoisonTime(6);
				other.setPoisonStrength(1);
				break;
			case ICE:
				other.freeze(2);
				break;
			case FIRE:
				other.addFireTime(4);
				break;
			case DARK:
				break;
		}
	}
}
