
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Gravity
{
	private static BorderLayout mainLayout = new BorderLayout();

	private static JFrame mainWindow = new JFrame("Gravity");

	private static MainPanel mainPanel;

	private static String previousState;
	private static String state = "mainMenu";

	private static int windowWidth = 1300;
	private static int windowHeight = 700;

	public static void main(String[] args) 
	{		
		SpriteLibrary.importAllSprites();
		ProfileManager.initializeProfiles();

		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				initializeWindow();
				initializePanel();
			}
		});
	}

	public static void initializeWindow()	//Initializes a JFrame
	{
		mainWindow.setSize(windowWidth, windowHeight);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setBackground(Color.WHITE);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setLayout(mainLayout);
		mainWindow.setResizable(false);
		mainWindow.setVisible(true);
	}

	//If there is no mainPanel, creates a new mainMenu
	//Otherwise, switches mainPanel to match the game state
	
	public static void initializePanel()
	{
		if(mainPanel != null)
		{
			mainPanel.stop();
			mainWindow.remove(mainPanel);
			mainPanel = null;
		}
		
		if(state.equals("mainMenu"))
		{
			mainPanel = new MainMenu(windowWidth, windowHeight);
		}
		else if(state.equals("gameRunning"))
		{
			mainPanel = new GamePanel(windowWidth, windowHeight);
		}
		else if(state.equals("gameOver"))
		{
			mainPanel = new GameOverMenu(windowWidth, windowHeight);
		}
		else if(state.equals("upgradeMenu"))
		{
			mainPanel = new UpgradeMenu(windowWidth, windowHeight, previousState);
		}
		else if(state.equals("tutorialMenu"))
		{
			mainPanel = new TutorialMenu(windowWidth, windowHeight);
		}

		mainWindow.add(mainPanel);
		mainPanel.run();	
		mainWindow.pack();
	}

	public static String getState()
	{
		return state;
	}

	public static void setState(String newState)
	{
		previousState = state;
		state = newState;
		initializePanel();
	}
}
