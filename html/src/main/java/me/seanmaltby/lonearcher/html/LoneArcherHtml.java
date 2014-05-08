package me.seanmaltby.lonearcher.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import me.seanmaltby.lonearcher.core.Global;
import me.seanmaltby.lonearcher.core.LoneArcher;

public class LoneArcherHtml extends GwtApplication
{
	@Override
	public void onModuleLoad()
	{
		super.onModuleLoad();
		centreGameDisplay();
		removeLoadingMessage();

		setVersionString();
		markCanvasLoaded();
		removeInternetExplorerNotice();
	}

	@Override
	public ApplicationListener getApplicationListener()
	{
		return new LoneArcher();
	}



	@Override
	public GwtApplicationConfiguration getConfig()
	{
		return new GwtApplicationConfiguration(Global.VIRTUAL_WIDTH, Global.VIRTUAL_HEIGHT);
	}

	private static void centreGameDisplay()
	{
		final Element tableElement = Document.get().getElementsByTagName("table").getItem(0);
		if (tableElement != null)
		{
			final String style = tableElement.getAttribute("style")
					+ " position: absolute;"
					+ " top: 50%; margin-top: -" + Global.VIRTUAL_HEIGHT / 2 + "px;"
					+ " left: 50%; margin-left: -" + Global.VIRTUAL_WIDTH / 2 + "px;";
			tableElement.setAttribute("style", style);
		}
	}

	private static void removeLoadingMessage()
	{
		final Element loadingElement = Document.get().getElementById("loading");
		if (loadingElement != null)
		{
			loadingElement.removeFromParent();
		}
	}

	private void setVersionString()
	{
		final Element versionElement = Document.get().getElementById("version");
		if (versionElement != null)
		{
			versionElement.setInnerText("1.0");
		}
	}

	private static void markCanvasLoaded()
	{
		final Element bodyElement = Document.get().getElementsByTagName("body").getItem(0);
		if (bodyElement != null)
		{
			bodyElement.removeAttribute("style");
			bodyElement.addClassName("loaded");
		}
	}

	private static void removeInternetExplorerNotice()
	{
		final Element ie10Element = Document.get().getElementById("ie10");
		if (ie10Element != null)
		{
			ie10Element.removeFromParent();
		}
	}
}
