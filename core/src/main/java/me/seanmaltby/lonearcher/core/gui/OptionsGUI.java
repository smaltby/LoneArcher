package me.seanmaltby.lonearcher.core.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import me.seanmaltby.lonearcher.core.Global;

public class OptionsGUI
{
	private Stage stage;

	private Window window;

	private boolean initialized = false;

	public OptionsGUI(Stage stage)
	{
		this.stage = stage;

		window = new Window("Options", Global.uiSkin);
	}

	public void open()
	{
		if(!initialized)
			initializeGUI();
		stage.addActor(window);
	}

	public void close()
	{
		if(Global.gameScreen.getWaveHandler().isRunning())
			Global.gameScreen.resumeInternal();
		window.remove();
	}

	private void initializeGUI()
	{
		window.setTitleAlignment(Align.center);

		window.setWidth(stage.getWidth() * .95f);
		window.setHeight(stage.getHeight() * .95f);
		window.setX(stage.getWidth() / 2f - window.getWidth() / 2f);
		window.setY(stage.getHeight() / 2f - window.getHeight() / 2f);

		Table table = new Table(Global.uiSkin);
		table.setFillParent(true);
		table.defaults().center().space(15f);
		Table backButtonTable = new Table(Global.uiSkin);
		backButtonTable.setFillParent(true);
		backButtonTable.top().left();

		createDefaultTiltChoices(table);
		createAimOptions(table);
		createSoundButtons(table);

		createBackButton(backButtonTable);

		window.addActor(table);
		window.addActor(backButtonTable);
	}

	private void createDefaultTiltChoices(Table table)
	{
		Label label = new Label("Angle to Hold Device At", Global.uiSkin, "hobbyOfNight-30");

		Label flatLabel = new Label("Flat", Global.uiSkin, "hobbyOfNight-20");
		Label tiltedLabel = new Label("Tilted", Global.uiSkin, "hobbyOfNight-20");
		Label verticalLabel = new Label("Vertical", Global.uiSkin, "hobbyOfNight-20");

		final Button flatChoice = new Button(Global.uiSkin, "radio");
		flatChoice.setChecked(Global.settings.getBoolean(Global.IPHONE_HORIZONTAL));
		final Button tiltedChoice = new Button(Global.uiSkin, "radio");
		tiltedChoice.setChecked(Global.settings.getBoolean(Global.IPHONE_TILTED));
		final Button verticalChoice = new Button(Global.uiSkin, "radio");
		verticalChoice.setChecked(Global.settings.getBoolean(Global.IPHONE_VERTICAL));
		new ButtonGroup(flatChoice, tiltedChoice, verticalChoice);

		Image iphoneFlat = new Image(Global.uiSkin, "IPhoneFlat");
		iphoneFlat.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);
				flatChoice.setChecked(true);
				Global.settings.putBoolean(Global.IPHONE_HORIZONTAL, true);
				Global.settings.putBoolean(Global.IPHONE_TILTED, false);
				Global.settings.putBoolean(Global.IPHONE_VERTICAL, false);
				Global.settings.flush();
			}
		});
		Image iphoneTilted = new Image(Global.uiSkin, "IPhoneTilted");
		iphoneTilted.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);
				tiltedChoice.setChecked(true);
				Global.settings.putBoolean(Global.IPHONE_TILTED, true);
				Global.settings.putBoolean(Global.IPHONE_HORIZONTAL, false);
				Global.settings.putBoolean(Global.IPHONE_VERTICAL, false);
				Global.settings.flush();
			}
		});
		Image iphoneVertical = new Image(Global.uiSkin, "IPhoneVertical");
		iphoneVertical.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);
				verticalChoice.setChecked(true);
				Global.settings.putBoolean(Global.IPHONE_VERTICAL, true);
				Global.settings.putBoolean(Global.IPHONE_TILTED, false);
				Global.settings.putBoolean(Global.IPHONE_HORIZONTAL, false);
				Global.settings.flush();
			}
		});

		table.row();
		table.add(label).colspan(3);

		table.row();
		table.add(flatLabel);
		table.add(tiltedLabel);
		table.add(verticalLabel);

		table.row().spaceBottom(5f);
		table.add(iphoneFlat);
		table.add(iphoneTilted);
		table.add(iphoneVertical);

		table.row().spaceTop(5f);
		table.add(flatChoice);
		table.add(tiltedChoice);
		table.add(verticalChoice);
	}

	private void createAimOptions(Table table)
	{
		Label label = new Label("Method of Aiming", Global.uiSkin, "hobbyOfNight-30");

		Label pressToAimLabel = new Label("Press To Aim", Global.uiSkin, "hobbyOfNight-20");
		Label analogStickLabel = new Label("Analog Stick", Global.uiSkin, "hobbyOfNight-20");

		final Button pressToAimChoice = new Button(Global.uiSkin, "radio");
		pressToAimChoice.setChecked(Global.settings.getBoolean(Global.PRESS_TO_AIM));
		final Button analogStickChoice = new Button(Global.uiSkin, "radio");
		analogStickChoice.setChecked(Global.settings.getBoolean(Global.ANALOG_STICK_AIM));
		new ButtonGroup(pressToAimChoice, analogStickChoice);

		Image pressToAim = new Image(Global.uiSkin, "PressToAim");
		pressToAim.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);
				pressToAimChoice.setChecked(true);
				Global.settings.putBoolean(Global.PRESS_TO_AIM, true);
				Global.settings.putBoolean(Global.ANALOG_STICK_AIM, false);
				Global.settings.flush();
			}
		});
		Image analogStick = new Image(Global.uiSkin, "AnalogStickToAim");
		analogStick.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);
				analogStickChoice.setChecked(true);
				Global.settings.putBoolean(Global.ANALOG_STICK_AIM, true);
				Global.settings.putBoolean(Global.PRESS_TO_AIM, false);
				Global.settings.flush();
			}
		});

		table.row();
		table.add(label).colspan(3);

		table.row();
		table.add(pressToAimLabel);
		table.add();
		table.add(analogStickLabel);

		table.row().spaceBottom(4f);
		table.add(pressToAim);
		table.add();
		table.add(analogStick);

		table.row().spaceTop(4f);
		table.add(pressToAimChoice);
		table.add();
		table.add(analogStickChoice);
	}

	private void createSoundButtons(Table table)
	{
		Table soundTable = new Table(Global.uiSkin);
		soundTable.defaults().space(20f);
		Table musicTable = new Table(Global.uiSkin);
		musicTable.defaults().space(20f);

		final Button sound = new Button(Global.uiSkin, "soundButton");
		sound.setChecked(Global.settings.getBoolean(Global.SOUND));
		final Button music = new Button(Global.uiSkin, "musicButton");
		music.setChecked(Global.settings.getBoolean(Global.MUSIC));
		Label soundLabel = new Label("Sound", Global.uiSkin, "hobbyOfNight-20");
		Label musicLabel = new Label("Music", Global.uiSkin, "hobbyOfNight-20");

		sound.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);
				Global.settings.putBoolean(Global.SOUND, sound.isChecked());
				Global.settings.flush();
			}
		});

		music.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);
				if(music.isChecked())
					Global.gameMusic1.play();
				else
					Global.gameMusic1.pause();
				Global.settings.putBoolean(Global.MUSIC, music.isChecked());
				Global.settings.flush();
			}
		});

		soundTable.add(sound);
		soundTable.add(soundLabel);
		musicTable.add(musicLabel);
		musicTable.add(music);

		table.row();
		table.add(soundTable);
		table.add();
		table.add(musicTable);
	}

	private void createBackButton(Table table)
	{
		Button backButton = new Button(Global.uiSkin, "backButton");
		backButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);
				close();
			}
		});
		Label backLabel = new Label("Back", Global.uiSkin, "hobbyOfNight-20");

		table.row();
		table.add(backButton).padLeft(30f).padTop(30f);
		table.add(backLabel).space(5f).center();
	}
}
