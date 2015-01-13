import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;


public class MainMenu extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private int panelWidth;
	private int panelHeight;

	private Graphics g;

	private static Timer timer;

	private JButton onePlayer = new JButton("One Player");
	private JButton twoPlayers = new JButton("Two Players");
	private JButton quitGame = new JButton("Quit Game");

	public MainMenu(int width, int height)
	{
		panelWidth = width;
		panelHeight = height;

		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		initializeButtons();
	}

	public void initializeButtons()
	{
		this.add(Box.createVerticalStrut(panelHeight / 9));

		onePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		twoPlayers.setAlignmentX(Component.CENTER_ALIGNMENT);
		quitGame.setAlignmentX(Component.CENTER_ALIGNMENT);

		onePlayer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.setMultiplayer(false);
				Gravitation.setState("gameRunning");
			}
		});
 
		twoPlayers.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.setMultiplayer(true);
				Gravitation.setState("gameRunning");
			}
		});

		quitGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});

		add(onePlayer);
		add(twoPlayers);
		add(quitGame);
	}

	public void run()
	{	
		g = getGraphics();

		setBackground(Color.LIGHT_GRAY);

		paint(g);

		timer = new Timer(20, this);
		timer.setInitialDelay(0);
		timer.start();
	}

	public void actionPerformed(ActionEvent a) 
	{   
		this.requestFocus();

		this.repaint(); 
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}

	public void stop()
	{
		timer.stop();
	}
}
