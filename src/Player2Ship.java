
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Player2Ship extends Ship
{	

	public Player2Ship()
	{

	}

	public Player2Ship(int x, int y)
	{
		this(x, y, 25, 12);
	}

	public Player2Ship(int x, int y, int w, int h)
	{
		super(x, y, w, h);

		firingDelay = 8;
		
		importSprite();
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

	public void importSprite()
	{
		try 
		{
			sprite = ImageIO.read(new File("Images/enemyShip.png"));  //import the sprite
		}
		catch (IOException e) 
		{
			System.out.println("Error loading sprite: enemyShip");
			e.printStackTrace();
		}
	}
}
