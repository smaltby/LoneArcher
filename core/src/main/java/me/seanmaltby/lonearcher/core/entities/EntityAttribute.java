package me.seanmaltby.lonearcher.core.entities;

import java.util.HashMap;
import java.util.Map;

public enum EntityAttribute
{
	MAX_HEALTH(10f), DAMAGE(1f), ATTACK_SPEED(1f), SPEED(2.5f), SIZE(0.4f), PIERCING(1), ELEMENT(Element.NORMAL),
	PROJECTILES(1), KNOCKBACK(0f), WORTH(15f);

	private Object defaultValue;

	EntityAttribute(Object defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	private static Map<EntityAttribute, Object> defaultAttributes;
	static
	{
		defaultAttributes = new HashMap<>();
		for(EntityAttribute attribute : EntityAttribute.values())
			defaultAttributes.put(attribute, attribute.defaultValue);
	}

	public static Map<EntityAttribute, Object> getDefaultAttributes()
	{
		return new HashMap<>(defaultAttributes);
	}
}
