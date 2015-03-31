import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JLabel;
import javax.swing.JPanel;


public class TutorialMenu extends MainPanel
{
	private static final long serialVersionUID = 1L;

	private static double buttonWidth = 225;
	private static double buttonHeight = 50;

	private static JPanel topPanel;
	private static JPanel textPanel;
	
	private static JLabel p1Controls;
	private static JLabel p2Controls;
	
	private GameButton back;

	public TutorialMenu(int width, int height)
	{
		setBackground(Color.BLACK);

		panelWidth = width;
		panelHeight = height;

		setPreferredSize(new Dimension(panelWidth, panelHeight));

		setLayout(new BorderLayout());

		initializePanel();
		initializeLabels();
		initializeButtons();
	}
	
	public void initializePanel()
	{
		topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(100, 200));
		topPanel.setBackground(Color.BLACK);
		add(topPanel, BorderLayout.NORTH);
		
		textPanel = new JPanel();
		textPanel.setBackground(Color.BLACK);
	}

	public void initializeLabels()
	{
		String one = "<html>" +
				"Player 1: <br><br>" +
				"Forwards:  W <br> " +
				"Backwards:  S <br>" +
				"Rotate Left: A <br" +
				"Rotate Right: D <br>" +
				"Shoot: Spacebar <br>" +
				"</html>";

		p1Controls = new JLabel(one);
		p1Controls.setForeground(Color.WHITE);
		p1Controls.setFont(new Font("LucidaTypewriter", Font.ITALIC, 18));
		p1Controls.setAlignmentX(Component.CENTER_ALIGNMENT);

		String two = "<html>" +
				"Player 2: <br><br>" +
				"Forwards:  Up <br> " +
				"Backwards:  Down <br>" +
				"Rotate Left: Left <br" +
				"Rotate Right: Right <br>" +
				"Shoot: Shift <br>" +
				"</html>";

		p2Controls = new JLabel(two);
		p2Controls.setForeground(Color.WHITE);
		p2Controls.setFont(new Font("LucidaTypewriter", Font.ITALIC, 18));
		p2Controls.setAlignmentX(Component.CENTER_ALIGNMENT);

		textPanel.add(p1Controls);
		
		JPanel spacer = new JPanel();
		spacer.setPreferredSize(new Dimension(100, 100));
		spacer.setBackground(Color.BLACK);
		textPanel.add(spacer);
		
		textPanel.add(p2Controls);
		
		add(textPanel, BorderLayout.CENTER);
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
				Gravity.setState("mainMenu");
			}
		});

		//add(Box.createVerticalStrut((int)(panelHeight * 1.6)));
		
		add(back, BorderLayout.SOUTH);
	}
}
