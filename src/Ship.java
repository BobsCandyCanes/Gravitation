import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;



public class Ship extends Entity
{	
	private static final double TWO_PI = 2 * Math.PI;
	
	protected double maxHealth = 75;
	protected double healthRegen = 0.1;
	protected double maxSpeed = 3;
	protected double armorValue = 1;

	protected double health = maxHealth;

	protected int firingDelay = 8; //Higher number means slower firing

	protected double angleInRadians = 0;

	protected double maxWidth;

	protected int turnsSinceLastShot = 0;

	public Ship()
	{

	}

	public Ship(int x, int y)
	{
		this(x, y, 26, 13);
	}

	public Ship(int x, int y, int w, int h)
	{
		// Setting a mass will make the ship be affected by gravity
		// Which is awesome, but super buggy so don't do it
		// mass = 1;
		
		xPosition = x;
		yPosition = y;

		width = w;
		height = h;

		maxWidth = Math.sqrt((width * width) + (height * height));

		centerXPosition = xPosition + width / 2;
		centerYPosition = yPosition + height / 2;

		bounds = new Rectangle ((int)xPosition, (int)yPosition, (int)width, (int)height);

		importSprite("ship.png");

		if(!GamePanel.isMultiplayer())
		{
			initAttributes();
		}
	}

	public void initAttributes()
	{
		maxHealth   = ProfileManager.getMaxHealth();
		healthRegen = ProfileManager.getHealthRegen();
		maxSpeed    = ProfileManager.getMaxSpeed();
		armorValue  = ProfileManager.getArmorValue();

		health = maxHealth;
	}

	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;

		g2d.rotate(-Math.toRadians(angleInDegrees), centerXPosition, centerYPosition);

		g2d.drawImage(sprite, (int)xPosition, (int)yPosition, null);

		g2d.rotate(Math.toRadians(angleInDegrees), centerXPosition, centerYPosition);
	}

	public void rotate(double radians) 
	{
		angleInRadians += radians;

		if(angleInRadians >=  TWO_PI)
		{
			angleInRadians -= TWO_PI;
		}
		else if(angleInRadians <= -TWO_PI)
		{
			angleInRadians += TWO_PI;
		}

		angleInDegrees = Math.toDegrees(angleInRadians);
	}

	public void act()
	{
		turnsSinceLastShot++;

		calculateLocation();
		checkForCollision();

		if(health < maxHealth)
		{
			regenerateHealth();
		}

		addEngineTrail();
	}

	public void addEngineTrail()
	{
		double xVector = 12 * Math.cos(angleInRadians);
		double yVector = 12 * Math.sin(angleInRadians);
		
		EngineTrail et = new EngineTrail((int)(centerXPosition - xVector), (int)(centerYPosition + yVector));
		GamePanel.addEngineTrail(et);
	}
	
	public void calculateLocation()
	{
		xPosition += xVelocity;
		yPosition += yVelocity;

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

		centerXPosition = xPosition + width / 2;
		centerYPosition = yPosition + height / 2;

		recalculateBounds();

		xVelocity *= 0.96;
		yVelocity *= 0.96;
	}

	public void recalculateBounds()
	{
		double sin = Math.abs(Math.sin(Math.toRadians(angleInDegrees)));
		double cos = Math.abs(Math.cos(Math.toRadians(angleInDegrees)));

		int newWidth = (int) Math.floor(width * cos + height * sin);
		int newHeight = (int) Math.floor(height * cos + width * sin);

		bounds = new Rectangle ((int)centerXPosition - newWidth / 2, (int)centerYPosition - newHeight / 2, newWidth, newHeight);
	}

	public void checkForCollision()
	{
		for(Entity e : GamePanel.getEntities())
		{
			if(e instanceof Planet && !(e instanceof SpaceStation))
			{
				if(getDistanceFrom(e) < (e.getWidth() / 2) + (width / 2))
				{
					xPosition -= xVelocity;
					yPosition -= yVelocity;

					xVelocity *= - 0.75;
					yVelocity *= - 0.75;

					takeDamage(health / 2 + 1);

					GamePanel.addEntity(new Explosion(centerXPosition + xVelocity, centerYPosition + yVelocity, 15));

					return;
				}
			}
			else if(e instanceof Ship)
			{
				if(e != this)
				{
					if(getDistanceFrom(e) < (e.getWidth() / 2) + (width / 2))
					{
						Ship s = (Ship)e;
						takeDamage(15);
						s.takeDamage(15);

						xVelocity *= 0.1;
						yVelocity *= 0.1;
						return;
					}
				}
			}
		}
	}

	public void regenerateHealth()
	{
		health += healthRegen;
	}

	public void destroy()
	{
		GamePanel.removeEntity(this);

		GamePanel.addEntity(new Explosion(centerXPosition, centerYPosition, 30));
	}

	public void shoot()
	{
		if(turnsSinceLastShot >= firingDelay)
		{
			turnsSinceLastShot = 0;

			double xVector = 12 * Math.cos(angleInRadians);
			double yVector = 12 * Math.sin(angleInRadians);

			double vX = xVelocity + xVector;
			double vY = yVelocity - yVector;

			Projectile p = new Projectile(getCenterXPosition() + xVector * 2.5, getCenterYPosition() - yVector * 2.5, vX, vY);
			p.setParent(this);
	
			GamePanel.addEntity(p);
		}
	}

	public void shootBeam()
	{
		if(turnsSinceLastShot >= firingDelay)
		{
			turnsSinceLastShot = 0;

			GamePanel.addEntity(new Beam(this));
		}
	}

	public void takeDamage(double damage)
	{
		health -= damage - armorValue;

		if(health <= 0)
		{
			this.destroy();

			return;
		}
	}

	public void moveShip(String direction, double magnitude)
	{
		if(direction.equals("forward"))
		{
			yVelocity -= Math.sin(angleInRadians) / 2;

			xVelocity += Math.cos(angleInRadians) / 2;
		}
		else if(direction.equals("backward"))
		{
			yVelocity += Math.sin(angleInRadians) / 2;

			xVelocity -= Math.cos(angleInRadians) / 2;
		}
	}

	public double getHealth()
	{
		return health;
	}

	public double getMaxHealth()
	{
		return maxHealth;
	}

	public void setMaxHealth(double h)
	{
		maxHealth = h;
	}

	public double getXVelocity()
	{
		return xVelocity;
	}

	public double getYVelocity()
	{
		return yVelocity;
	}

	public void setXVelocity(double xVelocity)
	{
		this.xVelocity = xVelocity;
	}

	public void setYVelocity(double yVelocity)
	{
		this.yVelocity = yVelocity;
	}
}
