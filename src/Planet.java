import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Planet extends Entity
{
	private static final int DEFAULT_SIZE = 200;
	private static final int G_CONSTANT = 9;

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

		mass = size + 40;

		centerXPosition = xPosition + width / 2;
		centerYPosition = yPosition + height / 2;

		radiusOfGravity = (int)(width * 10);

		chooseRandomSprite();
		chooseRandomRotationSpeed();
	}

	public void chooseRandomSprite()
	{
		//TODO: I've got an idea of how to make this better
		
		Random rand = new Random();

		int randInt = rand.nextInt(5);

		if(randInt == 0)
		{
			importSprite("planet1.png");
		}
		else if(randInt == 1)
		{
			importSprite("planet2.png");
		}
		else if(randInt == 2)
		{
			importSprite("planet3.png");
		}
		else if(randInt == 3)
		{
			importSprite("planet4.png");
		}
		else
		{
			importSprite("planet5.png");
		}
	}

	public void chooseRandomRotationSpeed()
	{
		Random random = new Random();
		
		//Choose a random speed between -0.25 an 0.25
		rotationSpeed = (random.nextDouble() * 0.5) - 0.25;
		
		//Make sure it's not too slow
		rotationSpeed += 0.05 * Math.signum(rotationSpeed);
	}

	public void act()
	{
		attractEntities();
		rotate();
	}

	public void attractEntities()
	{
		List<Entity> entities = GamePanel.getEntities();

		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);

			if(e instanceof Projectile || e instanceof Ship)	//TODO: Bad practice, fix it
			{
				double distanceFromEntity = Math.abs(getDistanceFrom(e));

				if((distanceFromEntity <= radiusOfGravity))
				{
					double xDiff = getXDistanceFrom(e);
					double yDiff = -getYDistanceFrom(e);

					double vectorAngle = Math.atan2(yDiff, xDiff);
					
					// F = G * ((m1 * m2) / (r^2))

					double gForce = G_CONSTANT * (mass * e.getMass()) / (distanceFromEntity * distanceFromEntity);			
					
					double gForceX = -gForce * Math.cos(vectorAngle);
					double gForceY = gForce * Math.sin(vectorAngle);
					
					e.setXVelocity(e.getXVelocity() + gForceX);
					e.setYVelocity(e.getYVelocity() + gForceY);
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

		g2d.setColor(Color.BLACK);
		g2d.drawOval((int)xPosition, (int)yPosition, (int)width, (int)height);

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

		int numMoons = rand.nextInt(3) + 1;

		for(int i = 0; i < numMoons; i++)
		{
			addMoon((width / 3) * i + width);
		}

		//TODO: Maybe fully implement this later
		//addSpaceStation(width + 30);
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
