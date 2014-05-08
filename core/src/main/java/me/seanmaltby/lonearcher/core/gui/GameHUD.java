package me.seanmaltby.lonearcher.core.gui;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
		Label moneyLabel = new Label("Money - $0", Global.uiSkin, "hobbyOfNight");
		moneyLabel.addAction(new UpdateMoney());
		table.add(moneyLabel).top();

		stage.addActor(table);
	}

	public void close()
	{
		table.remove();
	}

	class CropHealthBar extends Action
	{
		private CroppedTextureRegion healthBar;

		public CropHealthBar(CroppedTextureRegion healthBar)
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
}
