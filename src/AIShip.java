
public class AIShip extends Player2Ship
{
	private static final double ONE_DEGREE_IN_RADIANS = 0.0175;

	private static double TURN_SPEED = ONE_DEGREE_IN_RADIANS * 3.5;
	private static double MAX_SPEED = 4;
	
	private static double DEFAULT_WIDTH = 25;
	private static double DEFAULT_HEIGHT = 18;

	protected double targetAngleInRadians;

	public AIShip()
	{
	}

	public AIShip(int x, int y)
	{
		this(x, y, (int)DEFAULT_WIDTH, (int)DEFAULT_HEIGHT);
	}

	public AIShip(int x, int y, int w, int h)
	{
		super(x, y, w, h);

		firingDelay = 30;
		health = 10;

		importSprite("enemyShip.png");
	}

	public void act()
	{
		turnsSinceLastShot++;

		moveTowardsEnemy();

		if(GamePanel.getPlayer1Ship() != null)
		{
			shoot();
		}

		calculateLocation();

		checkForCollision();
	}

	public void moveTowardsEnemy()
	{
		Ship target = GamePanel.getPlayer1Ship();

		if(target != null)
		{
			targetAngleInRadians = calcAngleTo(target);
			
			avoidPlanets();
			
			turnTowardsTargetAngle();
			
			updateVelocity();
		}
		else
		{
			xVelocity *= 0.8;
			yVelocity *= 0.8;
		}
	}

	public void avoidPlanets()
	{	
		for(Entity e : GamePanel.getEntities())
		{
			if(e instanceof Planet)
			{
				if(getDistanceFrom(e) < Planet.getDefaultSize())
				{					
					double angleToPlanet = calcAngleTo(e);
					
					if(angleToPlanet < angleInRadians)
					{
						targetAngleInRadians += Math.PI / 2;
					}
					else
					{
						targetAngleInRadians -= Math.PI / 2;
					}
				}
			}
		}
	}
	
	public void turnTowardsTargetAngle()
	{
		if(angleInRadians > targetAngleInRadians)
		{
			angleInRadians -= TURN_SPEED;
		}
		else if(angleInRadians < targetAngleInRadians)
		{
			angleInRadians += TURN_SPEED;
		}

		angleInDegrees = Math.toDegrees(angleInRadians);
	}

	public double calcAngleTo(Entity target)
	{
		double targetAngleInRadians;

		double xDifference = getXDistanceFrom(target);
		double yDifference = getYDistanceFrom(target);

		if(xDifference > 0)
		{
			targetAngleInRadians = Math.PI + Math.atan(-yDifference / xDifference);
		}
		else if(xDifference < 0)
		{
			targetAngleInRadians = Math.atan(-yDifference / xDifference);
		}
		else
		{
			if(yDifference > 0)
			{
				targetAngleInRadians = Math.PI / 2;
			}
			else
			{
				targetAngleInRadians = -Math.PI / 2;
			}
		}

		return targetAngleInRadians;
	}
	
	public void updateVelocity()
	{
		xVelocity = -Math.cos(angleInRadians) * MAX_SPEED;
		yVelocity = Math.sin(angleInRadians) * MAX_SPEED;
	}

	public void checkForCollision()
	{
		for(Entity e : GamePanel.getEntities())
		{
			if(e != this)
			{
				if(e instanceof Planet)
				{
					if(getDistanceFrom(e) < (e.getWidth() / 2) + (width / 2))
					{
						destroy();
						return;
					}
				}
				else if(e instanceof Ship)
				{
					if(getDistanceFrom(e) < (e.getWidth() / 2) + (width / 2))
					{
						Ship s = (Ship)e;
						takeDamage(15);
						s.takeDamage(15);
						return;
					}
				}
			}
		}
	}
}
