import java.awt.Color;
import java.awt.Graphics;


public class EngineTrail extends Entity
{
	private final int MAX_AGE = 10;
	
	private int age = 1;
	private int colorIncrement = 255 / MAX_AGE;
	
	private Color color = Color.GREEN.brighter();
	
	public EngineTrail(int x, int y)
	{
		xPosition = x;
		yPosition = y;
		
		width = 14;
		height = 14;
	}
	
	public void act()
	{
		age++;
		
		if(age > MAX_AGE)
		{
			GamePanel.removeEngineTrail(this);
		}
		
		
		// Makes the color darker
		// Implementation taken straight from Color.darker()
		color = new Color(Math.max((int)(color.getRed() - colorIncrement), 0),  
				Math.max((int)(color.getGreen() - colorIncrement), 0), 
				Math.max((int)(color. getBlue() - colorIncrement), 0));
	}
	
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval((int)(xPosition - width / 2), (int)(yPosition - height / 2), (int)width, (int)height);
		g.setColor(Color.BLACK);
	}
	
	public void setColor(Color c)
	{
		color = c;
	}
}
