package me.seanmaltby.lonearcher.core.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class CroppedTextureRegion implements Drawable
{
	private Texture texture;

	private int srcX;
	private int srcY;
	private int srcWidth;
	private int srcHeight;

	private float cropRatioXStart = 0f;
	private float cropRatioXEnd = 1f;
	private float cropRatioYStart = 0f;
	private float cropRatioYEnd = 1f;

	public CroppedTextureRegion(TextureRegion region)
	{
		texture = region.getTexture();

		srcX = region.getRegionX();
		srcY = region.getRegionY();
		srcWidth = region.getRegionWidth();
		srcHeight = region.getRegionHeight();
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height)
	{
		int cropX = (int) (srcX + srcWidth * cropRatioXStart);
		int cropY = (int) (srcY + srcHeight * cropRatioYStart);

		float cropWidthRatio = cropRatioXEnd - cropRatioXStart;
		float cropHeightRatio = cropRatioYEnd - cropRatioYStart;
		int cropWidth = (int) (srcWidth * cropWidthRatio);
		int cropHeight = (int) (srcHeight * cropHeightRatio);

		batch.draw(texture, x, y, width * cropWidthRatio, height * cropHeightRatio, cropX, cropY, cropWidth, cropHeight, false, false);
	}

	public void setStartX(float ratio)
	{
		cropRatioXStart = ratio;
	}

	public void setEndX(float ratio)
	{
		cropRatioXEnd = ratio;
	}

	public void setStartY(float ratio)
	{
		cropRatioYStart = ratio;
	}

	public void setEndY(float ratio)
	{
		cropRatioYEnd = ratio;
	}

	@Override
	public float getLeftWidth()
	{
		return 0;
	}

	@Override
	public void setLeftWidth(float leftWidth)
	{

	}

	@Override
	public float getRightWidth()
	{
		return 0;
	}

	@Override
	public void setRightWidth(float rightWidth)
	{

	}

	@Override
	public float getTopHeight()
	{
		return 0;
	}

	@Override
	public void setTopHeight(float topHeight)
	{

	}

	@Override
	public float getBottomHeight()
	{
		return 0;
	}

	@Override
	public void setBottomHeight(float bottomHeight)
	{

	}

	@Override
	public float getMinWidth()
	{
		return srcWidth * (cropRatioXEnd - cropRatioXStart);
	}

	@Override
	public void setMinWidth(float minWidth)
	{

	}

	@Override
	public float getMinHeight()
	{
		return srcHeight * (cropRatioYEnd - cropRatioYStart);
	}

	@Override
	public void setMinHeight(float minHeight)
	{

	}
}
