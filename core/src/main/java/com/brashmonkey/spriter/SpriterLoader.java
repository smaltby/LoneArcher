/**************************************************************************
 * Copyright 2013 by Trixt0r
 * (https://github.com/Trixt0r, Heinrich Reich, e-mail: trixter16@web.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package com.brashmonkey.spriter;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;
import me.seanmaltby.lonearcher.core.Global;

/**
 * A loader class to load Spriter sprites inside LibGDX.
 *
 * @author Trixt0r
 */

public class SpriterLoader extends Loader<Sprite> implements Disposable
{
	public SpriterLoader(Data data)
	{
		super(data);
	}

	@Override
	protected Sprite loadResource(FileReference ref)
	{
		String path = data.getFile(ref).name;

		//Get sprite using the path with the extension removed
		return Global.atlas.createSprite(path.replaceAll("\\.\\S+$", ""));
	}
}