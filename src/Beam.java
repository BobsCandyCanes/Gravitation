import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;


public class Beam extends Projectile
{
	private final int MAX_LENGTH = 1341;

	private int age;
	private Ship source;

	private Line2D trajectory;
	
	private double xVector;
	private double yVector;

	public Beam(Ship source)
	{
		this.source = source;

		xPosition = source.getCenterXPosition() + source.getWidth() / 2 + 8;
		yPosition = source.getCenterYPosition();
		angleInDegrees = source.getAngleInDegrees();
		
		xVector = 12 * Math.cos(Math.toRadians(angleInDegrees));
		yVector = 12 * Math.sin(Math.toRadians(angleInDegrees));

		height = 4;
		width = MAX_LENGTH;

		damage = 10;
	}

	public void act()
	{
		age++;

		if(age >= 3)
		{
			destroy();
		}

		updatePosition();
		checkForCollision();
	}

	public void updatePosition()
	{
		angleInDegrees = source.getAngleInDegrees();
		
		xVector = 12 * Math.cos(Math.toRadians(angleInDegrees));
		yVector = 12 * Math.sin(Math.toRadians(angleInDegrees));
		
		xPosition = source.getCenterXPosition() + xVector * 2.5;
		yPosition = source.getCenterYPosition() - yVector * 2.5;

		updateTrajectory();
	}

	public void updateTrajectory()
	{		
		double x2 = xPosition + Math.cos(Math.toRadians(angleInDegrees)) * 1000;
		double y2 = yPosition - Math.sin(Math.toRadians(angleInDegrees)) * 1000;
		
		trajectory = new Line2D.Double(xPosition, yPosition, x2, y2);
	}

	public void checkForCollision()
	{
		for(int i = 0; i < GamePanel.getEntities().size(); i++)
		{
			Entity e = GamePanel.getEntities().get(i);

			//Rectangle r1 = new Rectangle(100, 100, 100, 100);
			//Line2D l1 = new Line2D.Float(0, 200, 200, 0);
			//System.out.println("l1.intersects(r1) = " + l1.intersects(r1));

			if(e instanceof Ship)
			{
				Ship s = (Ship) e;
				if(trajectory.intersects(s.getBounds()))
				{
					System.out.println("Collision");

					s.takeDamage(damage);
					destroy();
					return;
				}
			}

			/*
			else if(e instanceof Planet)
			{
				Planet p = (Planet) e;

				double distanceFromPlanet = Math.abs(getDistanceFrom(p));

				if(distanceFromPlanet <= p.getWidth() / 2 + width / 2)
				{
					destroy();
					return;
				}
			}
			 */
		}
	}

	public void destroy()
	{
		GamePanel.removeEntity(this);
	}

	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		/*
		g2d.rotate(-Math.toRadians(angleInDegrees), source.getCenterXPosition(), source.getCenterYPosition());

		g2d.setColor(Color.RED);

		g2d.fillRect((int)xPosition, (int)(yPosition - 1), (int)width, 2);

		g2d.rotate(Math.toRadians(angleInDegrees), source.getCenterXPosition(), source.getCenterYPosition());
		*/

		if(trajectory != null)
		{
			g2d.setColor(Color.RED);
			g2d.draw(trajectory); 
		}
	}
}
