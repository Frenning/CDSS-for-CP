package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import cbr.Age;
import cbr.ExaminationHistory;
import cbr.MetaHandler;
import graphics.Window.MenuListener;
import graphics.Window.SimGraphWindow;

public class ResultWindow extends JFrame
{
	private int[] values;
	private int age;
	private Vector<ExaminationHistory> histories;
	private JPanel panel = new JPanel();
	private JScrollPane scroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu = new JMenu("Arkiv");
	private JMenuItem item = new JMenuItem("Visa similarity metrics");

	public ResultWindow(Vector<ExaminationHistory> histories, ExaminationHistory patientHistory, int[] values, int age, int GMFCS_currentPatient, String[] standingInformation)
	{
		menu.add(item);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		menu.setMnemonic(KeyEvent.VK_A);
		item.setMnemonic(KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		item.addActionListener(new MenuListener());

		this.values = values;
		this.age = age;
		this.histories = histories;
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setPreferredSize(new Dimension(PatientPanel.WIDTH + 300, 1000));
		add(topPanel);
		topPanel.add(descriptionPanel()); // A panel in the top that is not
											// scrolled
		PatientPanel pp = new PatientPanel(patientHistory, GMFCS_currentPatient, standingInformation);
		pp.setPreferredSize(new Dimension(500, 1000));
		topPanel.add(pp);

		add(scroll); // The rest of the content is scrolled
		scroll.setPreferredSize(new Dimension(PatientPanel.WIDTH + 300, 700));

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.white);
		for (ExaminationHistory history : histories)
		{
			panel.add(new PatientPanel(history));
		}
		setSize(new Dimension(PatientPanel.WIDTH + 50, 1000));
		setTitle("Similar patients");
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	// A grey panel with rectangles with the description of the values
	private JPanel descriptionPanel()
	{
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setPreferredSize(new Dimension(1300, 90));
		JPanel pvp = new PatientValuePanel(MetaHandler.getDescriptionsExtended(), false);
		pvp.setBackground(new Color(200, 200, 200));
		topPanel.add(pvp);
		topPanel.setOpaque(true);
		return topPanel;
	}

	// A label with the text "Din patient"
	private JLabel yourPatient()
	{
		JLabel yourPatient = new JLabel("Din patient");
		yourPatient.setFont(new Font("Serif", Font.BOLD, 20));
		yourPatient.setBackground(Color.white);
		yourPatient.setBorder(BorderFactory.createEmptyBorder());
		yourPatient.setHorizontalTextPosition(JLabel.LEFT);
		yourPatient.setHorizontalTextPosition(JLabel.LEFT);
		return yourPatient;
	}

	class MenuListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			new SimilarityWindow(histories, values, age);
		}
	}

}
