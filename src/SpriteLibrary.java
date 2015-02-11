import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public final class SpriteLibrary
{
	private static String[] spritePaths = {
		"ship.png",
		"enemyShip.png",
		"moon.png",
		"planet1.png",
		"menuButton.png",
		"planet2.png",
		"plusButton.png",
		"minusButton.png"
	};

	private static BufferedImage[] sprites = new BufferedImage[spritePaths.length];

	public static void importAllSprites()
	{
		for(int i = 0; i < spritePaths.length; i++)
		{
			String path = spritePaths[i];

			try 
			{
				sprites[i] = ImageIO.read(new File("Images/" + path));  //import the sprite

			}
			catch (IOException e) 
			{
				System.out.println("Error loading sprite: " + path);
				e.printStackTrace();
			}
		}
	}

	public static BufferedImage getSprite(String name)
	{
		int index = -1;

		for(int i = 0; i < spritePaths.length; i++)
		{
			if(spritePaths[i].equals(name))
			{
				index = i;
				break;
			}
		}

		if(index != -1)
		{			
			return sprites[index];
		}
		else
		{
			System.out.println("Sprite not found: " + name);
			return sprites[0];
		}
	}
}
