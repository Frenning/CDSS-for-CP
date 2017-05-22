package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import javax.swing.JOptionPane;

import cbr.Age;
import cbr.ExaminationHistory;
import cbr.MetaHandler;
import cbr.Program;
import graphics.Window.MenuListener;
import graphics.Window.SimGraphWindow;

public class AdjustWeightsWindow extends JFrame
{
	public DoubleJTextField age = new DoubleJTextField();
	public DoubleJTextField standing = new DoubleJTextField();
	public DoubleJTextField hours = new DoubleJTextField();
	public DoubleJTextField dorsalExt = new DoubleJTextField();
	public DoubleJTextField dorsalFlek = new DoubleJTextField();
	public DoubleJTextField plantar = new DoubleJTextField();
	public DoubleJTextField surgery = new DoubleJTextField();

	public AdjustWeightsWindow()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		getContentPane().add(panel);
		
        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.EAST;
        GridBagConstraints right = new GridBagConstraints();
        right.weightx = 2.0;
        right.fill = GridBagConstraints.HORIZONTAL;
        right.gridwidth = GridBagConstraints.REMAINDER;
		
		
		JLabel text = new JLabel("Ålder:");
		panel.add(text, left);
		panel.add(age, right);
		
		text = new JLabel("Använder ståhjälpmedel:");
		panel.add(text, left);
		panel.add(standing, right);
		 
		text = new JLabel("Ståhjälpmedel - Timmar per vecka:");
		panel.add(text, left);
		panel.add(hours, right);
		
		text = new JLabel("Dorsalflexion (extenderat knä) höger/vänster:");
		panel.add(text, left);
		panel.add(dorsalExt, right);
		
		text = new JLabel("Dorsalflexion (flekterat knä) höger/vänster:");
		panel.add(text, left);
		panel.add(dorsalFlek, right);
		
		text = new JLabel("Plantar flexion höger/vänster:");
		panel.add(text, left);
		panel.add(plantar, right);
		
		text = new JLabel("Operation:");
		panel.add(text, left);
		panel.add(surgery, right);
		 
		JButton btnSave = new JButton("Spara");
		
		btnSave.addActionListener(new WeightsSaveButtonListener());
		add(btnSave, BorderLayout.SOUTH);
		setSize(new Dimension(325, 250));
		setTitle("Adjust weights");
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	class WeightsSaveButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			double totalWeights = Double.parseDouble(age.getText());
			totalWeights += Double.parseDouble(standing.getText());
			totalWeights += Double.parseDouble(hours.getText());
			totalWeights += Double.parseDouble(dorsalExt.getText());
			totalWeights += Double.parseDouble(dorsalFlek.getText());
			totalWeights += Double.parseDouble(plantar.getText());
			totalWeights += Double.parseDouble(surgery.getText());
			
			if(totalWeights == 1.0)
			{
				Program.weights[0] = Double.parseDouble(age.getText());
				Program.weights[1] = Double.parseDouble(standing.getText());
				Program.weights[2] = Double.parseDouble(hours.getText());
				Program.weights[3] = Double.parseDouble(plantar.getText());
				Program.weights[4] = Double.parseDouble(dorsalFlek.getText());
				Program.weights[5] = Double.parseDouble(dorsalExt.getText());
				Program.weights[6] = Double.parseDouble(surgery.getText());
				JOptionPane.showMessageDialog(null, "Values saved.");
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Entered values that does not add up to exactly 1");
			}
		}
	}

}
