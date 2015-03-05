import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JPanel;

public class UpgradeBar extends JPanel
{
	private static final long serialVersionUID = 1L;

	private static final int UPGRADE_COST = 100;
	
	private GameButton minusButton;
	private GameButton plusButton;

	private double max;
	private double min = 0;
	
	private double width = 200;

	private double value;
	private double increment;
	
	private static DecimalFormat formatter = new DecimalFormat("#.##");
	
	private ActionListener buttonAction;

	private Attribute attribute;
	
	public UpgradeBar(Attribute a)
	{
		attribute = a;
		
		this.value = a.getValue();
		this.min = a.getMin();
		this.max = a.getMax();
		this.increment = a.getIncrement();
		
		setSize(500, 50);
		setOpaque(false);
		setBackground(Color.BLACK);
		setLayout(new FlowLayout(FlowLayout.CENTER, 250, 0));
		
		initActionListener();
		initButtons();
	}

	public void initActionListener()
	{
		buttonAction = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(e.getSource().equals(minusButton))
				{
					if(value > min)
					{
						value -= increment;
						
						if(value < min)
						{
							value = min;
						}
						
						ProfileManager.addExperience(UPGRADE_COST);
						UpgradeMenu.updateExperience();
					}
				}
				else if(e.getSource().equals(plusButton))
				{
					if(value < max && ProfileManager.getExperience() >= UPGRADE_COST)
					{
						value += increment;
						
						if(value > max)
						{
							value = max;
						}
						
						ProfileManager.addExperience( - UPGRADE_COST);
						UpgradeMenu.updateExperience();
					}
				}
				updateValue();
			}
		};
	}
	
	public void initButtons()
	{
		minusButton = new GameButton("", "minusButton.png", 30, 12);
		plusButton = new GameButton("", "plusButton.png", 25, 25);

		minusButton.addActionListener(buttonAction);
		plusButton.addActionListener(buttonAction);

		add(minusButton);
		add(plusButton);
	}
	
	public void updateValue()
	{
		attribute.setValue(value);
		ProfileManager.updateAttributes();
	}
	
	public void draw(Graphics g)
	{
		int height = 16;
		
		int xPos = getX() + (int)(width / 1.5);
		int yPos = getY() + getHeight() * 6;
		
		g.setColor(Color.RED.darker().darker());
		g.fillRect(xPos, yPos, (int)(max * (width / max)), height);
		
		g.setColor(Color.CYAN.darker());
		g.fillRect(xPos, yPos, (int)(width * (value / max)), height);
		
		g.setColor(Color.CYAN);
		g.drawRect(xPos, yPos, (int)(width * (value / max)), height);

		g.setColor(Color.GREEN);
		g.drawString(attribute.getName() + ":   " + formatter.format(value) + "/" + formatter.format(max), xPos, yPos - 2);
	}
}
