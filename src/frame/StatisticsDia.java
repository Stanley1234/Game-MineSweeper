package frame;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.SaveFile;

@SuppressWarnings("serial")
public class StatisticsDia extends JDialog implements ActionListener, ItemListener{
	
	
	private int curMode = GridFrame.mode;
	
	private JLabel gamePlayedLabel;
	private JLabel gameWondLabel;
	private JLabel maxContWinLabel;
	private JLabel maxContLoseLabel;
	private JLabel winPercentLabel;
	
	private GridFrame gFrame;
	
	public StatisticsDia(GridFrame gFrame) {
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
		CheckboxGroup cbg = new CheckboxGroup();
		Checkbox easyCheckBox = new Checkbox("Easy", cbg, GridFrame.EASY == curMode);
		Checkbox normalCheckBox = new Checkbox("Normal", cbg, GridFrame.NORMAL == curMode);
		Checkbox lunaticCheckBox = new Checkbox("Lunatic", cbg, GridFrame.LUNATIC == curMode);
		Checkbox customizeCheckBox = new Checkbox("Customize", cbg, GridFrame.CUSTOMIZE == curMode);
		
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
		
		// info 
		gamePlayedLabel = new JLabel("Game Played: " + SaveFile.getGamePlayedTime(curMode));
		gameWondLabel = new JLabel("Game Won: " + SaveFile.getWinTime(curMode));
		maxContWinLabel = new JLabel("Continual Win: " + SaveFile.getMaxContWin(curMode));
		maxContLoseLabel = new JLabel("Continual Lose: " + SaveFile.getMaxContLose(curMode));
		winPercentLabel = new JLabel("Winning Percentage: " + SaveFile.getPercent(curMode));
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(4, 1));
		infoPanel.add(gamePlayedLabel);
		infoPanel.add(gameWondLabel);
		infoPanel.add(maxContWinLabel);
		infoPanel.add(maxContLoseLabel);
		
		infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 10, 20));
		
		// button - exit this dialoge / reset
		JButton exitButton = new JButton("Exit Dialogue");
		JButton resetButton = new JButton("Reset Statistics");
		
		exitButton.addActionListener(this);
		resetButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		buttonPanel.add(exitButton);
		buttonPanel.add(resetButton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		// add all panels 
		setLayout(new GridLayout(3, 1));
		add(selectPanel);
		add(infoPanel);
		add(buttonPanel);
		
	}
	
	private void resetInfoLabel() {
		gamePlayedLabel.setText("Game Played: " + SaveFile.getGamePlayedTime(curMode));
		gameWondLabel.setText("Game Won: " + SaveFile.getWinTime(curMode));
		maxContWinLabel.setText("Continual Win: " + SaveFile.getMaxContWin(curMode));
		maxContLoseLabel.setText("Continual Lose: " + SaveFile.getMaxContLose(curMode));
		winPercentLabel.setText("Winning Percentage: " + SaveFile.getPercent(curMode));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource().toString().indexOf("Exit Game") != -1) {
			dispose();
		} else if(e.getSource().toString().indexOf("Reset Statistics") != -1) {
			SaveFile.resetRecord();
			resetInfoLabel();
		} 
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource().toString().indexOf("Easy") != -1) {
			curMode = GridFrame.EASY;
			resetInfoLabel();
		} else if(e.getSource().toString().indexOf("Normal") != -1) {
			curMode = GridFrame.NORMAL;
			resetInfoLabel();
		} else if(e.getSource().toString().indexOf("Lunatic") != -1) {
			curMode = GridFrame.LUNATIC;
			resetInfoLabel();
		} else if(e.getSource().toString().indexOf("Customize") != -1) {
			curMode = GridFrame.CUSTOMIZE;
			resetInfoLabel();
		}
	}
}
