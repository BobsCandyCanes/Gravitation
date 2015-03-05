import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Explosion extends Entity
{
	private int age = 1;	    //the number of steps the object has existed
	
	private double maxRadius;	//maximum size of the explosion
	
	public Explosion(double x, double y, double r)
	{
		maxRadius = r;
		
		centerXPosition = x;
		centerYPosition = y;
		
		xPosition = x - maxRadius / 2;
		yPosition = y - maxRadius / 2;
		
		sprite = SpriteLibrary.getSprite("explosion.png");
	}
	
	public void act()
	{
		age++;
		
		if(age >= maxRadius)
		{
			this.destroy();
		}
	}
	
	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		
		
		//Draw smaller circles first
		
		g2d.setColor(Color.red.brighter());
		
		for(int i = 0; i < 3; i++)	
		{	
			int size = (int)(Math.random() * maxRadius / 2);

			int xVal = (int)xPosition - (int)(maxRadius / 2) + (int)(Math.random() * (2 * maxRadius));
			int yVal = (int)yPosition - (int)(maxRadius / 2) + (int)(Math.random() * (2 * maxRadius));

			g2d.fillOval(xVal, yVal, size, size);
		}
		
		//Then draw the big circle
		
		double currentRadius = (age * age) / 15; 
		
	
		//top left point of the big circle
		double xPos = centerXPosition - currentRadius / 2;
		double yPos = centerYPosition - currentRadius / 2;

		g2d.drawImage(sprite, (int)xPos, (int)yPos, (int)currentRadius, (int)currentRadius, null);
	}
	
	public void destroy()
	{
		GamePanel.removeEntity(this);
	}
}
