
public class Player2Ship extends Ship
{	

	public Player2Ship()
	{

	}

	public Player2Ship(int x, int y)
	{
		this(x, y, 26, 13);
	}

	public Player2Ship(int x, int y, int w, int h)
	{
		super(x, y, w, h);
		
		importSprite("shipReversed.png");
	}
	
	public void shoot()
	{
		if(turnsSinceLastShot >= firingDelay)
		{
			turnsSinceLastShot = 0;
			
			double xVector = 12 * Math.cos(angleInRadians);
			double yVector = 12 * Math.sin(angleInRadians);

			double vX = xVelocity - xVector;
			double vY = yVelocity + yVector;
			
			GamePanel.addEntity(new Projectile(getCenterXPosition() - xVector * 2.5, getCenterYPosition() + yVector * 2.5, vX, vY));
		}
	}
}
