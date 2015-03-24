import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameOverMenu extends MainMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;

	protected static double buttonWidth = 225;
	protected static double buttonHeight = 50;
	protected static int fontSize = 28;
	
	private static BorderLayout mainLayout = new BorderLayout();
	
	private static JPanel topPanel;
	private static JPanel buttonPanel;

	private GameButton retry;
	private GameButton shipMenu;
	private GameButton mainMenu;

	private int stepCounter = 0;
	private int experienceAdded = 0;
	
	private int score = 0;

	public GameOverMenu(int width, int height)
	{
		setBackground(Color.BLACK);

		panelWidth = width;
		panelHeight = height;

		setPreferredSize(new Dimension(panelWidth, panelHeight));
		
		setLayout(mainLayout);
		
		score = GamePanel.getScore();
		
		initializePanels();
		initializeButtons();
	}
	
	public void initializePanels()
	{
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setOpaque(false);
		buttonPanel.setBackground(Color.BLACK);
		add(buttonPanel, BorderLayout.CENTER);
		
		topPanel = new JPanel();
		topPanel.setBackground(Color.DARK_GRAY);
		topPanel.setPreferredSize(new Dimension(100, 90));
		
		JLabel scoreLabel = new JLabel("Score:  " + score);
		scoreLabel.setFont(new Font("Lucida", Font.ITALIC, 20));
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setPreferredSize(new Dimension(250, 60));

		
		topPanel.add(scoreLabel);
		
		add(topPanel, BorderLayout.NORTH);
	}

	public void initializeButtons()
	{
		retry = new GameButton("Retry", "menuButton.png", (int)buttonWidth, (int)buttonHeight);
		shipMenu = new GameButton("Upgrades", "menuButton.png", (int)buttonWidth, (int)buttonHeight);
		mainMenu = new GameButton("Main Menu", "menuButton.png", (int)buttonWidth, (int)buttonHeight);

		retry.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(experienceAdded < GamePanel.getScore())
				{
					ProfileManager.addExperience(score - experienceAdded);
				}
				
				ProfileManager.saveProfile();
				GamePanel.setMultiplayer(false);
				Gravity.setState("gameRunning");
			}
		});
		
		shipMenu.addActionListener(new ActionListener()
		{			
			public void actionPerformed(ActionEvent e)
			{
				if(experienceAdded < GamePanel.getScore())
				{
					ProfileManager.addExperience(score - experienceAdded);
				}
				
				ProfileManager.saveProfile();
				Gravity.setState("upgradeMenu");
			}
		});

		mainMenu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(experienceAdded < GamePanel.getScore())
				{
					ProfileManager.addExperience(score - experienceAdded);
				}
				
				ProfileManager.saveProfile();
				GamePanel.setMultiplayer(true);
				Gravity.setState("mainMenu");
			}
		});

		buttonPanel.add(Box.createVerticalStrut(panelHeight / 3));
		buttonPanel.add(retry);
		buttonPanel.add(Box.createVerticalStrut(6));
		buttonPanel.add(shipMenu);
		buttonPanel.add(Box.createVerticalStrut(6));
		buttonPanel.add(mainMenu);
	}

	public void actionPerformed(ActionEvent e)
	{
		this.requestFocus();

		stepCounter++;

		if(stepCounter >= 50)
		{
			if(experienceAdded < GamePanel.getScore())
			{
				ProfileManager.addExperience(1);
				experienceAdded++;
			}
		}

		this.repaint(); 
	}

	public void paintComponent(Graphics g)
	{	
		super.paintComponent(g);  // What does this even do?

		g.setFont(new Font("Lucida", Font.ITALIC, 22));

		g.setColor(Color.WHITE);

		g.drawString("Score: " + GamePanel.getScore(), 550, 80);
	}
}
