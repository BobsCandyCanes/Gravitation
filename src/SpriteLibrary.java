import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public final class SpriteLibrary
{
	
	private static ArrayList<String> spritePaths = new ArrayList<String>();
	
	private static BufferedImage[] sprites;

	public static void importAllSprites()
	{
		File[] files = new File("Images/").listFiles();
		
		for (File file : files) 
		{
		    if (file.isFile()) 
		    {
		        spritePaths.add(file.getName());
		    }
		    else
		    {
		    	System.out.println("Error loading sprites!");
		    }
		}
		
		sprites = new BufferedImage[spritePaths.size()];
		
		for(int i = 0; i < spritePaths.size(); i++)
		{
			String path = spritePaths.get(i);

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

		for(int i = 0; i < spritePaths.size(); i++)
		{
			if(spritePaths.get(i).equals(name))
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
	
	public static BufferedImage scaleSprite(BufferedImage s, double w, double h)
	{
		int spriteWidth  = s.getWidth();
		int spriteHeight = s.getHeight();

		double scaleX = (double)w / spriteWidth;
		double scaleY = (double)h / spriteHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

		s = bilinearScaleOp.filter(s, new BufferedImage((int)w, (int)h, s.getType()));
		
		return s;
	}
}
