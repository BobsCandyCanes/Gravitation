import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Ship extends Entity
{	
	protected double maxHealth = 100;

	protected double health = 50;

	protected double angleInRadians = 0;
	
	protected double maxWidth;
	
	protected int turnsSinceLastShot = 0;

	protected int firingSpeed = 8; //Higher number means slower firing
	
	public Ship()
	{

	}

	public Ship(int x, int y)
	{
		this(x, y, 20, 10);
	}

	public Ship(int x, int y, int w, int h)
	{
		xPosition = x;
		yPosition = y;

		width = w;
		height = h;

		maxWidth = Math.sqrt((width * width) + (height * height));
		
		centerXPosition = xPosition + width / 2;
		centerYPosition = yPosition + height / 2;

		bounds = new Rectangle ((int)xPosition, (int)yPosition, (int)width, (int)height);

		importSprite();
	}

	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.rotate(-Math.toRadians(angleInDegrees), centerXPosition, centerYPosition);
		
		g2d.drawImage(sprite, (int)xPosition, (int)yPosition, null);

		g2d.setColor(Color.BLACK);

		g2d.drawRect((int)xPosition, (int)yPosition, (int)width, (int)height);
		
		g2d.rotate(Math.toRadians(angleInDegrees), centerXPosition, centerYPosition);
	}
	
	public void rotate(int degrees) 
	{
		angleInDegrees += degrees;
		
		if(angleInDegrees >= 360)
		{
			angleInDegrees -= 360;
		}
		else if(angleInDegrees <= -360)
		{
			angleInDegrees += 360;
		}
		
		angleInRadians = Math.toRadians(angleInDegrees);
	}

	public void act()
	{
		turnsSinceLastShot++;
		
		calculateLocation();
	}
	
	public void calculateLocation()
	{
		int panelWidth = GamePanel.getPanelWidth();
		int panelHeight = GamePanel.getPanelHeight();

		xPosition += xVelocity;
		yPosition += yVelocity;
		
		if(xPosition <= 0)
		{
			xPosition = 0;
		}
		else if(xPosition + width >= panelWidth)
		{
			xPosition = panelWidth - width;
		}
		if(yPosition <= 0)
		{
			yPosition = 0;
		}
		else if(yPosition + height >= panelHeight)
		{
			yPosition = panelHeight - height;
		}

		centerXPosition = xPosition + width / 2;
		centerYPosition = yPosition + height / 2;

		recalculateBounds();

		xVelocity *= 0.92;
		yVelocity *= 0.92;
	}

	public void recalculateBounds()
	{
		double sin = Math.abs(Math.sin(Math.toRadians(angleInDegrees)));
		double cos = Math.abs(Math.cos(Math.toRadians(angleInDegrees)));
        
        int newWidth = (int) Math.floor(width * cos + height * sin);
        int newHeight = (int) Math.floor(height * cos + width * sin);
		
		bounds = new Rectangle ((int)centerXPosition - newWidth / 2, (int)centerYPosition - newHeight / 2, newWidth, newHeight);
	}
	
	public void destroy()
	{
		GamePanel.removeShip(this);
		
		GamePanel.addEntity(new Explosion(centerXPosition, centerYPosition, 30));
	}

	public void shoot()
	{
		if(turnsSinceLastShot >= firingSpeed)
		{
			turnsSinceLastShot = 0;
			
			double xVector = 12 * Math.cos(angleInRadians);
			double yVector = 12 * Math.sin(angleInRadians);

			double vX = xVelocity + xVector;
			double vY = yVelocity - yVector;
			
			GamePanel.addProjectile(new Projectile(getCenterXPosition() + xVector * 2.5, getCenterYPosition() - yVector * 2.5, vX, vY));
		}
	}
	
	public void shootBeam()
	{
		if(turnsSinceLastShot >= firingSpeed)
		{
			turnsSinceLastShot = 0;

			GamePanel.addProjectile(new Beam(this));
		}
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
	
	public void takeDamage(double damage)
	{
		health -= damage;
		
		if(health <= 0)
		{
			this.destroy();

			return;
		}
	}

	public void importSprite()
	{
		try 
		{
			sprite = ImageIO.read(new File("Images/ship.png"));  //import the sprite
		}
		catch (IOException e) 
		{
			System.out.println("Error loading sprite: ship");
			e.printStackTrace();
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
}