import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.SwingWorker;
import javax.swing.Timer;

public class GamePanel extends MainPanel implements ActionListener, Runnable
{
	private static final int LOGIC_FRAMERATE = 8;
	private static final int DRAW_FRAMERATE = 8;

	private static final int WORLD_WIDTH = 3000;
	private static final int WORLD_HEIGHT = 3000;

	private static final long serialVersionUID = 1L;

	private static Timer drawTimer;

	private static List<Entity> entities = new ArrayList<Entity>();

	//EngineTrails are stored in a separate list to improve performance
	private static ArrayList<EngineTrail> engineTrails = new ArrayList<EngineTrail>();

	private static Ship player1Ship;
	private static Ship player2Ship;

	private static boolean asteroids;
	private static boolean gameOver;
	private static boolean isMultiplayer = true;

	private static int greenScore; //multiplayer only
	private static int redScore;
	private static int score;     //singleplayer only

	private static int numShips;
	private static int turnsSinceEnemySpawn;
	private static int scoreAtLastGunship;
	private static int enemySpawnDelay = 10;

	private int middle;
	private int healthBarWidth = 90;
	private int scoreWidth = 120;

	private static int backgroundX = 0;
	private static int backgroundY = 0;

	private static int cameraX = 0;
	private static int cameraY = 0;

	private static Rectangle cameraBounds = new Rectangle(cameraX, cameraY, panelWidth, panelHeight);
	private static Rectangle worldBounds = new Rectangle(0, 0, WORLD_WIDTH, WORLD_HEIGHT);

	private static Rectangle topYBuffer;
	private static Rectangle bottomYBuffer;
	private static Rectangle leftXBuffer;
	private static Rectangle rightXBuffer;

	private static Rectangle xWrap = new Rectangle(-2, -2, 1, 1);
	private static Rectangle yWrap = new Rectangle(-2, -2, 1, 1);

	private static Rectangle xWrapDraw = new Rectangle(-2, -2, 1, 1);
	private static Rectangle yWrapDraw = new Rectangle(-2, -2, 1, 1);

	private Controller controller;

	private BufferedImage background;

	public GamePanel(int x, int y)
	{			
		panelWidth = x;
		panelHeight = y;

		middle = panelWidth / 2;

		topYBuffer    = new Rectangle(-panelWidth / 2, -panelHeight / 2, panelWidth + WORLD_WIDTH, panelHeight / 2);
		bottomYBuffer = new Rectangle(-panelWidth / 2, WORLD_HEIGHT, panelWidth + WORLD_WIDTH, panelHeight / 2);
		leftXBuffer   = new Rectangle(-panelWidth / 2, -panelHeight / 2, panelWidth / 2, panelHeight + WORLD_HEIGHT);
		rightXBuffer  = new Rectangle(WORLD_WIDTH, -panelHeight / 2, panelWidth / 2, panelHeight + WORLD_HEIGHT);

		asteroids = false;
		gameOver = false;

		setPreferredSize(new Dimension(panelWidth, panelHeight));

		setFocusable(true);

		controller = new Controller();

		addKeyListener(controller);

		background = SpriteLibrary.getSprite("background.png");
		background = SpriteLibrary.scaleSprite(background, panelWidth, panelHeight);

		// Improves performance, but causes flickering
		setDoubleBuffered(false);
	}

	public void run()
	{	
		g = getGraphics();

		setBackground(Color.BLACK);

		resetWorld();

		paint(g);

		timer = new Timer(LOGIC_FRAMERATE, new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				final SwingWorker<Void, Void> mainWorker = new SwingWorker<Void, Void>() 
						{
					protected Void doInBackground() throws Exception
					{
						controller.update();

						for(int i = 0; i < entities.size(); i++)
						{
							entities.get(i).act();
						}

						for(int i = 0; i < engineTrails.size(); i++)
						{
							engineTrails.get(i).act();
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

						updateCameraBounds();

						return null;
					}
						};			
						mainWorker.execute(); 


						//Moved this out of the swingworker due to weird concurrency issues
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

							Gravity.setState("gameOver");
						}
			}
		});
		timer.setInitialDelay(0);
		timer.start();

		drawTimer = new Timer(DRAW_FRAMERATE, this);
		drawTimer.setInitialDelay(0);
		drawTimer.start();
	}

	public static void resetWorld()
	{
		entities = new ArrayList<Entity>();
		engineTrails = new ArrayList<EngineTrail>();

		score = 0;
		numShips = 0;

		Ship p1Ship = new Ship(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
		player1Ship = p1Ship;
		addEntity(p1Ship);

		if(isMultiplayer)
		{
			Ship p2Ship = new Player2Ship(WORLD_WIDTH / 2 + 200, WORLD_HEIGHT / 2);
			player2Ship = p2Ship;
			addEntity(p2Ship);
		}
		else
		{
			spawnEnemies();
		}

		generateRandomPlanets();
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

		int numPlanets = 0;

		for(int i = 0; i < 30 && numPlanets < 6; i++)
		{
			boolean validLocation = true;

			//Generate random position
			int xPos = 300 + rand.nextInt(WORLD_WIDTH - 600);
			int yPos = 300 + rand.nextInt(WORLD_HEIGHT - 600);

			Planet randomPlanet = new Planet(xPos, yPos);

			//Make sure it doesn't spawn a planet on top of the player
			if(randomPlanet.getDistanceFrom(player1Ship) < 200)
			{
				validLocation = false;
			}
			else if(isMultiplayer)
			{
				if(randomPlanet.getDistanceFrom(player2Ship) < 200)
				{
					validLocation = false;
				}
			}

			//Make sure it's not too close to other planets
			for(Entity e : entities)	
			{
				if(e instanceof Planet)
				{
					Planet p  = (Planet) e;

					if(randomPlanet.getDistanceFrom(p) < Planet.getDefaultSize() * 7)
					{
						validLocation = false;
						break;
					}				
				}
			}

			if(validLocation)
			{
				addEntity(randomPlanet);
				randomPlanet.addRandomMoons();
				numPlanets++;
			}

		}

		//Make sure at least one planet spawns
		if(numPlanets == 0)
		{
			int xPos = WORLD_WIDTH / 4 - Planet.getDefaultSize() / 2;
			int yPos = WORLD_HEIGHT / 4 - Planet.getDefaultSize() / 2;

			Planet centerPlanet = new Planet(xPos, yPos);
			centerPlanet.addRandomMoons();
			addEntity(centerPlanet);
		}
	}

	public void actionPerformed(ActionEvent a) 
	{   
		this.requestFocus();

		repaint();
	}

	public static void updateCameraBounds()
	{
		if(player1Ship != null)
		{
			if(player2Ship != null)
			{
				int p1X = (int)(player1Ship.getXPosition());
				int p1Y = (int)(player1Ship.getYPosition());
				int p2X = (int)(player2Ship.getXPosition());
				int p2Y = (int)(player2Ship.getYPosition());
				
				cameraX = (int)((p1X + p2X) / 2 - panelWidth / 2); 
				cameraY = (int)((p1Y + p2Y) / 2 - panelHeight / 2); 
				
				cameraBounds = new Rectangle(cameraX, cameraY, panelWidth, panelHeight);
			}
			else
			{
				cameraX = (int)(player1Ship.getXPosition() - panelWidth / 2);
				cameraY = (int)(player1Ship.getYPosition() - panelHeight / 2);

				cameraBounds = new Rectangle(cameraX, cameraY, panelWidth, panelHeight);
			}
		}
	}

	public static void spawnEnemies()
	{
		turnsSinceEnemySpawn++;

		Random rand = new Random();

		if(numShips - 1 <= score && turnsSinceEnemySpawn >= enemySpawnDelay)
		{
			turnsSinceEnemySpawn = 0;

			int spawnPoint = rand.nextInt(4) + 1;

			if(spawnPoint == 1)
			{
				addEntity(new AIShip(cameraX + panelWidth, cameraY + rand.nextInt(panelHeight)));
			}
			else if(spawnPoint == 2)
			{
				addEntity(new AIShip(cameraX, cameraY + rand.nextInt(panelHeight)));
			}
			else if(spawnPoint == 3)
			{
				addEntity(new AIShip(cameraX + rand.nextInt(panelWidth), cameraY));
			}
			else if(spawnPoint == 4)
			{
				addEntity(new AIShip(cameraX + rand.nextInt(panelWidth), cameraY + panelHeight));
			}
		}

		if(score != 0 && score % 15 == 0)
		{
			if(score != scoreAtLastGunship)
			{
				scoreAtLastGunship = score;
				addEntity(new AIGunship(rand.nextInt(panelWidth), panelHeight));
			}
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D)g;

		drawBackground(g2d);

		g2d.translate(-cameraX, -cameraY);

		calcCameraWrap(g2d);

		for(int i = 0; i < engineTrails.size(); i++)
		{
			EngineTrail et = engineTrails.get(i);

			//CameraBounds is instantiated in a separate thread, so sometimes this causes concurrency issues
			if(cameraBounds.intersects(et.getBounds()))
			{
				et.draw(g2d);
			}

			if(xWrap.intersects(et.getBounds()))
			{
				int dx = (int)(Math.signum(cameraX - xWrap.x) * WORLD_WIDTH);

				g2d.translate(dx, 0);
				et.draw(g2d);
				g2d.translate(-dx, 0);
			}
			if(yWrap.intersects(et.getBounds()))
			{
				int dy = (int)(Math.signum(cameraY - yWrap.y) * WORLD_HEIGHT);

				g2d.translate(0, dy);
				et.draw(g2d);
				g2d.translate(0, -dy);
			}
		}

		// Entities is being modified by the act thread
		// Iterating through it here would cause concurrency issues
		// So, we make a shallow copy of entities to use temporarily
		List<Entity> entitiesToDraw = new ArrayList<Entity>(entities);

		for(int i = 0; i < entitiesToDraw.size(); i++)
		{
			Entity e = entitiesToDraw.get(i);

			//CameraBounds is instantiated in a separate thread, so sometimes this causes concurrency issues
			if(cameraBounds.intersects(e.getBounds()))
			{
				e.draw(g2d);
			}

			if(xWrap.intersects(e.getBounds()))
			{
				int dx = (int)(Math.signum(cameraX - xWrap.x) * WORLD_WIDTH);

				g2d.translate(dx, 0);
				e.draw(g2d);
				g2d.translate(-dx, 0);
			}
			if(yWrap.intersects(e.getBounds()))
			{
				int dy = (int)(Math.signum(cameraY - yWrap.y) * WORLD_HEIGHT);

				g2d.translate(0, dy);
				e.draw(g2d);
				g2d.translate(0, -dy);
			}
		}

		/*
		g2d.setColor(Color.GREEN);
		g2d.drawRect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
		*/

		g2d.translate(cameraX, cameraY);
		
		drawHUD(g2d);
	}

	public void drawBackground(Graphics2D g2d)
	{
		/*
		if(player1Ship != null)
		{
			backgroundX -= player1Ship.getXVelocity() / 6;
			backgroundY -= player1Ship.getYVelocity() / 6;
		}
		 */

		BufferedImage backgroundSub = background.getSubimage(0, 0, panelWidth, panelHeight);

		g2d.drawImage(backgroundSub, backgroundX, backgroundY, null);	
	}

	public void calcCameraWrap(Graphics2D g2d)
	{	
		if(!worldBounds.contains(cameraBounds))
		{
			if(cameraBounds.intersects(leftXBuffer))
			{
				xWrapDraw = cameraBounds.intersection(leftXBuffer);
				xWrap = xWrapDraw;
				xWrap.translate(WORLD_WIDTH, 0);
			}
			else if(cameraBounds.intersects(rightXBuffer))
			{
				xWrapDraw = cameraBounds.intersection(rightXBuffer);
				xWrap = xWrapDraw;
				xWrap.translate(-WORLD_WIDTH, 0);
			}

			if(cameraBounds.intersects(topYBuffer))
			{
				yWrapDraw = cameraBounds.intersection(topYBuffer);
				yWrap = yWrapDraw;
				yWrap.translate(0, WORLD_HEIGHT);
			}
			else if(cameraBounds.intersects(bottomYBuffer))
			{
				yWrapDraw = cameraBounds.intersection(bottomYBuffer);
				yWrap = yWrapDraw;
				yWrap.translate(0, -WORLD_HEIGHT);
			}
		}
	}

	public void drawHUD(Graphics g)
	{
		if(isMultiplayer)
		{
			g.setFont(new Font("LucidaTypewriter", Font.ITALIC, 18));
			g.setColor(Color.GREEN);
			g.drawString("Green: " + greenScore, 10, 20);
			g.setColor(Color.RED);
			g.drawString("Red: " + redScore, panelWidth - 70, 20);

			int p1Health = 0;

			if(player1Ship != null)
			{
				p1Health = (int)(player1Ship.getHealth());
			}
			else
			{
				g.setColor(Color.WHITE);
				g.drawString("Press Enter to reset", panelWidth / 2 - 50, panelHeight / 2);
			}

			g.setColor(Color.GREEN);
			g.fillRect(0, 30, (int)(p1Health * 1.5), 15);

			int p2Health = 0;

			if(player2Ship != null)
			{
				p2Health = (int)(player2Ship.getHealth());
			}
			else
			{
				g.setColor(Color.WHITE);
				g.drawString("Press Enter to reset", panelWidth / 2 - 50, panelHeight / 2);
			}

			g.setColor(Color.RED);
			g.fillRect((int)(panelWidth - p2Health * 1.5), 30, (int)(p2Health * 1.5), 15);
		}
		else
		{
			int[] scoreXVals = {0, scoreWidth, scoreWidth - 10, 0};
			int[] scoreYVals = {0, 0, 20, 20};

			g.setColor(Color.GRAY);
			g.fillPolygon(scoreXVals, scoreYVals, 4);

			g.setColor(Color.LIGHT_GRAY);
			g.drawPolygon(scoreXVals, scoreYVals, 4);

			g.setColor(Color.WHITE);
			g.drawString("Score: " + score, 8, 13);

			int[] healthXVals = {middle - healthBarWidth, middle + healthBarWidth, middle + healthBarWidth - 10, middle - healthBarWidth + 10};
			int[] healthYVals = {0, 0, 20, 20};

			g.setColor(Color.GRAY);
			g.fillPolygon(healthXVals, healthYVals, 4);

			g.setColor(Color.LIGHT_GRAY);
			g.drawPolygon(healthXVals, healthYVals, 4);

			int p1Health = 0;

			if(player1Ship != null)
			{
				p1Health = (int)(player1Ship.getHealth());
			}

			g.setColor(Color.RED);
			g.fillRect((panelWidth / 2) - (p1Health / 2), 5, p1Health, 10);

			g.setColor(Color.RED.darker());
			g.drawRect((panelWidth / 2) - (p1Health / 2), 5, p1Health, 10);
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
		if(e instanceof Ship)
		{
			numShips++;
		}

		entities.add(e);
	}

	public static void removeEntity(Entity e)
	{
		if(e instanceof Ship)
		{
			numShips--;

			if(e.equals(player1Ship))
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
			else if(e.equals(player2Ship))
			{
				if(isMultiplayer)
				{
					greenScore++;
				}
				player2Ship = null;
			}
		}

		entities.remove(e);
	}

	public static void setMultiplayer(boolean b)
	{
		isMultiplayer = b;

		if(isMultiplayer)
		{
			redScore = 0;
			greenScore = 0;
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

	public static List<Entity> getEntities()
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

	public static int getWorldWidth()
	{
		return WORLD_WIDTH;
	}

	public static int getWorldHeight()
	{
		return WORLD_HEIGHT;
	}

	public static int getScore()
	{
		return score;
	}

	public static void addScore(int s)
	{
		if(player1Ship != null)
		{
			score += s;
		}
	}

	public static Rectangle getCameraBounds()
	{
		return cameraBounds;
	}

	public static void addEngineTrail(EngineTrail et)
	{
		engineTrails.add(et);
	}

	public static void removeEngineTrail(EngineTrail et)
	{
		engineTrails.remove(et);
	}
}
