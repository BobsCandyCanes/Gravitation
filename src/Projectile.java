import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Projectile extends Entity
{	
	protected double damage;

	private int maxDistanceOffScreen = 250;

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

		damage = 10;

		centerXPosition = xPosition + width / 2;
		centerYPosition = yPosition + height / 2;

		bounds = new Rectangle ((int)xPosition, (int)yPosition, (int)width, (int)height);
	}

	public void act()
	{
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
		int panelWidth = GamePanel.getPanelWidth();
		int panelHeight = GamePanel.getPanelHeight();

		if(xPosition < -maxDistanceOffScreen || xPosition > panelWidth + maxDistanceOffScreen
				|| yPosition < -maxDistanceOffScreen || yPosition > panelHeight + maxDistanceOffScreen)
		{
			this.destroy();
		}
	}

	public void destroy()
	{
		GamePanel.removeEntity(this);

		GamePanel.addEntity(new Explosion(centerXPosition, centerYPosition, 10));
	}

	public void draw(Graphics g)
	{
		g.setColor(Color.WHITE);

		g.fillOval((int)xPosition, (int)yPosition, (int)(width / 2), (int)(height / 2));
	}

	public double getDamage()
	{
		return damage;
	}
}
