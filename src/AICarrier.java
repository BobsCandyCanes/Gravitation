import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class AICarrier extends AIShip
{
	
	private final double ONE_DEGREE_IN_RADIANS = 0.0175;
	
	public AICarrier()
	{

	}

	public AICarrier(int x, int y)
	{
		this(x, y, 40, 20);
	}

	public AICarrier(int x, int y, int w, int h)
	{
		super(x, y, w, h);

		firingDelay = 40;
		health = 200;
		
		MAX_SPEED = 1;
		MIN_SPEED = 0.8;
		TURN_SPEED = ONE_DEGREE_IN_RADIANS * 2.5;

		importSprite();
	}
	
	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.rotate(-Math.toRadians(angleInDegrees), centerXPosition, centerYPosition);
		
		g2d.setColor(Color.GREEN);
		
		g2d.fillRect((int)xPosition, (int)yPosition, (int)width, (int)height);

		g2d.setColor(Color.BLACK);

		g2d.drawRect((int)xPosition, (int)yPosition, (int)width, (int)height);
		
		g2d.rotate(Math.toRadians(angleInDegrees), centerXPosition, centerYPosition);
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


}
