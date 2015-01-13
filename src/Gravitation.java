
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

public class Gravitation
{
	private static BorderLayout mainLayout = new BorderLayout();

	private static JFrame mainWindow = new JFrame("Gravitation");

	private static GamePanel gamePanel;
	private static MainMenu mainMenu;

	private static String state = "mainMenu";

	private static int windowWidth = 1200;
	private static int windowHeight = 600;

	public static void main(String[] args) 
	{	
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
		state = newState;
		initializePanel();
	}

	public static void initializePanel()	//Creates a new GamePanel
	{
		if(state.equals("mainMenu"))
		{
			if(gamePanel != null)
			{
				gamePanel.stop();
				mainWindow.remove(gamePanel);
			}
			mainMenu = new MainMenu(windowWidth, windowHeight);
			mainWindow.add(mainMenu);
			mainMenu.run();
			mainWindow.add(mainMenu, BorderLayout.CENTER);
			mainWindow.pack();
		}
		else if(state.equals("gameRunning"))
		{
			if(mainMenu != null)
			{
				mainMenu.stop();
				mainWindow.remove(mainMenu);
			}
			gamePanel = new GamePanel(windowWidth, windowHeight);
			mainWindow.add(gamePanel);
			gamePanel.run();
			mainWindow.add(gamePanel, BorderLayout.CENTER);
			mainWindow.pack();
		}

		mainWindow.pack();
	}
}
