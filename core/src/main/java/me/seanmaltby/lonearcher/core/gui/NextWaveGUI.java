package me.seanmaltby.lonearcher.core.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import me.seanmaltby.lonearcher.core.Global;

public class NextWaveGUI
{
	private Stage stage;
	private int wave;
	private Table table;

	public NextWaveGUI(Stage stage, int wave)
	{
		this.stage = stage;
		this.wave = wave;
		open();
	}

	private void open()
	{
		table = new Table(Global.uiSkin);
		table.setFillParent(true);

		final Label waveLabel = new Label("Wave "+wave, Global.uiSkin, "hobbyOfNight-Large");
		table.add(waveLabel);

		stage.addActor(table);

		final int repeatCount = 30;
		Timer.schedule(new Timer.Task()
		{
			int count = 0;
			@Override
			public void run()
			{
				count++;
				Color color = waveLabel.getColor();
				waveLabel.setColor(color.r, color.g, color.b, color.a - (((float)count)/repeatCount));
			}
		}, 2f, .1f, repeatCount);

		Timer.schedule(new Timer.Task()
		{
			@Override
			public void run()
			{
				close();
			}
		}, 5f);
	}

	public void close()
	{
		table.remove();
	}
}
