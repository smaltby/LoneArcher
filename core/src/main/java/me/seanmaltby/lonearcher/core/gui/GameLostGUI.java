package me.seanmaltby.lonearcher.core.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import me.seanmaltby.lonearcher.core.Global;
import me.seanmaltby.lonearcher.core.LoneArcher;

public class GameLostGUI
{
	private Stage stage;
	private Table table;

	public GameLostGUI(Stage stage)
	{
		this.stage = stage;
		open();
	}

	private void open()
	{
		table = new Table(Global.uiSkin);
		table.setFillParent(true);

		stage.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				LoneArcher.getInstance().setScreen(Global.menuScreen);
			}
		});

		Label youLoseLabel = new Label("You Lose", Global.uiSkin, "hobbyOfNight-Large");
		table.add(youLoseLabel);

		stage.addActor(table);
	}

	public void close()
	{
		table.remove();
	}
}
