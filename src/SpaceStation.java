import java.awt.Color;
import java.awt.Graphics;


public class SpaceStation extends Moon
{
	private String faction = "neutral";

	private int turnsSinceLastShot = 0;

	private int firingSpeed = 15;

	public SpaceStation(double r, Planet p)
	{	
		super(r, p);

		width = 10;
		height = 10;

		mass = 10;
	}

	public void act()
	{
		turnsSinceLastShot++;

		super.act();
		checkForFactionChange();
		shootAtEnemy();
	}

	public void checkForFactionChange()
	{
		Ship player1Ship = GamePanel.getPlayer1Ship();
		Ship player2Ship = GamePanel.getPlayer2Ship();

		boolean touchingPlayer1 = false;
		boolean touchingPlayer2 = false;

		if(player1Ship != null && this.getBounds().intersects(player1Ship.getBounds()))
		{
			touchingPlayer1 = true;
		}

		if(player2Ship != null && this.getBounds().intersects(player2Ship.getBounds()))
		{
			touchingPlayer2 = true;
		}

		if(touchingPlayer1 && !touchingPlayer2)
		{
			faction = "player1";
		}
		else if(touchingPlayer2 && !touchingPlayer1)
		{
			faction = "player2";
		}
	}

	public void shootAtEnemy()
	{
		if(!faction.equals("neutral"))
		{
			Ship target = new Ship();

			if(faction.equals("player1"))
			{
				target = GamePanel.getPlayer2Ship();
			}
			else if(faction.equals("player2"))
			{
				target = GamePanel.getPlayer1Ship();
			}

			if(target != null)
			{
				if(turnsSinceLastShot >= firingSpeed)
				{
					turnsSinceLastShot = 0;

					double xDifference = getXDistanceFrom(target) / 10;
					double yDifference = getYDistanceFrom(target) / 10;

					double projectileXVelocity = xDifference / 3 * 4;
					double projectileYVelocity = yDifference / 3 * 4;
					
					GamePanel.addEntity(new Projectile(centerXPosition, centerYPosition, projectileXVelocity, projectileYVelocity));
				}
			}
		}
	}

	public void draw(Graphics g)
	{
		if(faction.equals("player1"))
		{
			g.setColor(Color.BLUE);
		}
		else if(faction.equals("player2"))
		{
			g.setColor(Color.RED);
		}
		else
		{
			g.setColor(Color.MAGENTA);
		}

		g.fillOval((int)xPosition, (int)yPosition, (int)width, (int)height);

		g.setColor(Color.BLACK);

		g.drawOval((int)xPosition, (int)yPosition, (int)width, (int)height);
	}
}
