
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener
{
	private static final double ONE_DEGREE_IN_RADIANS = 0.0175;

	private static double TURN_SPEED = ONE_DEGREE_IN_RADIANS * 4;

	boolean[] keys = new boolean[255];

	final int shipSpeed = 1;

	final int LEFT = 37;	
	final int UP = 38;
	final int RIGHT = 39;
	final int DOWN = 40;
	final int SHIFT = 16;

	final int W = 87;
	final int A = 65;
	final int S = 83;
	final int D = 68;
	final int SPACE = 32;

	final int O = 79;

	final int ENTER = 10;
	final int ESCAPE = 27;


	public void keyPressed(KeyEvent e) 
	{
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) 
	{
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) 
	{
	}

	public void update() //Call this function from your main method to actually look at the keys
	{	

		Ship player1 = GamePanel.getPlayer1Ship();
		Ship player2 = GamePanel.getPlayer2Ship();

		if(player1 != null)
		{
			if(keys[W]) { player1.moveShip("forward", shipSpeed);  }
			if(keys[S]) { player1.moveShip("backward", shipSpeed * 0.8); }
			if(keys[A]) { player1.rotate(TURN_SPEED);  }
			if(keys[D]) { player1.rotate(-TURN_SPEED);   }

			if(keys[SPACE]) { player1.shoot(); }
		}

		if(player2 != null)
		{
			if(keys[DOWN])  { player2.moveShip("forward", shipSpeed);  }
			if(keys[UP])    { player2.moveShip("backward", shipSpeed * 0.8); }
			if(keys[LEFT])  { player2.rotate(TURN_SPEED);  }
			if(keys[RIGHT]) { player2.rotate(-TURN_SPEED);   }

			if(keys[SHIFT]) { player2.shoot(); }
		}

		if(keys[ENTER])  
		{ 
			if(Gravity.getState().equals("gameRunning"))
			{
				GamePanel.resetWorld(); 
				slightPause();
			}
			else if(Gravity.getState().equals("gameOver"))
			{
				Gravity.setState("gameRunning");
			}
		}

		if(keys[O])
		{
			GamePanel.toggleAsteroids();
			slightPause();
		}

		if(keys[ESCAPE]) { Gravity.setState("mainMenu"); }
	}

	public void slightPause()
	{
		try
		{
			Thread.sleep(100);
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
