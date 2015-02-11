import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;


public class Planet extends Entity
{
	private static final int DEFAULT_SIZE = 140;
	
	protected int radiusOfGravity;

	private ArrayList<Moon> moons = new ArrayList<Moon>();

	private double rotationSpeed;

	public Planet()
	{
	}

	public Planet(int x, int y)
	{
		this(x, y, DEFAULT_SIZE);
	}

	public Planet(int x, int y, int size)
	{
		xPosition = x - width / 2;
		yPosition = y - height / 2;

		width = size;
		height = size;

		mass = size + 20;

		centerXPosition = xPosition + width / 2;
		centerYPosition = yPosition + height / 2;

		radiusOfGravity = (int)(width * 10);

		chooseRandomSprite();
		chooseRandomRotationSpeed();
	}

	public void chooseRandomSprite()
	{
		Random rand = new Random();
		
		int randInt = rand.nextInt(2);

		if(randInt == 0)
		{
			importSprite("planet1.png");
		}
		else
		{
			importSprite("planet2.png");
		}
	}

	public void chooseRandomRotationSpeed()
	{
		rotationSpeed = (Math.random() * 0.25) + 0.05;

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

			if(e instanceof Projectile || e instanceof Ship)
			{
				if(!(e instanceof Beam))
				{
					double distanceFromProjectile = Math.abs(getDistanceFrom(e));

					if((distanceFromProjectile <= radiusOfGravity))
					{
						double xDiff = getXDistanceFrom(e);
						double yDiff = getYDistanceFrom(e);

						// F = G * ((m1 * m2) / (r^2))

						double gravitationalForce = 9 * (mass * e.getMass()) / (distanceFromProjectile * distanceFromProjectile);

						if(xDiff > 0)
						{
							e.setXVelocity(e.getXVelocity() - gravitationalForce);
						}
						else if(xDiff < 0)
						{
							e.setXVelocity(e.getXVelocity() + gravitationalForce);
						}

						if(yDiff > 0)
						{
							e.setYVelocity(e.getYVelocity() - gravitationalForce);
						}
						else if(yDiff < 0)
						{
							e.setYVelocity(e.getYVelocity() + gravitationalForce);
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

	public void addSpaceStation(double orbitRadius)
	{
		SpaceStation station = new SpaceStation(orbitRadius, this);

		GamePanel.addEntity(station);

		moons.add(station);
	}

	public void addRandomMoons()
	{
		Random rand = new Random();
		
		int numMoons = rand.nextInt(4) + 1;

		for(int i = 0; i < numMoons; i++)
		{
			addMoon((width / 3) * i + width);
		}

		addSpaceStation(width + 30);
	}

	public void addMoon(double orbitRadius)
	{
		Moon moon = new Moon(orbitRadius, this);

		GamePanel.addEntity(moon);

		moons.add(moon);
	}
	
	public static int getDefaultSize()
	{
		return DEFAULT_SIZE;
	}
}
