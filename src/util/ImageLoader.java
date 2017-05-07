package util;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	
	
	public static Image FLAG;
	public static Image MINE;
	public static Image DisabledButton;
	public static Image BACKGROUND;
	
	private ImageLoader() {}
	
	public static void init() {
		try {
			FLAG = ImageIO.read(ImageLoader.class.getClassLoader().getResource("flag.png"));
			DisabledButton = ImageIO.read(ImageLoader.class.getClassLoader().getResource("disabled_button.png"));
			BACKGROUND = ImageIO.read(ImageLoader.class.getClassLoader().getResource("BG.jpg"));
			MINE = ImageIO.read(ImageLoader.class.getClassLoader().getResource("mine.png"));
		} catch (IOException e) {}
	}
	
}
