import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class MainMenu extends MainPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	protected static int buttonWidth = 240;
	protected static int buttonHeight = 60;
	protected static int fontSize = 28;

	private static BorderLayout mainLayout = new BorderLayout();

	private JPanel buttonPanel = new JPanel();
	private JPanel profilePanel = new JPanel();

	private GameButton controls;
	private GameButton onePlayer;
	private GameButton twoPlayers;
	private GameButton shipMenu;
	private GameButton quitGame;
	private GameButton addProfile;
	private GameButton removeProfile;

	private JComboBox profileChooser;

	public MainMenu()
	{	
	}

	public MainMenu(int width, int height)
	{
		setBackground(Color.BLACK);

		panelWidth = width;
		panelHeight = height;

		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setLayout(mainLayout);

		initializeButtonPanel();
		initializeProfilePanel();
	}

	public void initializeButtonPanel()
	{
		controls  = new GameButton("Controls", "menuButton.png", buttonWidth, buttonHeight);
		onePlayer  = new GameButton("One Player", "menuButton.png", buttonWidth, buttonHeight);
		twoPlayers = new GameButton("Two Players", "menuButton.png", buttonWidth, buttonHeight);
		shipMenu   = new GameButton("Upgrades", "menuButton.png", buttonWidth, buttonHeight);
		quitGame   = new GameButton("Quit Game", "menuButton.png", buttonWidth, buttonHeight);

		controls.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.setMultiplayer(false);
				Gravity.setState("tutorialMenu");
			}
		});
		
		onePlayer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.setMultiplayer(false);
				Gravity.setState("gameRunning");
			}
		});

		twoPlayers.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.setMultiplayer(true);
				Gravity.setState("gameRunning");
			}
		});

		shipMenu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Gravity.setState("upgradeMenu");
			}
		});

		quitGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ProfileManager.saveProfile();
				System.exit(0);
			}
		});

		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setOpaque(false);
		buttonPanel.setBackground(Color.BLACK);
		buttonPanel.add(Box.createVerticalStrut(panelHeight / 4));
		buttonPanel.add(onePlayer);
		buttonPanel.add(Box.createVerticalStrut(6));
		buttonPanel.add(twoPlayers);
		buttonPanel.add(Box.createVerticalStrut(6));
		buttonPanel.add(shipMenu);
		buttonPanel.add(Box.createVerticalStrut(6));
		buttonPanel.add(controls);
		buttonPanel.add(Box.createVerticalStrut(6));
		buttonPanel.add(quitGame);

		this.add(buttonPanel, BorderLayout.CENTER);
	}

	public void initializeProfilePanel()
	{
		initializeProfileChooser();

		JLabel profileLabel = new JLabel("Profile:   ");
		profileLabel.setFont(new Font("Lucida", Font.ITALIC, 20));
		profileLabel.setForeground(Color.WHITE);

		addProfile = new GameButton("", "plusButton.png", 25, 25);
		removeProfile = new GameButton("", "minusButton.png", 30, 12);

		addProfile.addActionListener(			
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						//Prompt the user for a name
						String name = (String)JOptionPane.showInputDialog(null, "Profile Name: ");

						if ((name != null) && (name.length() > 0)) 
						{
							
							//Make sure profile doesn't already exist
							for(String s : ProfileManager.getProfileNames())
							{
								if(name.equals(s))
								{
									JOptionPane.showMessageDialog(null,
										    "Profile \"" + name + "\" already exists", "",
										    JOptionPane.ERROR_MESSAGE);

									return;
								}
							}
							
							ProfileManager.createProfile(name);
							System.out.println("Current profile: " + ProfileManager.getCurrentProfile());
							repopulateProfileChooser();
						}
					}
				});

		removeProfile.addActionListener(			
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						//Ask the user for confirmation
						int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete "
								+ ProfileManager.getCurrentProfile().getName() + "?", "Confirm",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

						if (response == JOptionPane.YES_OPTION) 
						{
							ProfileManager.deleteCurrentProfile();

							repopulateProfileChooser();
						} 
					}
				});

		profilePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15 , 0));
		profilePanel.setBackground(Color.DARK_GRAY);
		profilePanel.add(profileLabel);
		profilePanel.add(profileChooser);
		profilePanel.add(addProfile);
		profilePanel.add(removeProfile);

		this.add(profilePanel, BorderLayout.NORTH);
	}

	public void initializeProfileChooser()
	{
		profileChooser = new JComboBox(ProfileManager.getProfileNames());
		profileChooser.setSelectedIndex(0);
		profileChooser.addActionListener(this);	
		profileChooser.setPreferredSize(new Dimension(200, 30));
		profileChooser.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");

		profileChooser.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						JComboBox cb = (JComboBox)e.getSource();
						String newProfile = (String)cb.getSelectedItem();
						ProfileManager.saveProfile();
						ProfileManager.loadProfile(newProfile);
					}
				});
	}

	public void repopulateProfileChooser()
	{
		System.out.println("Repopulate");
		
		profileChooser.removeAllItems();

		for(int i = 0; i < ProfileManager.getProfileNames().length; i++)
		{
			String name = ProfileManager.getProfileNames()[i];
			
			profileChooser.addItem(name);
			
			//TODO this doesn't seem to work
			if(name.equals(ProfileManager.getCurrentProfile()))
			{
				System.out.println("Found profile!");
				profileChooser.setSelectedIndex(i);
			}
			else
			{
				System.out.println(name + " != " + ProfileManager.getCurrentProfile());
			}
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g); 

		g.setFont(new Font("LucidaTypewriter", Font.ITALIC, 18));

		g.setColor(Color.WHITE);

		Profile currentProfile = ProfileManager.getCurrentProfile();

		g.drawString("High Score: " + currentProfile.getHighScore(), 580, 155);
		g.drawString("Experience: " + currentProfile.getExperience(), 580, 180);		
		
		/*
		g.drawString("Max Health: "   + currentProfile.getMaxHealth(), 900, 150);	
		g.drawString("Health Regen: " + currentProfile.getHealthRegen(), 900, 170);	
		g.drawString("Max Speed: "    + currentProfile.getMaxSpeed(), 900, 190);	
		g.drawString("Armor Value: "  + currentProfile.getArmorValue(), 900, 210);	
		*/
	}
}
