

public class AIGunship extends AIShip
{
	
	private static final double ONE_DEGREE_IN_RADIANS = 0.0175;
	
	private static double TURN_SPEED = ONE_DEGREE_IN_RADIANS * 2.5;
	private static double MAX_SPEED = 1;

	private static int DEFAULT_WIDTH = 45;
	private static int DEFAULT_HEIGHT = 30;
	
	public AIGunship()
	{

	}

	public AIGunship(int x, int y)
	{
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public AIGunship(int x, int y, int w, int h)
	{
		super(x, y, w, h);

		firingDelay = 40;
		health = 100;

		sprite = SpriteLibrary.getSprite("enemyShip.png");
		sprite = SpriteLibrary.scaleSprite(sprite, w, h);
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
	
	public void updateVelocity()
	{
		xVelocity = -Math.cos(angleInRadians) * MAX_SPEED;
		yVelocity = Math.sin(angleInRadians) * MAX_SPEED;
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
			
			Projectile p = new Projectile(getCenterXPosition() - xVector * 2.5, getCenterYPosition() + yVector * 2.5, vX, vY, 14);
			p.setDamage(18);
			GamePanel.addEntity(p);
		}
	}
	
	public void destroy()
	{
		GamePanel.removeEntity(this);

		GamePanel.addEntity(new Explosion(centerXPosition, centerYPosition, 45));
	}
}
