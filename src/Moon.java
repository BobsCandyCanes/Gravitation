import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Moon extends Planet
{	
	private double locationInOrbit;
	
	private double speed;
	
	private double radiusFromPlanet;
	
	private Planet parentPlanet;
	
	private int defaultSize = 20;

	public Moon(double r, Planet p)
	{	
		parentPlanet = p;
		
		radiusFromPlanet = r;
		
		centerXPosition = parentPlanet.centerXPosition + radiusFromPlanet;
		centerYPosition = parentPlanet.centerYPosition;

		width = defaultSize;
		height = defaultSize;
		
		mass = defaultSize; 
		
		xPosition = centerXPosition - width / 2;
		yPosition = centerYPosition - height / 2;
		
		radiusOfGravity = (int)(width * 10);
		
		locationInOrbit = Math.random() * (2 * Math.PI);
		
		importSprite();
	}
	
	public void act()
	{
		speed = (1 / radiusFromPlanet) / 2;
		
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
	
	public void importSprite()
	{
		try 
		{
			sprite = ImageIO.read(new File("Images/moon.png"));  //import the sprite
		}
		catch (IOException e) 
		{
			System.out.println("Error loading sprite: moon");
			e.printStackTrace();
		}
	}
}
