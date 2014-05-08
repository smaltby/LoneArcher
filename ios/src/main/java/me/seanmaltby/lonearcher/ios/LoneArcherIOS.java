package me.seanmaltby.lonearcher.ios;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import me.seanmaltby.lonearcher.core.LoneArcher;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

public class LoneArcherIOS extends IOSApplication.Delegate
{
	public static void main(String[] args)
	{
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(args, null, LoneArcherIOS.class);
		pool.close();
	}

	@Override
	protected IOSApplication createApplication()
	{
		IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		config.orientationLandscape = true;
		config.orientationPortrait = false;
		return new IOSApplication(new LoneArcher(), config);
	}
}
