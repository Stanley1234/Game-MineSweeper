package frame;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.security.KeyStore.PrivateKeyEntry;
import java.time.chrono.MinguoDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import util.Constant;
import util.SaveFile;

@SuppressWarnings("serial")
public class GameOptionDia extends JDialog implements ActionListener, ItemListener {
	private GridFrame gFrame;
	
	private Checkbox easyCheckBox;
	private Checkbox normalCheckBox;
	private Checkbox lunaticCheckBox;
	private Checkbox customizeCheckBox;
	private CheckboxGroup cbg;
	
	private TextField widthArea;
	private TextField heightArea;
	private TextField mineArea;

	public GameOptionDia(GridFrame gFrame) {
		this.gFrame = gFrame;

		setTitle("Statistics");
		setLocationRelativeTo(gFrame);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		initComponents();

		setModal(true);
		setResizable(false);
		pack();
		setVisible(true);

	}

	private void initComponents() {
		// check box
		cbg = new CheckboxGroup();
		easyCheckBox = new Checkbox("Easy", cbg, GridFrame.EASY == GridFrame.mode);
		normalCheckBox = new Checkbox("Normal", cbg, GridFrame.NORMAL == GridFrame.mode);
		lunaticCheckBox = new Checkbox("Lunatic", cbg, GridFrame.LUNATIC == GridFrame.mode);
		customizeCheckBox = new Checkbox("Customize", cbg, GridFrame.CUSTOMIZE == GridFrame.mode);

		easyCheckBox.addItemListener(this);
		normalCheckBox.addItemListener(this);
		lunaticCheckBox.addItemListener(this);
		customizeCheckBox.addItemListener(this);
		
		
		JPanel selectPanel = new JPanel();
		selectPanel.setLayout(new GridLayout(1, 4));
		selectPanel.add(easyCheckBox);
		selectPanel.add(normalCheckBox);
		selectPanel.add(lunaticCheckBox);
		selectPanel.add(customizeCheckBox);
		selectPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		// Text area - customize
		widthArea = new TextField("9-30");
		heightArea = new TextField("9-24");
		mineArea = new TextField("10-600");
		
		if(GridFrame.mode != GridFrame.CUSTOMIZE) {
			widthArea.setEditable(false);
			heightArea.setEditable(false);
			mineArea.setEditable(false);
		}
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		textPanel.add(widthArea);
		textPanel.add(heightArea);
		textPanel.add(mineArea);
		
		// button - Confirm / Cancel
		JButton confirmButton = new JButton("Confirm");
		JButton cancelButton = new JButton("Cancel");

		confirmButton.addActionListener(this);
		cancelButton.addActionListener(this);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
	
		setLayout(new GridLayout(3, 1));
		add(selectPanel);
		add(textPanel);
		add(buttonPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().toString().indexOf("Confirm") != -1) {
			if(cbg.getSelectedCheckbox() == easyCheckBox) {
				GridFrame.mode = GridFrame.EASY;
				gFrame.resizeFrame(Constant.EASY_WIDTH, Constant.EASY_HEIGHT, Constant.EASY_MINE);
				
			} else if(cbg.getSelectedCheckbox() == normalCheckBox) {
				GridFrame.mode = GridFrame.NORMAL;
				gFrame.resizeFrame(Constant.NORMAL_WIDTH, Constant.NORMAL_HEIGHT, Constant.NORMAL_MINE);
			} else if(cbg.getSelectedCheckbox() == lunaticCheckBox) {
				GridFrame.mode = GridFrame.LUNATIC;
				gFrame.resizeFrame(Constant.LUNATIC_WIDTH, Constant.LUNATIC_HEIGHT, Constant.LUNATIC_MINE);
			} else if(cbg.getSelectedCheckbox() == customizeCheckBox) {
				GridFrame.mode = GridFrame.CUSTOMIZE;
				
				int newWidth, newHeight, newMineNum;
				try{ 
					newWidth = Integer.parseInt(widthArea.getText());
				} catch (Exception exception) {
					JOptionPane.showMessageDialog(null, "Width is invalid");
					return;
				}
				if(newWidth > 30 || newWidth < 9) {
					JOptionPane.showMessageDialog(null, "Width is out of range");
					return;
				}
				
				try{ 
					newHeight = Integer.parseInt(heightArea.getText());
				} catch (Exception exception) {
					JOptionPane.showMessageDialog(null, "Height is invalid");
					return;
				}
				if(newHeight > 24 || newHeight < 9) {
					JOptionPane.showMessageDialog(null, "Height is out of range");
					return;
				}
				
				try{ 
					newMineNum = Integer.parseInt(mineArea.getText());
				} catch (Exception exception) {
					JOptionPane.showMessageDialog(null, "The number of mines is invalid");
					return;
				}
				if(newMineNum >= newWidth * newHeight) {
					JOptionPane.showMessageDialog(null, "You want to cheat, huh?");
					return;	
				}
				if(newMineNum > 600 || newMineNum < 10) {
					JOptionPane.showMessageDialog(null, "The number of mines is out of range");
					return;
				}
				gFrame.resizeFrame(newWidth, newHeight, newMineNum);
			}
			
			dispose();
		} else if(e.getSource().toString().indexOf("Cancel") != -1) {
			dispose();
		} 
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(cbg.getSelectedCheckbox() == customizeCheckBox) {
			widthArea.setEditable(true);
			heightArea.setEditable(true);
			mineArea.setEditable(true);
		} else {
			widthArea.setEditable(false);
			heightArea.setEditable(false);
			mineArea.setEditable(false);
		}
	}
}
