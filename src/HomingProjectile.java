
public class HomingProjectile extends Projectile
{
	private Ship target;

	public HomingProjectile(double x, double y, double vX, double vY, Ship t)
	{
		this(x, y, vX, vY, 12, t);
	}

	public HomingProjectile(double x, double y, double vX, double vY, int size, Ship t)
	{
		super(x, y, vX, vY, size);

		target = t;
	}

	public void calculateLocation()
	{
		goTowardsTarget();

		xPosition += xVelocity;
		yPosition += yVelocity;

		checkIfOffScreen();

		centerXPosition = xPosition + width / 2;
		centerYPosition = yPosition + height / 2;
	}

	public void goTowardsTarget()
	{
		if(target != null)
		{
			double xDistance = getXDistanceFrom(target);

			if(xDistance > 0)	//Target is to the right
			{
				xVelocity += 0.8; //xDistance / 500;
			}
			else if(xDistance < 0)
			{
				xVelocity -= 0.8; //xDistance / 500;
			}

			double yDistance = getYDistanceFrom(target);

			if(yDistance > 0)	//Target is to the right
			{
				yVelocity += 0.8; //yDistance / 500;
			}
			else if(yDistance < 0)
			{
				yVelocity -= 0.8; //yDistance / 500;
			}
		}
	}
}
