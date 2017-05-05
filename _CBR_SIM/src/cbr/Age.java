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

public class Age
{
	public static int MIN = 2;
	public static int MAX = 20;
	private static int range = MAX - MIN;
	private static double weight = 0.0;
	private static AgeSimTableModel ageSimTableModel = new AgeSimTableModel();

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
	
	public static boolean isSameAgeGroup(int currentAge, int otherAge)
	{
		
		switch(currentAge)
		{
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
			return otherAge == currentAge;
		case 8:
			return (8 == otherAge || otherAge == 9);
		case 9:
			return (8 <= otherAge && otherAge <= 10);
		case 10:
			return (9 <= otherAge && otherAge <= 12);
		case 11:
			return (10 <= otherAge && otherAge <= 13);
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
			return (currentAge-2 <= otherAge && otherAge <= currentAge+2);
		case 20:
		case 21:
		case 22:
			return (currentAge-(currentAge-20)-2 <= otherAge && otherAge <= currentAge+5);			//exempel: 21-(21-20)-2 = 18
		case 23:
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:
		case 29:
		case 30:
			return (currentAge-5 <= otherAge && otherAge <= currentAge+5);
		}
		return false;
	}

	public static double getWeight()
	{
		return weight;
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
