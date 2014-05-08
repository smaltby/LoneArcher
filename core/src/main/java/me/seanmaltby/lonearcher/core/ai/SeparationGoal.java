package me.seanmaltby.lonearcher.core.ai;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import me.seanmaltby.lonearcher.core.entities.LivingEntity;
import me.seanmaltby.lonearcher.core.utils.GetNearbyQueryCallBack;

public class SeparationGoal extends Goal
{
	private LivingEntity entity;

	public SeparationGoal(LivingEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public void execute()
	{
		Body body = entity.getBody();

		Vector2 position = body.getPosition();
		float radius = body.getFixtureList().get(0).getShape().getRadius() * 3;

		Vector2 separationVector = new Vector2();

		GetNearbyQueryCallBack queryCallBack = new GetNearbyQueryCallBack(body.getFixtureList());
		entity.getWorld().QueryAABB(queryCallBack, position.x - radius, position.y - radius, position.x + radius, position.y + radius);
		for(Body nearby : queryCallBack.bodies)
		{
			Vector2 nearbyPosition = nearby.getPosition();
			Vector2 positionDiff = new Vector2(position).sub(nearbyPosition);
			Vector2 inversePositionDiff = new Vector2(radius, radius).scl(1/positionDiff.x, 1/positionDiff.y);
			separationVector.add(inversePositionDiff);
		}
		if(separationVector.equals(Vector2.Zero))
			return;

		float desiredAngle = separationVector.angle();
		float currentAngle = body.getLinearVelocity().angle();

		//Prevents transitional issues during the interpolation
		if(Math.abs(desiredAngle - currentAngle) > 180)
		{
			if(desiredAngle < currentAngle)
				desiredAngle += 360;
			else
				currentAngle += 360;
		}
		float approachAngle = Interpolation.linear.apply(currentAngle, desiredAngle, 0.1f) - currentAngle;
		if(approachAngle > 180)
			approachAngle -= 360;
		else if(approachAngle < -180)
			approachAngle += 360;

		if(approachAngle > 2)
			approachAngle = 2;
		else if(approachAngle < -2)
			approachAngle = -2;

		body.setLinearVelocity(body.getLinearVelocity().setAngle(approachAngle + currentAngle));
	}
}
