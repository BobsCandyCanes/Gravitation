
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

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
		
		initializeWindow();
		initializePanel();
	}

	public static void initializeWindow()	//Initializes a JFrame
	{
		mainWindow.setSize(windowWidth, windowHeight);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setBackground(Color.WHITE);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setLayout(mainLayout);
		mainWindow.setVisible(true);
	}

	public static void setState(String newState)
	{
		previousState = state;
		state = newState;
		initializePanel();
	}

	public static void initializePanel()	//Creates a new GamePanel
	{
		if(mainPanel != null)
		{
			mainPanel.stop();
			mainWindow.remove(mainPanel);
		}
		if(state.equals("mainMenu"))
		{
			mainPanel = new MainMenu(windowWidth, windowHeight);
		}
		else if(state.equals("gameRunning"))
		{
			mainPanel = new GamePanel(windowWidth, windowHeight);
		}
		else if(state.equals("gameOverMenu"))
		{
			mainPanel = new GameOverMenu(windowWidth, windowHeight);
		}
		else if(state.equals("shipMenu"))
		{
			mainPanel = new ShipMenu(windowWidth, windowHeight, previousState);
		}

		mainWindow.add(mainPanel);
		mainPanel.run();
		mainWindow.add(mainPanel, BorderLayout.CENTER);
		mainWindow.pack();
	}
}
