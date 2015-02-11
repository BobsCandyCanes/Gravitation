import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ShipMenu extends MainMenu
{
	private static final long serialVersionUID = 1L;

	protected static double buttonWidth = 225;
	protected static double buttonHeight = 50;
	protected static int fontSize = 28;
	
	private static BorderLayout mainLayout = new BorderLayout();
	
	private static JPanel topPanel;
	private static JPanel buttonPanel;

	private static String previousMenu;
	
	private JButton back = new JButton("Back");

	public ShipMenu(int width, int height, String previousState)
	{
		setBackground(Color.BLACK);

		panelWidth = width;
		panelHeight = height;

		setPreferredSize(new Dimension(panelWidth, panelHeight));
		
		previousMenu = previousState;
		
		setLayout(mainLayout);
		
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
		topPanel.setPreferredSize(new Dimension(10, 90));
		
		JLabel experienceLabel = new JLabel("Experience:  " + ProfileManager.getExperience());
		experienceLabel.setFont(new Font("Lucida", Font.ITALIC, 20));
		experienceLabel.setForeground(Color.WHITE);
		experienceLabel.setPreferredSize(new Dimension(250, 60));

		topPanel.add(experienceLabel);
		
		add(topPanel, BorderLayout.NORTH);
	}

	public void initializeButtons()
	{
		back.setAlignmentX(Component.CENTER_ALIGNMENT);

		setButtonLook(back, "menuButton.png");

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
		buttonPanel.add(Box.createVerticalStrut(6));
	}

	public void actionPerformed(ActionEvent e)
	{
		this.requestFocus();

		this.repaint(); 
	}

	public void paintComponent(Graphics g)
	{	
		super.paintComponent(g);  // What does this even do?

		g.setFont(new Font("Lucida", Font.ITALIC, 22));

		g.setColor(Color.WHITE);

		g.drawString("Experience: " + ProfileManager.getExperience(), 550, 80);
	}
}
