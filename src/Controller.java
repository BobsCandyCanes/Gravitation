
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener
{
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
			if(keys[S]) { player1.moveShip("backward", shipSpeed); }
			if(keys[A]) { player1.rotate(4);  }
			if(keys[D]) { player1.rotate(-4);   }

			if(keys[SPACE]) { player1.shoot(); }
		}
		
		if(GamePanel.isMultiplayer() && player2 != null)
		{
			if(keys[DOWN])  { player2.moveShip("forward", shipSpeed);  }
			if(keys[UP])    { player2.moveShip("backward", shipSpeed); }
			if(keys[LEFT])  { player2.rotate(4);  }
			if(keys[RIGHT]) { player2.rotate(-4);   }

			if(keys[SHIFT]) { player2.shoot(); }
		}

		if(keys[ENTER])  
		{ 
			GamePanel.resetWorld(); 
			slightPause();
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
