import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Planet extends Entity
{
	protected int radiusOfGravity;

	private ArrayList<Moon> moons = new ArrayList<Moon>();

	private String spritePath;

	private double rotationSpeed;

	public Planet()
	{
	}

	public Planet(int x, int y)
	{
		this(x, y, 80);
	}

	public Planet(int x, int y, int size)
	{
		xPosition = x - width / 2;
		yPosition = y - height / 2;

		width = size;
		height = size;

		mass = size;

		centerXPosition = xPosition + width / 2;
		centerYPosition = yPosition + height / 2;

		radiusOfGravity = (int)(width * 10);

		chooseRandomSprite();
		chooseRandomRotationSpeed();

		importSprite();
	}

	public void chooseRandomSprite()
	{
		int rand = (int)(Math.random() * 2) + 1;

		if(rand == 1)
		{
			spritePath = "Images/planet1.png";
		}
		else
		{
			spritePath = "Images/planet2.png";
		}
	}

	public void chooseRandomRotationSpeed()
	{
		rotationSpeed = (Math.random() * 0.4) + 0.1;

		boolean counterClockwise = (Math.random() < 0.5); //random boolean

		if(counterClockwise)
		{
			rotationSpeed *= -1;
		}
	}

	public void act()
	{
		attractProjectiles();
		rotate();
	}

	public void attractProjectiles()
	{
		ArrayList<Entity> entities = GamePanel.getEntities();

		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);

			if(e instanceof Projectile)
			{
				Projectile p = (Projectile)e;
				
				if(!(p instanceof Beam))
				{
					double distanceFromProjectile = Math.abs(getDistanceFrom(p));

					if((distanceFromProjectile <= radiusOfGravity))
					{
						double xDiff = getXDistanceFrom(p);
						double yDiff = getYDistanceFrom(p);

						// F = G * ((m1 * m2) / (r^2))

						double gravitationalForce = 7 * (mass * p.getMass()) / (distanceFromProjectile * distanceFromProjectile);

						if(xDiff > 0)
						{
							p.setXVelocity(p.getXVelocity() - gravitationalForce);
						}
						else if(xDiff < 0)
						{
							p.setXVelocity(p.getXVelocity() + gravitationalForce);
						}

						if(yDiff > 0)
						{
							p.setYVelocity(p.getYVelocity() - gravitationalForce);
						}
						else if(yDiff < 0)
						{
							p.setYVelocity(p.getYVelocity() + gravitationalForce);
						}
					}
				}
			}
		}
	}

	public void rotate()
	{
		angleInDegrees += rotationSpeed;

		if(angleInDegrees >= 360)
		{
			angleInDegrees -= 360;
		}
	}

	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;

		g2d.rotate(-Math.toRadians(angleInDegrees), centerXPosition, centerYPosition);

		g2d.drawImage(sprite, (int)xPosition, (int)yPosition, null);

		g.setColor(Color.BLACK);
		g.drawOval((int)xPosition, (int)yPosition, (int)width, (int)height);

		g2d.rotate(Math.toRadians(angleInDegrees), centerXPosition, centerYPosition);
	}

	public void addMoon(double orbitRadius)
	{
		Moon moon = new Moon(orbitRadius, this);

		GamePanel.addPlanet(moon);

		moons.add(moon);
	}

	public void addSpaceStation(double orbitRadius)
	{
		SpaceStation station = new SpaceStation(orbitRadius, this);

		GamePanel.addPlanet(station);

		moons.add(station);
	}

	public void importSprite()
	{
		try 
		{
			sprite = ImageIO.read(new File(spritePath));  //import the sprite
		}
		catch (IOException e) 
		{
			System.out.println("Error loading sprite: " + spritePath);
			e.printStackTrace();
		}
	}
}
