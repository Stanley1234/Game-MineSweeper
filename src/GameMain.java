import frame.GridFrame;
import util.ImageLoader;
import util.SaveFile;

public class GameMain {
	public static void main(String[] args) {
		
		SaveFile.init();
		ImageLoader.init();
		new GridFrame();
		
		
	}
}
