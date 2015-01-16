import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, Runnable
{
	private static final long serialVersionUID = 1L;

	private Graphics g;

	private static int panelWidth;
	private static int panelHeight;

	private static Timer timer;

	private static ArrayList<Entity> entities = new ArrayList<Entity>();

	private Controller controller;

	private static Ship player1Ship;
	private static Ship player2Ship;

	private static boolean asteroids;

	private static boolean isMultiplayer = true;

	private static int blueScore; //multiplayer only
	private static int redScore;
	private static int score; //singleplayer only

	public GamePanel(int x, int y)
	{	
		panelWidth = x;
		panelHeight = y;

		setPreferredSize(new Dimension(panelWidth, panelHeight));

		setFocusable(true);

		controller = new Controller();

		addKeyListener(controller);
	}

	public void run()
	{	
		g = getGraphics();

		setBackground(Color.BLACK);

		resetWorld();

		paint(g);

		timer = new Timer(20, this);
		timer.setInitialDelay(0);

		timer.start();
	}

	public static void resetWorld()
	{
		entities = new ArrayList<Entity>();

		generateRandomPlanets();

		Ship p1Ship = new Ship(200, panelHeight / 2);
		player1Ship = p1Ship;
		addShip(p1Ship);

		score = 0;

		if(isMultiplayer)
		{
			Ship p2Ship = new Player2Ship(950, panelHeight / 2);
			player2Ship = p2Ship;
			addShip(p2Ship);
		}
		else
		{
			spawnEnemies();
		}
	}

	public static void generateFixedPlanets()
	{
		Planet centerPlanet = new Planet(600, panelHeight / 2 - 40);
		centerPlanet.addMoon(80);
		centerPlanet.addMoon(120);

		centerPlanet.addSpaceStation(100);

		Planet leftPlanet = new Planet(75, panelHeight / 2 - 40);
		leftPlanet.addMoon(80);

		Planet rightPlanet = new Planet(1025, panelHeight / 2 - 40);
		rightPlanet.addMoon(80);

		addPlanet(centerPlanet);
		addPlanet(leftPlanet);
		addPlanet(rightPlanet);
	}

	public static void generateRandomPlanets()
	{
		Random rand = new Random();

		for(int r = 0; r < panelHeight; r+= panelHeight / 2)
		{
			for(int c = 0; c < panelWidth; c+= panelWidth / 3)
			{
				if(rand.nextBoolean())
				{
					Planet randomPlanet = new Planet(c + panelWidth / 6, r + panelHeight / 4);

					int numMoons = rand.nextInt(3) + 1;

					for(int i = 0; i < numMoons; i++)
					{
						randomPlanet.addMoon((40 * (i + 1)) + 40);
					}

					randomPlanet.addSpaceStation(100);

					addPlanet(randomPlanet);
				}
			}
		}
	}

	public void actionPerformed(ActionEvent a) 
	{   
		this.requestFocus();

		controller.update();

		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);

			e.act();
		}

		if(asteroids)
		{
			generateAsteroids();
			generateAsteroids();
			generateAsteroids();
		}

		if(!isMultiplayer)
		{
			spawnEnemies();
		}

		this.repaint(); 
	}

	public static void spawnEnemies()
	{
		int numEnemiesAlive = 0;

		for(Entity e : entities)
		{
			if(e instanceof AIShip)
			{
				numEnemiesAlive++;
			}
		}

		if(numEnemiesAlive == 0)
		{
			Random rand = new Random();

			for(int i = 0; i < score + 1; i++)
			{
				addShip(new AIShip(panelWidth - 40, rand.nextInt(panelHeight)));
			}
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);

			e.draw(g);
		}

		drawHUD(g);
	}

	public void drawHUD(Graphics g)
	{
		if(isMultiplayer)
		{
			g.setColor(Color.BLUE);
			g.drawString("Blue: " + blueScore, 10, 10);
			g.setColor(Color.RED);
			g.drawString("Red: " + redScore, panelWidth - 55, 10);
		}
		else
		{
			g.setColor(Color.WHITE);
			g.drawString("Score: " + score, 10, 10);
		}
	}

	public void generateAsteroids()
	{
		int xPosition = (int)(Math.random() * panelWidth);
		int yPosition = 0;

		double xVelocity = (int)(Math.random() * 25);

		if(xVelocity % 2 == 0)
		{
			xVelocity *= -1;
		}

		double yVelocity = (int)(Math.random() * 25);

		addEntity(new Asteroid(xPosition, yPosition, xVelocity, yVelocity, (int) (Math.random() * 20)));
	}

	public static void addEntity(Entity e)
	{
		entities.add(e);
	}

	public static void removeEntity(Entity e)
	{
		entities.remove(e);
	}

	public static void addShip(Ship s)
	{
		entities.add(s);
	}

	public static void removeShip(Ship s)
	{
		if(s.equals(player1Ship))
		{
			if(isMultiplayer)
			{
				redScore++;
			}
			player1Ship = null;
		}
		else if(s.equals(player2Ship))
		{
			if(isMultiplayer)
			{
				blueScore++;
			}
			player2Ship = null;
		}
		else if(s instanceof AIShip)
		{
			score++;
		}

		entities.remove(s);
	}

	public void stop()
	{
		timer.stop();
	}

	public static void toggleAsteroids()
	{
		if(asteroids)
		{
			asteroids = false;
		}
		else
		{
			asteroids = true;
		}
	}

	public static void addPlanet(Planet p)
	{
		entities.add(p);
	}

	public static ArrayList<Entity> getEntities()
	{
		return entities;
	}

	public static Ship getPlayer1Ship()
	{
		return player1Ship;
	}

	public static Ship getPlayer2Ship()
	{
		return player2Ship;
	}

	public static int getPanelWidth()
	{
		return panelWidth;
	}

	public static int getPanelHeight()
	{
		return panelHeight;
	}

	public static void setMultiplayer(boolean b)
	{
		isMultiplayer = b;
		if(isMultiplayer)
		{
			redScore = 0;
			blueScore = 0;
		}
	}

	public static boolean isMultiplayer()
	{
		return isMultiplayer;
	}
}
