import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class MainMenu extends MainPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	protected static double buttonWidth = 240;
	protected static double buttonHeight = 60;
	protected static int fontSize = 28;

	private static BorderLayout mainLayout = new BorderLayout();

	private JPanel buttonPanel = new JPanel();
	private JPanel profilePanel = new JPanel();

	private JButton onePlayer = new JButton("One Player");
	private JButton twoPlayers = new JButton("Two Players");
	private JButton shipMenu = new JButton("Ship");
	private JButton quitGame = new JButton("Quit Game");
	private JButton addProfile = new JButton();
	private JButton removeProfile = new JButton();

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
		onePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		twoPlayers.setAlignmentX(Component.CENTER_ALIGNMENT);
		shipMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		quitGame.setAlignmentX(Component.CENTER_ALIGNMENT);

		setButtonLook(onePlayer, "menuButton.png");
		setButtonLook(twoPlayers, "menuButton.png");
		setButtonLook(shipMenu, "menuButton.png");
		setButtonLook(quitGame, "menuButton.png");

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
				Gravity.setState("shipMenu");
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
		buttonPanel.add(quitGame);

		this.add(buttonPanel, BorderLayout.CENTER);
	}

	public void initializeProfilePanel()
	{
		initializeProfileChooser();

		JLabel profileLabel = new JLabel("Profile:   ");
		profileLabel.setFont(new Font("Lucida", Font.ITALIC, 20));
		profileLabel.setForeground(Color.WHITE);

		addProfile.setPreferredSize(new Dimension(25,25));
		removeProfile.setPreferredSize(new Dimension(25,25));
		
		setButtonLook(addProfile, "plusButton.png");
		setButtonLook(removeProfile, "minusButton.png");
		
		addProfile.addActionListener(			
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						String name = (String)JOptionPane.showInputDialog(null, "Profile Name: ");

						if ((name != null) && (name.length() > 0)) 
						{
							ProfileManager.createProfile(name);

							repopulateProfileChooser();
						}
					}
				});

		removeProfile.addActionListener(			
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
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
		profileChooser.setPreferredSize(new Dimension(200, 80));
		profileChooser.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXX");

		profileChooser.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e) {
						JComboBox cb = (JComboBox)e.getSource();
						String newProfile = (String)cb.getSelectedItem();
						ProfileManager.saveProfile();
						ProfileManager.loadProfile(newProfile);
					}
				});
	}

	public void repopulateProfileChooser()
	{
		profileChooser.removeAllItems();
		
		for(String s : ProfileManager.getProfileNames())
		{
			profileChooser.addItem(s);
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g); 

		g.setFont(new Font("LucidaTypewriter", Font.ITALIC, 18));

		g.setColor(Color.WHITE);

		Profile currentProfile = ProfileManager.getCurrentProfile();

		g.drawString("High Score: " + currentProfile.getHighScore(), 550, 155);
		g.drawString("Experience: " + currentProfile.getExperience(), 560, 180);		
	}

	public void setButtonLook(JButton button, String spritePath)
	{
		BufferedImage sprite = SpriteLibrary.getSprite(spritePath);

		if(spritePath.equals("plusButton.png"))
		{
			sprite = scaleSprite(sprite, 25, 25);
		}
		else if(spritePath.equals("minusButton.png"))
		{
			sprite = scaleSprite(sprite, 30, 10);
		}
		else
		{
			sprite = scaleSprite(sprite, 240, 60);
		}

		ImageIcon icon = new ImageIcon(sprite);

		button.setIcon(icon);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBackground(Color.WHITE);

		button.setFont(new Font("LucidaTypewriter", Font.ITALIC, fontSize));

		button.setHorizontalTextPosition(JButton.CENTER);
		button.setVerticalTextPosition(JButton.CENTER);

		button.setBorder(null);
	}

	public static BufferedImage scaleSprite(BufferedImage s, int width, int height)
	{
		int spriteWidth  = s.getWidth();
		int spriteHeight = s.getHeight();

		double scaleX = (double)width / spriteWidth;
		double scaleY = (double)height / spriteHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

		return s = bilinearScaleOp.filter(s, new BufferedImage(width, height, s.getType()));
	}
}
