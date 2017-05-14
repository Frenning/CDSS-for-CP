package cbr;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;

import examination.Examination;
import similarity.AgeSimTableModel;
import similarity.Breakpoint;
import similarity.SimCalculator;

public class Age
{
	public static int MIN = 2;
	public static int MAX = 20;
	private static int range = MAX - MIN;
	
	public static double pubertyWeight = 1.5;
	public static int similarityFallOff = 2;
	public static double maxSimilarity = 0.5;
	
	private static AgeSimTableModel ageSimTableModel = new AgeSimTableModel();
	public static SimCalculator simCalculator = new SimCalculator();
	public static SimCalculator maxAgeDiffCalculator = new SimCalculator();
	
	public static void addBreakPoints(double age, String puberty)
	{
		double maxAgeDiff = maxAgeDiffCalculator.getWeight(age);
		
		if((puberty == "Ja" || puberty == "Vet ej") && age <= 18)
			maxAgeDiff *= pubertyWeight;
		double lowestAge = maxAgeDiffCalculator.calculateLowestAgeDiff(age);
		if(puberty == "Nej" && age >= 16)
			lowestAge *= pubertyWeight;
		
		simCalculator = new SimCalculator();
		simCalculator.addBreakpoint(new Breakpoint(lowestAge-similarityFallOff, 0));
		simCalculator.addBreakpoint(new Breakpoint(lowestAge, maxSimilarity));
		simCalculator.addBreakpoint(new Breakpoint(age, maxSimilarity));
		simCalculator.addBreakpoint(new Breakpoint(age+maxAgeDiff, maxSimilarity));
		simCalculator.addBreakpoint(new Breakpoint(age+maxAgeDiff+similarityFallOff, 0));
		
		MetaHandler.ageCalculator = simCalculator;
	}
	
	// Todo hämta från
	public static double calculateSimilarity(double ageAtExamination, int age)
	{
		if (ageAtExamination < 0)
		{
			System.out.println("Fel: ageAtExamination är mindre än noll");
			return 0;
		}
		Object sim = ageSimTableModel.getValueAt((int) ageAtExamination, age + 1); // Add one because the first column contains headlines/descriptions
		return (double) sim;
		/*
		 * double ageDiff = Math.abs(ageAtExamination - age); double normalized
		 * = (double) ageDiff / (double) range; normalized = 1 - normalized;
		 * return normalized;
		 */
	}
	
	
	//getAgeAtExamination diffToDouble takes exam date and sub birthday and then converts time to years	
	public static double getAgeAtExamination(int birthYear, String examinationDate)
	{
		return Examination.ageDiffToDouble(Examination.sqlStringToDate(examinationDate),
				Examination.birthYearToDate(birthYear));
	}
	
	public static double calculateAgeSim(double otherAge)
	{
		// Exempel: currentAge = 9. maxAgeDiff är då = 2.5. ->
		// -> breakPoint nr 1 = 5.5, breakpoint nr 2 = 6.5, breakpoint nr 3 = 11.5 och breakpoint nr 4 = 12.5

		return simCalculator.getWeight(otherAge);
	}

	public static void showAgeSimWindow()
	{
		new AgeSimWindow();
	}

	static class AgeSimWindow extends JFrame
	{
		private JMenuBar menuBar = new JMenuBar();
		private JMenu menu = new JMenu("Arkiv");
		private JMenuItem item = new JMenuItem("Spara");

		public AgeSimWindow()
		{
			menu.add(item);
			menuBar.add(menu);
			setJMenuBar(menuBar);
			item.addActionListener(new SaveListener());
			menu.setMnemonic(KeyEvent.VK_A);
			item.setMnemonic(KeyEvent.VK_S);
			item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)); //hotkey
			JTable table = new JTable(ageSimTableModel);
			for (int i = 0; i < ageSimTableModel.getColumnCount(); i++)
			{
				table.getColumnModel().getColumn(i).setCellRenderer(new CellColorRenderer());
			}
			JScrollPane scroll = new JScrollPane(table);
			table.setFillsViewportHeight(true);
			add(scroll);
			setSize(new Dimension(1200, 500));
			setTitle("Redigera similarities för ålder");
			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}

		class SaveListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ageSimTableModel.writeFile();
			}
		}

		class CellColorRenderer extends DefaultTableCellRenderer
		{
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int col)
			{
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						col);
				if (col == 0)
				{
					label.setBackground(new Color(238, 238, 238));
				}
				else if (col == row + 1)
				{
					label.setBackground(new Color(255, 255, 100));
				}
				else
				{
					label.setBackground(Color.white);
				}
				return label;
			}
		}
	}

}
