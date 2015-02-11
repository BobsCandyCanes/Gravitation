import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Moon extends Planet
{	
	private static final int DEFAULT_SIZE = 28;
	
	private double locationInOrbit;
	
	private double speed;
	
	private double radiusFromPlanet;
	
	private Planet parentPlanet;

	public Moon(double r, Planet p)
	{	
		parentPlanet = p;
		
		radiusFromPlanet = r;
		
		centerXPosition = parentPlanet.centerXPosition + radiusFromPlanet;
		centerYPosition = parentPlanet.centerYPosition;

		width = DEFAULT_SIZE;
		height = DEFAULT_SIZE;
		
		mass = width; 
		
		xPosition = centerXPosition - width / 2;
		yPosition = centerYPosition - height / 2;
		
		radiusOfGravity = (int)(width * 10);
		
		locationInOrbit = Math.random() * (2 * Math.PI);
		
		importSprite("moon.png");
	}
	
	public void act()
	{
		speed = (1 / radiusFromPlanet) / 2.5;
		
		locationInOrbit += speed;
		
		calculateLocation();
		attractProjectiles();
		
		if(hasFinishedOrbit())
		{
			locationInOrbit = 0;
		}
	}
	
	public boolean hasFinishedOrbit()
	{
		return locationInOrbit >= 2 * (Math.PI);
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.GRAY);
		
		g.drawImage(sprite, (int)xPosition, (int)yPosition, null);
		
		g.setColor(Color.BLACK);
		
		g.drawOval((int)xPosition, (int)yPosition, (int)width, (int)height);
	}
	
	public void calculateLocation()
	{	
		centerXPosition = parentPlanet.getCenterXPosition() + radiusFromPlanet * Math.sin(locationInOrbit);
		centerYPosition = parentPlanet.getCenterYPosition() + radiusFromPlanet * Math.cos(locationInOrbit);
		
		xPosition = centerXPosition - width / 2;
		yPosition = centerYPosition - height / 2;
		
		bounds = new Rectangle ((int)xPosition, (int)yPosition, (int)width, (int)height);
	}	
}
