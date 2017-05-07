
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class GameButton extends JButton {

	private int x, y;
	private GameFrame gFrame;

	public GameButton(final int x, final int y, GameFrame gFrame) {
		super();
		this.x = x;
		this.y = y;
		this.gFrame = gFrame;

	
		setFont(new Font("Serif", Font.BOLD, 20));
		
		setBackground(new Color(122, 154, 250));  // light blue
		setForeground(Color.RED);
		
		// the text area
		setMargin(new Insets(1, 1, 1, 1));
		
		this.addMouseListener(gFrame);
		this.setContentAreaFilled(false);
	}

	@Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(getBackground());
        } else if (getModel().isRollover()) {
           g.setColor(getBackground());
        } else {
            g.setColor(getBackground());
        }
        
        
        
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

	@Override
	public String toString() {
		return "[GButton] " + x + "," + y;
	}
	
	public void setTextByCount(final int count) {
		if(count == 0)
			return;
		
		System.out.println(count);
		Color color = null;
		if(count == 1)  // medium blue
			color = new Color(57, 71, 241);
		else if(count == 2) // green
			color = new Color(57, 198, 69);
		else if(count == 3)  // dark red
			color = new Color(193, 72, 69);
		else if(count == 4)  // dark blue
			color = new Color(57, 72, 127);
		else if(count >= 5)  // purple
			color = new Color(101, 28, 127);
		
		//.getDefaults().put("Button.disabledText", Color.RED); 
		setForeground(color);
		setText(count + "");
	}
}
