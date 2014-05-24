package me.seanmaltby.lonearcher.core.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import me.seanmaltby.lonearcher.core.Global;
import me.seanmaltby.lonearcher.core.entities.EntityAttribute;
import me.seanmaltby.lonearcher.core.entities.Player;
import me.seanmaltby.lonearcher.core.utils.CroppedTextureRegion;

public class GameHUD
{
	private Stage stage;
	private Table table;

	public GameHUD(Stage stage)
	{
		this.stage = stage;
		open();
	}

	private void open()
	{
		table = new Table(Global.uiSkin);
		table.setFillParent(true);
		table.pad(10f);
		table.left().top();

		//Health Bar
		Group group = new Group();

		Image healthBarBackground = new Image(Global.uiSkin, "HealthBarBackground");
		CroppedTextureRegion healthBarDrawable = new CroppedTextureRegion(Global.atlas.findRegion("HealthBar"));
		Image healthBar = new Image(healthBarDrawable);
		healthBar.addAction(new CropHealthBar(healthBarDrawable));
		Image healthBarForeground = new Image(Global.uiSkin, "HealthBarForeground");

		group.addActor(healthBarBackground);
		group.addActor(healthBar);
		group.addActor(healthBarForeground);

		healthBarBackground.setPosition(18.4f, 13.8f);
		healthBar.setPosition(18.4f, 13.8f);

		group.setSize(healthBarForeground.getWidth(), healthBarForeground.getHeight());
		table.add(group);

		//Empty space between health bar and money
		table.add().expandX();

		//Money
		Label moneyLabel = new Label("Money - $0", Global.uiSkin, "hobbyOfNight-30");
		moneyLabel.addAction(new UpdateMoney());
		table.add(moneyLabel).top();

		table.row().expandY().bottom();

		Button pauseButton = new Button(Global.uiSkin, "pauseButton");
		pauseButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				super.clicked(event, x, y);
				Global.gameScreen.pauseInternal();
				new OptionsGUI(stage).open();
			}
		});
		table.add(pauseButton).left();

		//Empty space between pause button and aim control
		table.add().expandX();

		AimControl aimControl = new AimControl();
		table.add(aimControl).right().pad(aimControl.getWidth() / 2);
		aimControl.setVisible(Global.settings.getBoolean(Global.ANALOG_STICK_AIM));

		stage.addActor(table);
	}

	public void close()
	{
		table.remove();
	}

	class CropHealthBar extends Action
	{
		private CroppedTextureRegion healthBar;

		CropHealthBar(CroppedTextureRegion healthBar)
		{
			this.healthBar = healthBar;
		}

		@Override
		public boolean act(float delta)
		{
			Player player = Global.gameScreen.getPlayer();
			healthBar.setEndX(player.getHealth() / player.getAttributeFloat(EntityAttribute.MAX_HEALTH));
			return false;
		}
	}

	class UpdateMoney extends Action
	{
		@Override
		public boolean act(float delta)
		{
			Label label = (Label) getActor();
			label.setText("Money - $"+Global.gameScreen.getPlayer().getMoney());
			return false;
		}
	}

	class AimControl extends Group
	{
		private Image analogStick;

		AimControl()
		{
			addActor(new Image(Global.uiSkin, "AnalogStickBackground"));
			addActor(analogStick = new Image(Global.uiSkin, "AnalogStick"));
			analogStick.addListener(new ClickListener()
			{
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
				{
					super.touchDown(event, x, y, pointer, button);
					analogStick.addAction(new UpdateAim(new Vector2(Gdx.input.getX(), Gdx.input.getY()), analogStick.getImageWidth() / 2));

					return true;
				}
			});

			analogStick.addAction(new Action()
			{
				@Override
				public boolean act(float delta)
				{
					setVisible(Global.settings.getBoolean(Global.ANALOG_STICK_AIM));
					return false;
				}
			});

			setSize(analogStick.getWidth(), analogStick.getHeight());
		}
	}

	class UpdateAim extends Action
	{
		private Vector2 initialTouch;
		private float maxRadius;

		UpdateAim(Vector2 initialTouch, float maxRadius)
		{
			this.initialTouch = initialTouch;
			this.maxRadius = maxRadius;
		}

		@Override
		public boolean act(float delta)
		{
			Image analogStick = (Image) getActor();
			Player player = Global.gameScreen.getPlayer();

			if(Gdx.input.isTouched() && player.isAlive())
			{
				//Reverse the y coordinates because the y axis is flipped for inputs
				Vector2 deltaPosition = new Vector2(Gdx.input.getX() - initialTouch.x, initialTouch.y - Gdx.input.getY());
				float distance = Math.min(deltaPosition.len(), maxRadius);
				deltaPosition.nor().scl(distance);

				analogStick.setPosition(deltaPosition.x, deltaPosition.y);
				player.setDirection(deltaPosition.getAngleRad());

				return false;
			} else
			{
				analogStick.setPosition(0, 0);
				return true;
			}
		}
	}
}
