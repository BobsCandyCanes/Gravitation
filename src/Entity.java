 import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


public abstract class Entity
{
	protected double xPosition;
	protected double yPosition;
	
	protected double width = 1;
	protected double height = 1;
	
	protected double centerXPosition;
	protected double centerYPosition;
	
	protected double mass;
	
	protected double xVelocity;
	protected double yVelocity;
	
	protected double angleInDegrees;
	
	protected Rectangle bounds;

	protected BufferedImage sprite;
	
	public Entity()
	{	
	}

	public void act()
	{	
	}
	
	public void draw(Graphics g)
	{	
	}
	
	public Ship getParent()
	{
		return new Ship();
	}
	
	public double getMass()
	{
		return mass;
	}
	
	public double calcAngleTo(Entity target)
	{
		double xDifference = getXDistanceFrom(target);
		double yDifference = getYDistanceFrom(target);

		return Math.atan2(-yDifference, xDifference);
	}
	
	public double getDistanceFrom(Entity e)
	{
		double xDiff = getXDistanceFrom(e);
		double yDiff = getYDistanceFrom(e);
		
		return Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
	}
	
	public double getXDistanceFrom(Entity e)
	{
		return e.centerXPosition - this.centerXPosition;
	}
	
	public double getYDistanceFrom(Entity e)
	{
		return e.centerYPosition - this.centerYPosition;
	}
	
	public double getXPosition()
	{
		return xPosition;
	}
	
	public double getYPosition()
	{
		return yPosition;
	}
	
	public double getCenterXPosition()
	{
		centerXPosition = xPosition + width / 2;
		return centerXPosition;
	}
	
	public double getCenterYPosition()
	{
		centerYPosition = yPosition + height / 2;
		return centerYPosition;
	}
	
	public double getWidth()
	{
		return width;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public double getXVelocity()
	{
		return xVelocity;
	}
	
	public double getYVelocity()
	{
		return yVelocity;
	}
	
	public void setXVelocity(double vX)
	{
		xVelocity = vX;
	}
	
	public void setYVelocity(double vY)
	{
		yVelocity = vY;
	}
	
	public double getAngleInDegrees()
	{
		return angleInDegrees;
	}
	
	public Rectangle getBounds()
	{
		bounds = new Rectangle ((int)xPosition, (int)yPosition, (int)width, (int)height);
		
		return bounds;
	}
	
	public void importSprite(String spritePath)
	{
		sprite = SpriteLibrary.getSprite(spritePath);
		scaleSprite();
	}
	
	public void scaleSprite()
	{
		int spriteWidth  = sprite.getWidth();
		int spriteHeight = sprite.getHeight();

		double scaleX = (double)width / spriteWidth;
		double scaleY = (double)height / spriteHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

		sprite = bilinearScaleOp.filter(sprite, new BufferedImage((int)width, (int)height, sprite.getType()));
	}
}
