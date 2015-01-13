import java.awt.Color;
import java.awt.Graphics;

public class Asteroid extends Projectile
{
	private int size;
	
	public Asteroid(double x, double y, double vX, double vY, int s)
	{
		super(x, y, vX, vY);
		
		size = s;
		damage = 2;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(new Color(139,69,19));
		
		g.fillOval((int)xPosition, (int)yPosition, size, size);
		
		g.setColor(Color.BLACK);
		
		g.drawOval((int)xPosition, (int)yPosition, size, size);
	}
}
