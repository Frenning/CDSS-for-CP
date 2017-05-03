package graphics;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import cbr.Age;
import cbr.ExaminationHistory;
import cbr.MetaHandler;

public class SimilarityWindow extends JFrame
{
	private JPanel background = new JPanel();
	private JScrollPane scroll = new JScrollPane(background, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel topPanel = new JPanel();
	private JScrollPane topScroll = new JScrollPane(topPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	public static int WIDTH = 100;
	public static int HEIGHT = 50;

	public SimilarityWindow(Vector<ExaminationHistory> histories, int[] values, int age)
	{
		int nrOfColumns = MetaHandler.getNrOfColumns() + 3;

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, nrOfColumns));
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		label.setBorder(new EtchedBorder());
		panel.add(label);
		label = new JLabel("�lder");
		label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		label.setBorder(new EtchedBorder());
		panel.add(label);
		for (String columnName : MetaHandler.getColumnNames())
		{
			label = new JLabel(columnName);
			label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
			label.setBorder(new EtchedBorder());
			panel.add(label);
		}
		label = new JLabel();
		label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		label.setBorder(new EtchedBorder());
		panel.add(label);

		JPanel currentPatientValuesPanel = new JPanel();
		currentPatientValuesPanel.setLayout(new GridLayout(1, nrOfColumns));
		label = new JLabel("Din patients v�rde");
		label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		label.setBorder(new EtchedBorder());
		currentPatientValuesPanel.add(label);
		//convert int to string array
		String[] svalues = new String[values.length];
		int i = 0;
	    while (i < values.length) {
	    	svalues[i] = String.valueOf(values[i++]);
	    }

		for (String data : svalues)
		{
			label = new JLabel(data);
			label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
			label.setBorder(new EtchedBorder());
			currentPatientValuesPanel.add(label);
		}
		label = new JLabel();
		label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		label.setBorder(new EtchedBorder());
		currentPatientValuesPanel.add(label);

		JPanel weightPanel = new JPanel();
		weightPanel.setLayout(new GridLayout(1, nrOfColumns));
		label = new JLabel("Vikt");
		label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		label.setBorder(new EtchedBorder());
		weightPanel.add(label);
		label = new JLabel(String.format("%.3f", Age.getWeight()));
		label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		label.setBorder(new EtchedBorder());
		weightPanel.add(label);
		for (double weight : MetaHandler.getWeighs())
		{
			label = new JLabel(String.format("%.3f", weight));
			label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
			label.setBorder(new EtchedBorder());
			weightPanel.add(label);
		}
		label = new JLabel();
		label.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		label.setBorder(new EtchedBorder());
		weightPanel.add(label);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topScroll.setPreferredSize(new Dimension(1200, 400));
		topPanel.add(panel);
		topPanel.add(currentPatientValuesPanel);
		topPanel.add(weightPanel);
		add(topScroll);

		add(scroll);
		background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));

		for (ExaminationHistory history : histories)
		{
			panel = history.getSimilarityHistory().showSimilarityMeasures();
			background.add(panel);
		}
		setSize(new Dimension(1200, 1000));
		setTitle("Similaries");
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

}
