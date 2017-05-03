package similarity;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import cbr.MetaHandler;
import graphics.SimilarityWindow;

public class SimilarityHistoryComplete {
	private Vector<SimilarityHistory> histories = new Vector<SimilarityHistory>();
	private double totalSimilarity;
	private int childId;
	
	
	public SimilarityHistoryComplete(int childId) {
		this.childId = childId;
	}
	
	public void addHistory(SimilarityHistory history) {
		this.histories.addElement(history);
	}
	
	public void setTotalSimilarity(double totalSimilarity) {
		this.totalSimilarity = totalSimilarity;
	}
	
	public double getTotalSimilarity() {
		return this.totalSimilarity;
	}
	
	public int getChildId() {
		return this.childId;
	}

	@Override
	public String toString() {
		String info = "SimiliarityHiistoryComplete: ";
		for (SimilarityHistory history : histories) {
			info += history.toString();
		}
		return info;
	}
	
	public JPanel showSimilarityMeasures() {
		return new SimWindow(this.histories, this.totalSimilarity);
	}
	
	// Todo ta med patientens id...
	// Egentligen kanske göra det här för alla patienter på en gång... men det blir i framtiden...
	class SimWindow extends JPanel {
		private JLabel[] clickLabels;
		
		public SimWindow(Vector<SimilarityHistory> histories, double total) {
			setLayout( new GridLayout(4,histories.size()+2)	 );
			int width = SimilarityWindow.WIDTH;
			int height = SimilarityWindow.HEIGHT;
			
			JLabel label = new JLabel(String.format("Patient %d värde", childId));
			label.setPreferredSize(new Dimension(width, height));
			label.setBorder(new EtchedBorder());
			add(label);
			for (int i=0; i<histories.size(); i++) {
				label = new JLabel(  String.format("%.1f", histories.get(i).getValueFromCaseBase()) );
				label.setPreferredSize(new Dimension(width, height));
				label.setBorder(new EtchedBorder());
				add(label);
			}
			label = new JLabel();
			label.setPreferredSize(new Dimension(width, height));
			label.setBorder(new EtchedBorder());
			add(label);
			
			label = new JLabel(String.format("Patient %d skillnad", childId));
			label.setPreferredSize(new Dimension(width, height));
			label.setBorder(new EtchedBorder());
			add(label);
			for (int i=0; i<histories.size(); i++) {
				label = new JLabel( String.format("%.3f", histories.get(i).getDifference()) );
				label.setPreferredSize(new Dimension(width, height));
				label.setBorder(new EtchedBorder());
				add(label);
			}
			label = new JLabel();
			label.setPreferredSize(new Dimension(width, height));
			label.setBorder(new EtchedBorder());
			add(label);
			
			label = new JLabel(String.format("Patient %d similarity", childId));
			label.setPreferredSize(new Dimension(width, height));
			label.setBorder(new EtchedBorder());
			add(label);
			clickLabels = new JLabel[histories.size()];
			for (int i=0; i<histories.size(); i++) {
				clickLabels[i] = new JLabel( String.format("%.3f", histories.get(i).getSimilarity()));
				clickLabels[i].setPreferredSize(new Dimension(width, height));
				clickLabels[i].setBorder(new EtchedBorder());
				clickLabels[i].addMouseListener(new MouseClickListener());
				clickLabels[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
				add(clickLabels[i]);
			}
			label = new JLabel();
			label.setPreferredSize(new Dimension(width, height));
			label.setBorder(new EtchedBorder());
			add(label);

			label = new JLabel(String.format("Patient %d totalt", childId));
			label.setPreferredSize(new Dimension(width, height));
			label.setBorder(new EtchedBorder());
			add(label);
			for (int i=0; i<histories.size(); i++) {
				label = new JLabel( String.format("%.3f", histories.get(i).getTotal()));
				label.setPreferredSize(new Dimension(width, height));
				label.setBorder(new EtchedBorder());
				add(label);
			}
			label = new JLabel(String.format("%.4f",total));
			label.setPreferredSize(new Dimension(width, height));
			label.setBorder(new EtchedBorder());
			add(label);
			
			setBackground(new Color(253,253,253));
			setVisible(true);
		}
		
		class MouseClickListener extends MouseAdapter {
			@Override
			public void mousePressed(MouseEvent event) {
				JLabel label = (JLabel) event.getSource();
				int index = -1;
				for (int i=0; i<clickLabels.length; i++) {
					if (label == clickLabels[i]) {
						index = i;
						break;
					}
				}
				if (index > 0) {
					MetaHandler.showSimCalculatorGraph(index-1, histories.get(index), "Patient " + childId);	// index-1 because of age not in the array used (yet)
				}
			}
		}
		
	}
	
	
	
	

}
