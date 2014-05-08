package me.seanmaltby.lonearcher.core;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import me.seanmaltby.lonearcher.core.entities.Entity;

import java.util.*;

public class ParticleEffectManager
{
	private static Map<ParticleEffect, Entity> effects = new HashMap<>();

	public static void addEffect(ParticleEffect effect)
	{
		effects.put(effect, null);
	}

	public static void addEffect(ParticleEffect effect, Entity entity)
	{
		effects.put(effect, entity);
	}

	public static void render(float delta)
	{
		Global.batch.begin();
		Iterator<Map.Entry<ParticleEffect, Entity>> iter = effects.entrySet().iterator();
		while(iter.hasNext())
		{
			Map.Entry<ParticleEffect, Entity> entry = iter.next();
			ParticleEffect effect = entry.getKey();
			Entity entity = entry.getValue();
			if(effect.isComplete())
			{
				effect.dispose();
				iter.remove();
			} else
			{
				if(entity != null)
					effect.setPosition(entity.getPosition().x, entity.getPosition().y);
				effect.draw(Global.batch, delta);
			}
		}
		Global.batch.end();
	}
}
