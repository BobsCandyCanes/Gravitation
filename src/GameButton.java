import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;


public class GameButton extends JButton
{
	private static final long serialVersionUID = 1L;
	
	private String spritePath;
	private int width;
	private int height;
	
	private int fontSize = 28;
	
	public GameButton(String label, String s, int w, int h)
	{
		setText(label);
		spritePath = s;
		width = w;
		height = h;
		
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setPreferredSize(new Dimension(w, h));
		setButtonLookAndFeel();
	}
	
	public void setButtonLookAndFeel()
	{
		BufferedImage sprite = SpriteLibrary.getSprite(spritePath);

		sprite = scaleSprite(sprite, width, height);

		ImageIcon icon = new ImageIcon(sprite);

		setIcon(icon);
		setMargin(new Insets(0, 0, 0, 0));
		setBackground(new Color(0, 0, 0, 255));

		setFont(new Font("LucidaTypewriter", Font.ITALIC, fontSize));

		setHorizontalTextPosition(JButton.CENTER);
		setVerticalTextPosition(JButton.CENTER);

		setBorder(null);
	}

	public static BufferedImage scaleSprite(BufferedImage s, int width, int height)
	{
		int spriteWidth  = s.getWidth();
		int spriteHeight = s.getHeight();

		double scaleX = (double)width / spriteWidth;
		double scaleY = (double)height / spriteHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

		return s = bilinearScaleOp.filter(s, new BufferedImage(width, height, s.getType()));
	}
}
