import java.awt.Color;


/**
 * Player2Ship is only used in the multiplayer game mode
 */

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
	
	public void addEngineTrail()
	{
		double xVector = 12 * Math.cos(angleInRadians);
		double yVector = 12 * Math.sin(angleInRadians);
		
		EngineTrail et = new EngineTrail((int)(centerXPosition + xVector), (int)(centerYPosition - yVector));
		et.setColor(Color.RED);
		GamePanel.addEngineTrail(et);
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
			
			Projectile p = new Projectile(getCenterXPosition() - xVector * 2.5, getCenterYPosition() + yVector * 2.5, vX, vY);
			p.setParent(this)
;			GamePanel.addEntity(p);
		}
	}
}
