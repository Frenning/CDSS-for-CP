package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import cbr.ExaminationHistory;

public class PatientPanel extends JPanel {
	private static int HEIGHT = HistoryPanel.HEIGHT + HistoryPanel.PADDING_Y + PatientValuePanel.HEIGHT + 50;
	public static int WIDTH = 1800;
	private ExaminationHistory history;
	private HistoryPanel historyPanel;
	private PatientValuePanel valuePanel;
	private JPanel treatmentInfoPanel;
	private JScrollPane scroll;
	private JLabel treatmentLabel;
	private String treatmentLabelContent = "";
	
	public PatientPanel(ExaminationHistory history) {
		this.history = history;
		historyPanel = new HistoryPanel(this, history);
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setBackground(Color.white);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBorder(new EtchedBorder());
		valuePanel = new PatientValuePanel(history.relevant().getValues(), true);
		valuePanel.setParent(this);
		add(this.patientLabel());
		add(valuePanel);
		add(historyPanel);
		treatmentLabel = this.treatmentLabel();
		treatmentInfoPanel = new JPanel();
		scroll = new JScrollPane(treatmentInfoPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroll);
		scroll.setPreferredSize(new Dimension(500, HistoryPanel.HEIGHT+HistoryPanel.PADDING_Y));
		treatmentInfoPanel.setBackground(Color.white);
		treatmentInfoPanel.add(treatmentLabel);
		scroll.setVisible(false);
	}
	
	private JLabel patientLabel() {
		JLabel patient = new JLabel("Patient " + history.getChild());
		patient.setFont(new Font("Serif", Font.BOLD, 20));
		patient.setBackground(Color.white);
		patient.setBorder(BorderFactory.createEmptyBorder());
		patient.setHorizontalTextPosition(JLabel.LEFT);
		patient.setHorizontalTextPosition(JLabel.LEFT);
		patient.setPreferredSize(new Dimension(WIDTH, 50));
		return patient;
	}
	
	private JLabel treatmentLabel() {
		JLabel label = new JLabel();
		label.setVerticalAlignment(JLabel.TOP);
		label.setPreferredSize(new Dimension(500, HistoryPanel.HEIGHT+HistoryPanel.PADDING_Y+100));
		return label;
	}
	
	
	public void alertActivated(int i) {
		historyPanel.setDisplay(i, true);
	}
	
	public void alertDisActivated(int i) {
		historyPanel.setDisplay(i, false);
	}
	
	public void removeTreatmentLabel() {
		treatmentLabelContent = "";
	}
	
	public void updateTreatmentLabel(String info) {
		treatmentLabelContent += info + "<br/>";
		treatmentLabel.setText( "<html>" + treatmentLabelContent + "</html>" );
		scroll.setVisible(true);
	}
	


	
}
