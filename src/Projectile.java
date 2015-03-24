import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * A projectile is shot from a ship
 * 
 * Projectiles are affected by gravity (see Planet class)
 * 
 * Projectiles explode and deal damage upon collision
 */

public class Projectile extends Entity
{	
	private static final int MAX_AGE = 800;
	
	private Ship parent;
	
	private Color color = Color.GREEN;
	
	private int age = 1;
	
	protected double damage;
	
	public Projectile()
	{

	}

	public Projectile(double x, double y, double vX, double vY)
	{
		this(x, y, vX, vY, 8);
	}

	public Projectile(double x, double y, double vX, double vY, int size)
	{
		xPosition = x - width / 2;
		yPosition = y - width / 2;

		xVelocity = vX;
		yVelocity = vY;

		width = size;
		height = size;

		mass = 10;

		damage = 12;

		centerXPosition = xPosition + width / 2;
		centerYPosition = yPosition + height / 2;

		bounds = new Rectangle ((int)xPosition, (int)yPosition, (int)width, (int)height);
	}

	public void act()
	{
		age++;
		
		if(age >= MAX_AGE)
		{
			destroy();
		}
		
		calculateLocation();

		checkForCollision();

		checkIfOffScreen();
	}

	public void checkForCollision()
	{
		for(int i = 0; i < GamePanel.getEntities().size(); i++)
		{
			Entity e = GamePanel.getEntities().get(i);

			if(e instanceof Ship)
			{
				Ship s = (Ship) e;
				if(this.getBounds().intersects(s.getBounds()))
				{
					if(parent instanceof AIShip && s instanceof AIShip)
					{
						damage *= 0.2;
					}
					
					s.takeDamage(damage);
					destroy();
					return;
				}
			}
			else if(e instanceof Planet)
			{
				Planet p = (Planet) e;

				double distanceFromPlanet = Math.abs(getDistanceFrom(p));

				if(distanceFromPlanet <= p.getWidth() / 2 + width / 2)
				{
					destroy();
					return;
				}
			}
		}
	}

	public void calculateLocation()
	{
		xPosition += xVelocity;
		yPosition += yVelocity;

		centerXPosition = xPosition + width / 2;
		centerYPosition = yPosition + height / 2;
	}

	public void checkIfOffScreen()
	{
		int worldWidth = GamePanel.getWorldWidth();
		int worldHeight = GamePanel.getWorldHeight();

		//Code for wrapping around edges of map
		if (xPosition > worldWidth) 
		{
			xPosition = xPosition % worldWidth;
		}
		else if (xPosition < 0) 
		{
			xPosition = worldWidth - xPosition;
		}

		if (yPosition > worldHeight) 
		{	
			yPosition = yPosition % worldHeight;
		}
		else if (yPosition < 0) 
		{
			yPosition = worldHeight - yPosition;
		}
	}

	public void destroy()
	{
		GamePanel.removeEntity(this);

		GamePanel.addEntity(new Explosion(centerXPosition, centerYPosition, 10));
	}

	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval((int)xPosition, (int)yPosition, (int)(width / 2), (int)(height / 2));
		
		g.setColor(Color.WHITE);
		g.drawOval((int)xPosition, (int)yPosition, (int)(width / 2), (int)(height / 2));
	}
	
	public void setParent(Ship s)
	{
		parent = s;
		
		if(parent instanceof AIShip || parent instanceof Player2Ship)
		{
			color = Color.red;
		}
	}

	public void setDamage(double d)
	{
		damage = d;
	}
	
	public double getDamage()
	{
		return damage;
	}
}
