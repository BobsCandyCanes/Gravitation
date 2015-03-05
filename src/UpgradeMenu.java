import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class UpgradeMenu extends MainMenu
{
	private static final long serialVersionUID = 1L;

	protected static double buttonWidth = 225;
	protected static double buttonHeight = 50;
	protected static int fontSize = 28;
	
	private static BorderLayout mainLayout = new BorderLayout();
	
	private static JPanel topPanel;
	private static JPanel buttonPanel;
	private static JPanel gridPanel;
	
	private static JLabel experienceLabel;

	private static String previousMenu;
	
	private UpgradeBar[] upgradeBars = new UpgradeBar[4];
	
	private GameButton back;

	public UpgradeMenu(int width, int height, String previousState)
	{
		setBackground(Color.BLACK);

		panelWidth = width;
		panelHeight = height;

		setPreferredSize(new Dimension(panelWidth, panelHeight));
		
		previousMenu = previousState;
		
		setLayout(mainLayout);
		
		initializePanels();
		initializeUpgradeBars();
		initializeButtons();
	}
	
	public void initializePanels()
	{
		topPanel = new JPanel();
		buttonPanel = new JPanel();
		gridPanel = new JPanel();
		
		gridPanel.setLayout(new GridLayout(3, 2, 50, 100));
		gridPanel.setOpaque(false);
		gridPanel.setBackground(Color.MAGENTA);
		
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setOpaque(false);
		buttonPanel.setBackground(Color.MAGENTA);
		buttonPanel.add(Box.createVerticalStrut(panelHeight / 2));
		buttonPanel.add(gridPanel);
		add(buttonPanel, BorderLayout.CENTER);
		
		topPanel.setBackground(Color.DARK_GRAY);
		topPanel.setPreferredSize(new Dimension(10, 90));
		
		experienceLabel = new JLabel("Experience:  " + ProfileManager.getExperience());
		experienceLabel.setFont(new Font("Lucida", Font.ITALIC, 20));
		experienceLabel.setForeground(Color.WHITE);
		experienceLabel.setPreferredSize(new Dimension(250, 60));

		topPanel.add(experienceLabel);
		
		add(topPanel, BorderLayout.NORTH);
	}

	public void initializeButtons()
	{
		back = new GameButton("Back", "menuButton.png", (int)buttonWidth, (int)buttonHeight);

		back.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ProfileManager.saveProfile();
				GamePanel.setMultiplayer(false);
				Gravity.setState(previousMenu);
			}
		});

		buttonPanel.add(Box.createVerticalStrut(panelHeight / 3));
		buttonPanel.add(back);
	}
	
	public void initializeUpgradeBars()
	{	
		upgradeBars[0] = new UpgradeBar(ProfileManager.getAttributes()[0]);
		upgradeBars[1] = new UpgradeBar(ProfileManager.getAttributes()[1]);
		upgradeBars[2] = new UpgradeBar(ProfileManager.getAttributes()[2]);
		upgradeBars[3] = new UpgradeBar(ProfileManager.getAttributes()[3]);
		
		for(UpgradeBar ub : upgradeBars)
		{
			gridPanel.add(ub);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		this.requestFocus();

		this.repaint(); 
	}

	public void paintComponent(Graphics g)
	{	
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		
		g.fillRect(0, 0, panelWidth, panelHeight);
		
		for(UpgradeBar ub : upgradeBars)
		{
			ub.draw(g);
		}
	}
	
	public static void updateExperience()
	{
		experienceLabel.setText("Experience:  " + ProfileManager.getExperience());
	}
}
