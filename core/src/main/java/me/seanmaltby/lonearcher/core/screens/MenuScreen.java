package me.seanmaltby.lonearcher.core.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import me.seanmaltby.lonearcher.core.Global;
import me.seanmaltby.lonearcher.core.LoneArcher;

public class MenuScreen implements Screen
{
	private Stage stage;

	public MenuScreen()
	{
		stage = new Stage(new FitViewport(Global.VIRTUAL_WIDTH, Global.VIRTUAL_HEIGHT));
	}

	public void initializeGUI()
	{
		Table table = new Table(Global.uiSkin);
		table.setFillParent(true);
		table.setBackground(new TiledDrawable(Global.uiSkin.getRegion("CrackedTexture")));

		Image logo = new Image(Global.uiSkin, "Logo");
		table.add(logo);

		//Spacing between logo and start button
		table.row();
		table.add().height(table.getHeight() * .05f);

		table.row();

		Button startButton = new Button(Global.uiSkin, "startButton");
		startButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				LoneArcher.getInstance().setScreen(Global.gameScreen);
			}
		});
		table.add(startButton);

		stage.addActor(table);
	}

	@Override
	public void render(float delta)
	{
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		Global.inputMultiplexer.addProcessor(stage);
		initializeGUI();
	}

	@Override
	public void hide()
	{
		Global.inputMultiplexer.removeProcessor(stage);
		stage.clear();
	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void dispose()
	{

	}
}
