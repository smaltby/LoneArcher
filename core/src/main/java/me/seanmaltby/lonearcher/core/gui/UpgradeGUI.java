package me.seanmaltby.lonearcher.core.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import me.seanmaltby.lonearcher.core.Global;
import me.seanmaltby.lonearcher.core.WaveHandler;
import me.seanmaltby.lonearcher.core.entities.EntityAttribute;
import me.seanmaltby.lonearcher.core.entities.Player;

import java.util.ArrayList;
import java.util.List;

public class UpgradeGUI
{
	private Stage stage;
	private WaveHandler waveHandler;

	private Window upgradeWindow;
	private List<UpgradePath> upgradePathsList;
	private List<UpgradeButton> upgradeButtonsList;
	private List<PlayerStat> playerStatsList;

	private boolean initialized = false;

	public UpgradeGUI(Stage stage, WaveHandler waveHandler)
	{
		this.stage = stage;
		this.waveHandler = waveHandler;

		upgradeWindow = new Window("Upgrade", Global.uiSkin);
		upgradePathsList = new ArrayList<>();
		upgradeButtonsList = new ArrayList<>();
		playerStatsList = new ArrayList<>();
	}

	private void initializeGUI()
	{
		initialized = true;

		upgradeWindow.setTitleAlignment(Align.center);
		upgradeWindow.setWidth(stage.getWidth() * .9f);
		upgradeWindow.setHeight(stage.getHeight() * .8f);
		upgradeWindow.setX(stage.getWidth() / 2f - upgradeWindow.getWidth() / 2f);
		upgradeWindow.setY(stage.getHeight() / 2f - upgradeWindow.getHeight() / 2f);
		upgradeWindow.pad(30f);
		upgradeWindow.left();

		Table upgradePaths = new Table(Global.uiSkin);
		Table upgradeButtons = new Table(Global.uiSkin);
		upgradeButtons.defaults().pad(5f);
		Table statsTable = new Table(Global.uiSkin);
		statsTable.defaults().left().top();
		upgradeWindow.add(upgradePaths).fillY().expandY().top().left();
		upgradeWindow.add(upgradeButtons).fillY().expandY().top().left();
		upgradeWindow.add(statsTable).fillY().expandY().top().left();
		upgradeWindow.row();

		createUpgradeButtons(upgradeButtons);

		upgradePathsList.add(new UpgradePath("Health", 45, 2, EntityAttribute.MAX_HEALTH, 5, 1, 5, false));
		upgradePathsList.add(new UpgradePath("Damage", 75, 2, EntityAttribute.DAMAGE, 2, 1, 5, false));
		upgradePathsList.add(new UpgradePath("Attack Speed", 60, 2, EntityAttribute.ATTACK_SPEED, 0.5f, 1, 5, false));
		upgradePathsList.add(new UpgradePath("Move Speed", 45, 2, EntityAttribute.SPEED, 0.25f, 1, 5, false));
		upgradePathsList.add(new UpgradePath("Piercing", 240, 1.5f, EntityAttribute.PIERCING, 1, 1, 3, true));

		for(UpgradePath path : upgradePathsList)
		{
			upgradePaths.add(path).left();
			upgradePaths.row();
		}

		playerStatsList.add(new PlayerStat(null, EntityAttribute.MAX_HEALTH));
		playerStatsList.add(new PlayerStat(null, EntityAttribute.DAMAGE));
		playerStatsList.add(new PlayerStat(null, EntityAttribute.ATTACK_SPEED));
		playerStatsList.add(new PlayerStat(null, EntityAttribute.SPEED));
		playerStatsList.add(new PlayerStat(null, EntityAttribute.PIERCING));
		for(PlayerStat stat : playerStatsList)
		{
			statsTable.add(stat);
			statsTable.row();
		}

		Button nextWaveButton = new Button(Global.uiSkin, "nextWaveButton");
		nextWaveButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				waveHandler.startWave();
				close();
			}
		});
		upgradeWindow.add(nextWaveButton).expandX().center().colspan(3);
	}

	private void createUpgradeButtons(Table upgradeButtons)
	{
		final Player player = Global.gameScreen.getPlayer();
		upgradeButtonsList.add(new UpgradeButton("Triple Shot", Global.uiSkin, "tripleShot", 1200, new Runnable()
		{
			@Override
			public void run()
			{
				player.setAttribute(EntityAttribute.PROJECTILES, 3);
			}
		}));
		upgradeButtonsList.add(new UpgradeButton("Explosive Arrows", Global.uiSkin, "explosion", 1500, new Runnable()
		{
			@Override
			public void run()
			{
				player.setExplosiveArrows(true);
				player.setAttribute(EntityAttribute.DAMAGE, player.getAttributeFloat(EntityAttribute.DAMAGE) + 5);
			}
		}));
		upgradeButtonsList.add(new UpgradeButton("Revive on Death", Global.uiSkin, "revive", 450, new Runnable()
		{
			@Override
			public void run()
			{
				player.setRevive(true);
			}
		}));
		upgradeButtonsList.add(new UpgradeButton("Knockback", Global.uiSkin, "knockback", 600, new Runnable()
		{
			@Override
			public void run()
			{
				player.setAttribute(EntityAttribute.KNOCKBACK, 5f);
			}
		}));
		upgradeButtonsList.add(new UpgradeButton("Health Regen", Global.uiSkin, "regen", 900, new Runnable()
		{
			@Override
			public void run()
			{
				player.setRegen(true);
			}
		}));
		upgradeButtonsList.add(new UpgradeButton("Fire Arrows", Global.uiSkin, "ember", 450, new Runnable()
		{
			@Override
			public void run()
			{
				player.setEmber(true);
			}
		}));

		for (int i = 0; i < upgradeButtonsList.size(); i++)
		{
			upgradeButtons.add(upgradeButtonsList.get(i));
			if(i % 2 == 1)
				upgradeButtons.row();
		}
	}

	public void open()
	{
		if(!initialized)
			initializeGUI();
		stage.addActor(upgradeWindow);
		updateDisabledStatuses();
	}

	public void close()
	{
		upgradeWindow.remove();
	}

	private void updateStats()
	{
		for(PlayerStat stat : playerStatsList)
		{
			stat.updateValue();
		}
	}

	private void updateDisabledStatuses()
	{
		for(UpgradeButton button : upgradeButtonsList)
			button.updateDisabledStatus();
		for(UpgradePath path : upgradePathsList)
			path.updateDisabledStatus();
	}

	class UpgradePath extends Table
	{
		private String name;

		private Button[] radios;
		private Label nameLabel;
		private Label costLabel;
		private Button plus;

		private int upgradeLevel = 0;
		private int numUpgrades;

		private float cost;
		private EntityAttribute attribute;
		private float attributeIncrease;

		private float costMultiplier;
		private float increaseMultiplier;

		private boolean integer;

		UpgradePath(String name, float initialCost, float costMultiplier, EntityAttribute attribute, float initialIncrease,
					float increaseMultiplier, int numUpgrades, boolean integer)
		{
			this.name = name;

			cost = initialCost;
			this.attribute = attribute;
			attributeIncrease = initialIncrease;

			this.costMultiplier = costMultiplier;
			this.increaseMultiplier = increaseMultiplier;

			this.numUpgrades = numUpgrades;

			this.integer = integer;

			addActors();
		}

		private void addActors()
		{
			String nameString = "+"+attributeIncrease+" "+name;
			nameLabel = new Label(nameString, Global.uiSkin, "hobbyOfNight-20");
			add(nameLabel).width(250f).colspan(numUpgrades + 1); //+1 to take into account the empty cell after the radio buttons

			String costString = "$"+cost;
			costLabel = new Label(costString, Global.uiSkin, "hobbyOfNight-20");
			add(costLabel);

			row();

			radios = new Button[numUpgrades];
			for(int i = 0; i < numUpgrades; i++)
			{
				radios[i] = new Button(Global.uiSkin, "radio");
				radios[i].setTouchable(Touchable.disabled);
				add(radios[i]).pad(2f);
			}
			add().expandX();

			plus = new Button(Global.uiSkin, "upgradePlus");
			plus.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					if(upgradeLevel >= numUpgrades)
						return;
					if(plus.isDisabled())
						return;

					//Deduct cost if possible
					Player player = Global.gameScreen.getPlayer();
					if(player.getMoney() < cost)
						return;

					player.subMoney(cost);

					//Upgrade level and handle actor appearances
					radios[upgradeLevel].setChecked(true);
					upgradeLevel++;
					if(upgradeLevel == numUpgrades)
					{
						plus.setDisabled(true);
						costLabel.setText("$N/A");
					} else
					{
						//Handle attribute increases
						if (integer)
							player.setAttribute(attribute, player.getAttributeInteger(attribute) + MathUtils.round(attributeIncrease));
						else
							player.setAttribute(attribute, player.getAttributeFloat(attribute) + attributeIncrease);
						attributeIncrease *= increaseMultiplier;
						cost *= costMultiplier;

						nameLabel.setText("+" + attributeIncrease + " " + name);
						costLabel.setText("$" + cost);
						costLabel.setWidth(150f);
					}

					//Play sound
					if(Global.settings.getBoolean(Global.SOUND))
						Global.buttonClick.play();

					//Update stats and whether or not the other upgrades are now disabled
					updateStats();
					updateDisabledStatuses();
				}
			});

			add(plus).pad(2f).left();
		}

		void updateDisabledStatus()
		{
			if(upgradeLevel == numUpgrades)
				return;
			Player player = Global.gameScreen.getPlayer();
			if(player.getMoney() < cost)
				costLabel.setColor(Color.RED);
			else
				costLabel.setColor(Color.WHITE);
		}
	}

	class UpgradeButton extends Table
	{
		private float cost;
		private boolean used = false;

		private Button button;
		private Label costLabel;
		private Label nameLabel;

		UpgradeButton(String name, Skin uiSkin, String style, final float cost, final Runnable onBuy)
		{
			button = new Button(uiSkin, style);
			this.cost = cost;

			button.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					if(button.isDisabled())
						return;

					Player player = Global.gameScreen.getPlayer();
					if(player.getMoney() < cost)
						return;

					onBuy.run();

					player.subMoney(cost);
					used = true;
					button.setDisabled(true);
					costLabel.setText("$N/A");

					//Play sound
					if(Global.settings.getBoolean(Global.SOUND))
						Global.buttonClick.play();

					//Update stats and whether or not the other upgrades are now disabled
					updateStats();
					updateDisabledStatuses();
				}
			});
			costLabel = new Label("$"+cost, Global.uiSkin, "hobbyOfNight-17");
			costLabel.setTouchable(Touchable.childrenOnly);

			Group buttonGroup = new Group();
			buttonGroup.addActor(button);
			buttonGroup.addActor(costLabel);
			buttonGroup.setSize(button.getWidth(), button.getHeight());

			add(buttonGroup).center();
			row();
			nameLabel = new Label(name, Global.uiSkin, "hobbyOfNight-20");
			add(nameLabel);
		}

		void updateDisabledStatus()
		{
			if(used)
				return;
			Player player = Global.gameScreen.getPlayer();
			if(player.getMoney() < cost)
				costLabel.setColor(Color.RED);
			else
				costLabel.setColor(Color.WHITE);
		}
	}

	class PlayerStat extends Table
	{
		private Image icon;
		private Label attributeLabel;
		private Label valueLabel;

		private EntityAttribute attribute;

		PlayerStat(Image icon, EntityAttribute attribute)
		{
			this.icon = icon;
			this.attribute = attribute;

			attributeLabel = new Label(attribute.name(), Global.uiSkin, "hobbyOfNight-20");
			valueLabel = new Label("", Global.uiSkin, "hobbyOfNight-20");
			updateValue();

			addActors();
		}

		void updateValue()
		{
			Player player = Global.gameScreen.getPlayer();
			valueLabel.setText(player.getAttribute(attribute, Number.class).toString());
		}

		private void addActors()
		{
			add().size(40, 40);
			add(attributeLabel).width(150f);
			add(valueLabel);
		}
	}
}
