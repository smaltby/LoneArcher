package me.seanmaltby.lonearcher.core.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import me.seanmaltby.lonearcher.core.Global;

public class OptionsScreen implements Screen
{
	private Stage stage;

	public OptionsScreen()
	{
		stage = new Stage(new FitViewport(Global.VIRTUAL_WIDTH, Global.VIRTUAL_HEIGHT));
	}

	public void initializeGUI()
	{

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
