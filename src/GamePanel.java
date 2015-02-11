import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

public class GamePanel extends MainPanel implements ActionListener, Runnable
{
	private static final long serialVersionUID = 1L;

	private static ArrayList<Entity> entities = new ArrayList<Entity>();

	private Controller controller;

	private static Ship player1Ship;
	private static Ship player2Ship;

	private static boolean asteroids;

	private static boolean isMultiplayer = true;

	private static int blueScore; //multiplayer only
	private static int redScore;
	private static int score;     //singleplayer only

	private static int turnsSinceEnemySpawn;

	private static boolean gameOver;

	public GamePanel(int x, int y)
	{	
		panelWidth = x;
		panelHeight = y;

		gameOver = false;

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

		Ship p1Ship = new Ship(100, panelHeight / 2);
		player1Ship = p1Ship;
		addShip(p1Ship);

		score = 0;

		if(isMultiplayer)
		{
			Ship p2Ship = new Player2Ship(1050, panelHeight / 2);
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

		addEntity(centerPlanet);
		addEntity(leftPlanet);
		addEntity(rightPlanet);
	}

	public static void generateRandomPlanets()
	{
		Random rand = new Random();

		/*
		Planet centerPlanet = new Planet((panelWidth / 2) - 50, (panelHeight / 2) - 50);

		centerPlanet.addMoon(120);

		addEntity(centerPlanet);
		centerPlanet.addRandomMoons();
		 */

		int numPlanets = 0;

		for(int r = 0; r < panelHeight; r+= panelHeight / 2)
		{
			for(int c = 0; c < panelWidth; c+= panelWidth / 2)
			{
				if(rand.nextBoolean())
				{
					Planet randomPlanet = new Planet((c + panelWidth / 4) - 50, (r + panelHeight / 4) - 50);

					addEntity(randomPlanet);
					randomPlanet.addRandomMoons();
					numPlanets++;
					if(numPlanets >= 2)
					{
						return;
					}
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

		if(!isMultiplayer && !gameOver)
		{
			spawnEnemies();
		}

		if(gameOver)
		{
			if(score > ProfileManager.getHighScore())
			{
				ProfileManager.setHighScore(score);
			}

			try
			{
				Thread.sleep(500);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			Gravity.setState("gameOverMenu");
		}

		this.repaint(); 
	}

	public static void spawnEnemies()
	{
		turnsSinceEnemySpawn++;

		int numEnemiesAlive = 0;
		int numCarriers = 0;

		Random rand = new Random();


		for(Entity e : entities)
		{
			if(e instanceof AIShip)
			{
				numEnemiesAlive++;

				if(e instanceof AICarrier)
				{
					numCarriers++;
				}
			}
		}

		if(turnsSinceEnemySpawn >= 30 && numEnemiesAlive <= score)
		{
			turnsSinceEnemySpawn = 0;

			int spawnPoint = rand.nextInt(4) + 1;

			if(spawnPoint == 1)
			{
				addShip(new AIShip(panelWidth, rand.nextInt(panelHeight)));
			}
			else if(spawnPoint == 2)
			{
				addShip(new AIShip(0, rand.nextInt(panelHeight)));
			}
			else if(spawnPoint == 3)
			{
				addShip(new AIShip(rand.nextInt(panelWidth), 0));
			}
			else if(spawnPoint == 4)
			{
				addShip(new AIShip(rand.nextInt(panelWidth), panelHeight));
			}
		}


		if(score != 0 && score % 10 == 0)
		{
			if(numCarriers <= score / 15)
			{
				addShip(new AICarrier(rand.nextInt(panelWidth), panelHeight));
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
			g.setFont(new Font("LucidaTypewriter", Font.ITALIC, 18));
			g.setColor(Color.BLUE);
			g.drawString("Blue: " + blueScore, 10, 20);
			g.setColor(Color.RED);
			g.drawString("Red: " + redScore, panelWidth - 70, 20);

			int p1Health = 0;

			if(player1Ship != null)
			{
				p1Health = (int)(player1Ship.getHealth());
			}

			g.setColor(Color.BLUE);
			g.fillRect(0, 30, (int)(p1Health * 1.5), 15);

			int p2Health = 0;

			if(player2Ship != null)
			{
				p2Health = (int)(player2Ship.getHealth());
			}

			g.setColor(Color.RED);
			g.fillRect((int)(panelWidth - p2Health * 1.5), 30, (int)(p2Health * 1.5), 15);
		}
		else
		{
			g.setColor(Color.WHITE);
			g.drawString("Score: " + score, 10, 10);

			int p1Health = 0;

			if(player1Ship != null)
			{
				p1Health = (int)(player1Ship.getHealth());
			}

			g.setColor(Color.RED);
			g.fillRect((panelWidth / 2) - (p1Health / 2), 5, p1Health, 10);
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
			else
			{
				gameOver = true;
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
			if(player1Ship != null)
			{
				if(s instanceof AICarrier)
				{
					score += 5;
				}
				else
				{
					score++;
				}
			}
		}

		entities.remove(s);
	}

	public static void setMultiplayer(boolean b)
	{
		isMultiplayer = b;

		if(isMultiplayer)
		{
			redScore = 0;
			blueScore = 0;
		}
		else
		{
			score = 0;
		}
	}

	public static boolean isMultiplayer()
	{
		return isMultiplayer;
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
	
	public static int getScore()
	{
		return score;
	}
}
