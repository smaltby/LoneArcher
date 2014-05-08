package me.seanmaltby.lonearcher.core.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.utils.Array;
import me.seanmaltby.lonearcher.core.entities.Player;

import java.util.HashSet;
import java.util.Set;

public class GetNearbyQueryCallBack implements QueryCallback
{
	public Set<Fixture> fixtures = new HashSet<>();
	public Set<Body> bodies = new HashSet<>();
	private Array<Fixture> ignore;

	public GetNearbyQueryCallBack(Array<Fixture> ignore)
	{
		this.ignore = ignore;
	}

	@Override
	public boolean reportFixture(Fixture fixture)
	{
		if(fixture.isSensor() || fixture.getUserData() instanceof Player)
			return true;
		if(!ignore.contains(fixture, false))
		{
			fixtures.add(fixture);
			bodies.add(fixture.getBody());
		}
		return true;
	}
}
