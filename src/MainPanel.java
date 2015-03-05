
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;


public class MainPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	protected static int panelWidth;
	protected static int panelHeight;

	protected static Graphics g;
	
	protected static Timer timer;
	
	public MainPanel()
	{
		
	}
	
	public void stop()
	{
		timer.stop();
	}
	
	public void run()
	{	
		g = getGraphics();

		paint(g);

		timer = new Timer(20, this);
		timer.setInitialDelay(0);
		timer.start();
	}

	public void actionPerformed(ActionEvent e)
	{
		this.repaint(); 
	}
}
