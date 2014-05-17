package me.seanmaltby.lonearcher.core.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import me.seanmaltby.lonearcher.core.Global;
import me.seanmaltby.lonearcher.core.ParticleEffectManager;
import me.seanmaltby.lonearcher.core.screens.GameScreen;

public class Player extends RangedEntity
{
	private boolean revive = false;
	private boolean regen = false;
	private boolean ember = false;

	private float money = 100f;

	public Player(Vector2 position, float direction, World b2World)
	{
		super("Archer", position, direction, b2World);
		setLayer(Player.PLAYER_LAYER);

		getSprite().setAnimation("Attack");
		setAttribute(EntityAttribute.MAX_HEALTH, 20f);
		setAttribute(EntityAttribute.DAMAGE, 5f);
		setAttribute(EntityAttribute.ATTACK_SPEED, 2f);
		setAttribute(EntityAttribute.SPEED, 3f);
	}

	@Override
	public void update(float delta)
	{
		handleInput();
		if(!isAttacking())
			attack();
		//Regenerate .5 health per second if the player has regen
		if(regen)
			setHealth(getHealth() + 0.5f * delta);

		super.update(delta);
		if(getPosition().x < 0 || getPosition().x > GameScreen.GAME_WIDTH || getPosition().y < 0 || getPosition().y > GameScreen.GAME_HEIGHT)
		{
			setPosition(new Vector2(
					MathUtils.clamp(getPosition().x, 0, GameScreen.GAME_WIDTH),
					MathUtils.clamp(getPosition().y, 0, GameScreen.GAME_HEIGHT))
			);
		}
	}

	@Override
	public void kill()
	{
		if(revive)
		{
			setHealth(getAttributeFloat(EntityAttribute.MAX_HEALTH));
			revive = false;
			ParticleEffect revive = new ParticleEffect();
			revive.load(Gdx.files.internal("effects/revive.par"), Gdx.files.internal("effects"));
			revive.setPosition(getPosition().x, getPosition().y);
			revive.start();
			ParticleEffectManager.addEffect(revive, this);
		} else
		{
			super.kill();
		}
	}

	@Override
	protected Projectile createProjectile()
	{
		Projectile projectile = new Projectile("Arrow", new Vector2(getPosition()), getDirection(), getWorld(), this);
		if(ember && MathUtils.random() < 0.35f)
			projectile.setAttribute(EntityAttribute.ELEMENT, Element.FIRE);
		return projectile;
	}

	@Override
	protected int getProjectileDistanceFromSelf()
	{
		return 70;
	}

	@Override
	public boolean isAI()
	{
		return false;
	}

	private void handleInput()
	{
		OrthographicCamera camera = Global.gameScreen.getCamera();
		//Variables for determing look direction
		float deltaY;
		float deltaX;
		float angle;

		switch(Gdx.app.getType())
		{
			case Desktop:
			case Applet:
			case WebGL:
				//Look direction
				Vector3 mouseCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(mouseCoords);

				deltaY = mouseCoords.y - getPosition().y;
				deltaX = mouseCoords.x - getPosition().x;
				angle = MathUtils.atan2(deltaY, deltaX);
				setDirection(angle);

				//Movement
				int vertical = 0;
				int horizontal = 0;
				if(Gdx.input.isKeyPressed(Input.Keys.W))
					vertical++;
				if(Gdx.input.isKeyPressed(Input.Keys.S))
					vertical--;
				if(Gdx.input.isKeyPressed(Input.Keys.D))
					horizontal++;
				if(Gdx.input.isKeyPressed(Input.Keys.A))
					horizontal--;

				move(new Vector2(horizontal, vertical));
				break;
			case iOS:
			case Android:
				//NOTE: Look direction is handled by the aim controller in the GameHUD for mobile targets

				//Movement
				float defaultX = 0;
				float defaultY = 4.9f;
				float maxDelta = 1.5f;

				//Opposite accelerometer readings are used because the game is run in landscape mode
				float accelX = Gdx.input.getAccelerometerY() - defaultX;
				float accelY = Gdx.input.getAccelerometerX() - defaultY;
				//Negate accelY to account for landscape right
				Vector2 accel = new Vector2(accelX, -accelY);
				accel.clamp(0, maxDelta);

				float speedRatio = accel.len2() / (maxDelta * maxDelta);

				move(accel, speedRatio * getAttributeFloat(EntityAttribute.SPEED));
				break;
		}
	}

	public void addMoney(float moneyIncrement)
	{
		money += moneyIncrement;
	}

	public void subMoney(float moneyDecrement)
	{
		money -= moneyDecrement;
	}

	public float getMoney()
	{
		return money;
	}

	public boolean isRevive()
	{
		return revive;
	}

	public void setRevive(boolean revive)
	{
		this.revive = revive;
	}

	public boolean isRegen()
	{
		return regen;
	}

	public void setRegen(boolean regen)
	{
		this.regen = regen;
	}

	public boolean isEmber()
	{
		return ember;
	}

	public void setEmber(boolean ember)
	{
		this.ember = ember;
	}
}
