
public class AIShip extends Player2Ship
{
	private static final double ONE_DEGREE_IN_RADIANS = 0.0175;

	protected static double TURN_SPEED = ONE_DEGREE_IN_RADIANS * 3.5;
	protected static double MAX_SPEED = 4;
	protected static double MIN_SPEED = 2;
	
	protected static double DEFAULT_WIDTH = 25;
	protected static double DEFAULT_HEIGHT = 18;

	private double targetAngleInRadians;

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

		avoidPlanets();

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
			double xDifference = getXDistanceFrom(target);
			double yDifference = getYDistanceFrom(target);

			targetAngleInRadians = calcVelocityAngle();

			turnTowardsTargetAngle();

			xVelocity = xDifference / 50;
			yVelocity = yDifference / 50;

			enforceSpeedLimit();
		}
		else
		{
			xVelocity *= 0.8;
			yVelocity *= 0.8;
		}
	}

	public void enforceSpeedLimit()
	{
		if(Math.abs(xVelocity) > MAX_SPEED)
		{
			if(xVelocity > 0)
			{
				xVelocity = MAX_SPEED;
			}
			else
			{
				xVelocity = -MAX_SPEED;
			}
		}
		else if(Math.abs(xVelocity) < MIN_SPEED)
		{
			if(xVelocity > 0)
			{
				xVelocity = MIN_SPEED;
			}
			else
			{
				xVelocity = -MIN_SPEED;
			}
		}

		if(Math.abs(yVelocity) > MAX_SPEED)
		{
			if(yVelocity > 0)
			{
				yVelocity = MAX_SPEED;
			}
			else
			{
				yVelocity = -MAX_SPEED;
			}
		}
		else if(Math.abs(yVelocity) < MIN_SPEED)
		{
			if(yVelocity > 0)
			{
				yVelocity = MIN_SPEED;
			}
			else
			{
				yVelocity = -MIN_SPEED;
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

	public void avoidPlanets()
	{
		for(Entity e : GamePanel.getEntities())
		{
			if(e instanceof Planet)
			{
				if(getDistanceFrom(e) < Planet.getDefaultSize() * 1.5)
				{
					double xDiff = getXDistanceFrom(e);
					double yDiff = getYDistanceFrom(e);

					double repellingForce = (1 / getDistanceFrom(e)) * 50;

					if(xDiff > 0)
					{
						xVelocity -= repellingForce;
					}
					else if(xDiff < 0)
					{
						xVelocity += repellingForce;
					}

					if(yDiff > 0)
					{
						yVelocity -= repellingForce;
					}
					else if(yDiff < 0)
					{
						yVelocity += repellingForce;
					}
				}
			}
		}
	}

	public double calcVelocityAngle()
	{
		double velocityAngleInRadians;

		Ship target = GamePanel.getPlayer1Ship();

		double xDifference = getXDistanceFrom(target);
		double yDifference = getYDistanceFrom(target);


		if(xDifference > 0)
		{
			velocityAngleInRadians = Math.PI + Math.atan(-yDifference / xDifference);
		}
		else if(xDifference < 0)
		{
			velocityAngleInRadians = Math.atan(-yDifference / xDifference);
		}
		else
		{
			if(yDifference < 0)
			{
				velocityAngleInRadians = Math.PI / 2;
			}
			else
			{
				velocityAngleInRadians = -Math.PI / 2;
			}
		}

		return velocityAngleInRadians;
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
						takeDamage(10);
						s.takeDamage(10);
						return;
					}
				}
			}
		}
	}
}
