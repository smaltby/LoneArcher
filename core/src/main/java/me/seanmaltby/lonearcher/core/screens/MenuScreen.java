package me.seanmaltby.lonearcher.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import me.seanmaltby.lonearcher.core.Global;
import me.seanmaltby.lonearcher.core.LoneArcher;

public class MenuScreen implements Screen
{
	private Stage stage;

	public MenuScreen()
	{
		//Sizes are based off of aspect ratio to avoid stretching. Some devices may display more or less of the world
		//than others on the vertical axis as a result of this, but not by a significant amount.
		float aspectRatio = ((float) Gdx.graphics.getWidth()) / Gdx.graphics.getHeight();
		stage = new Stage(new FillViewport(Global.VIRTUAL_WIDTH, Global.VIRTUAL_WIDTH / aspectRatio));
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
