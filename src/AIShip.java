
public class AIShip extends Player2Ship
{
	public AIShip()
	{

	}

	public AIShip(int x, int y)
	{
		this(x, y, 20, 10);
	}

	public AIShip(int x, int y, int w, int h)
	{
		super(x, y, w, h);

		importSprite();

		firingSpeed = 25;
	}

	public void act()
	{
		turnsSinceLastShot++;

		moveTowardsEnemy();

		shoot();

		calculateLocation();
	}

	public void moveTowardsEnemy()
	{
		Ship target = GamePanel.getPlayer1Ship();

		if(target != null)
		{
			double xDifference = getXDistanceFrom(target) / 10;
			double yDifference = getYDistanceFrom(target);

			double ratio = Math.abs(yDifference/xDifference);
			
			//angleInRadians = Math.atan(xDifference / yDifference);
			//angleInDegrees = Math.toDegrees(angleInRadians);

			if(yDifference > 0)
			{
				yVelocity = ratio;
				angleInDegrees ++;
			}
			else if(yDifference < 0)
			{
				yVelocity = -ratio;
				angleInDegrees --;
			}	

			if(xDifference > 0)
			{
				xVelocity = 1;
			}
			else if(xDifference < 0)
			{
				xVelocity = -1;
			}	
			
			angleInRadians = Math.toRadians(angleInDegrees);
		}
	}
}
