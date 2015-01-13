import java.awt.Color;
import java.awt.Graphics;

public class Explosion extends Entity
{
	private int age = 0;	//the number of steps the object has existed
	
	private double maxRadius;	//maximum size of the explosion
	
	public Explosion(double x, double y, double r)
	{
		maxRadius = r;
		
		centerXPosition = x;
		centerYPosition = y;
		
		xPosition = x - maxRadius / 2;
		yPosition = y - r / 2;
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
		g.setColor(Color.RED);
		
		double currentRadius = age; 
		
		xPosition = centerXPosition - currentRadius / 2;
		yPosition = centerYPosition - currentRadius / 2;
		
		g.fillOval((int)xPosition, (int)yPosition, (int)currentRadius, (int)currentRadius);
		
		for(int i = 0; i < 4; i++)
		{	
			g.setColor(Color.ORANGE);
			
			int size = (int)(Math.random() * maxRadius / 2);

			int xVal = (int)xPosition - (int)(maxRadius / 2) + (int)(Math.random() * (2 * maxRadius));
			int yVal = (int)yPosition - (int)(maxRadius / 2) + (int)(Math.random() * (2 * maxRadius));
			
			g.fillOval(xVal, yVal, size, size);
		}
	}
	
	public void destroy()
	{
		GamePanel.removeEntity(this);
	}
}
