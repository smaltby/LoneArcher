package me.seanmaltby.lonearcher.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.seanmaltby.lonearcher.core.Global;
import me.seanmaltby.lonearcher.core.LoneArcher;

public class LoneArcherDesktop
{
	public static void main (String[] args)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Global.VIRTUAL_WIDTH;
		config.height = Global.VIRTUAL_HEIGHT;
		config.resizable = false;
		new LwjglApplication(new LoneArcher(), config);
	}
}
