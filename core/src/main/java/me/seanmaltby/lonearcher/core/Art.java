package me.seanmaltby.lonearcher.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;
import java.util.Map;

public class Art
{
	private static Map<String, Pool<Sprite>> spritePools = new HashMap<String, Pool<Sprite>>();

	private static Map<Sprite, String> spriteNames = new HashMap<Sprite, String>();

	public static Pool<Sprite> getSpritePool(String name)
	{
		if(!spritePools.containsKey(name))
			spritePools.put(name, new SpritePool(name));
		return spritePools.get(name);
	}

	/**
	 * Gets the sprite from the texture atlas with the specified name.
	 * The sprite MUST be freed after it's usage is done.
	 * @param name	name of the sprite
	 * @return		sprite
	 */
	public static Sprite getSprite(String name)
	{
		return getSpritePool(name).obtain();
	}

	/**
	 * Frees the sprite, returning it to the sprite pool so it can be allocated again later.
	 * @param sprite	 the sprite
	 */
	public static void freeSprite(Sprite sprite)
	{
		getSpritePool(spriteNames.get(sprite)).free(sprite);
	}

	private static class SpritePool extends Pool<Sprite>
	{
		private final Sprite spriteReset;
		private final String spriteName;

		public SpritePool(String spriteName)
		{
			this.spriteName = spriteName;

			spriteReset = Global.atlas.createSprite(spriteName);
			if(spriteReset == null)
				throw new IllegalArgumentException("Sprite of name '"+spriteName+"' not found.");
		}

		@Override
		protected Sprite newObject()
		{
			return Global.atlas.createSprite(spriteName);
		}

		@Override
		public Sprite obtain()
		{
			Sprite sprite = super.obtain();
			//Map the sprite to it's name, which is used for freeing the sprite
			spriteNames.put(sprite, spriteName);
			return sprite;
		}

		@Override
		public void free(Sprite sprite)
		{
			super.free(sprite);
			//remove the sprite from the map, now that it's been freed
			spriteNames.remove(sprite);
			//Reset the sprite to default.
			sprite.set(spriteReset);
		}
	}
}
