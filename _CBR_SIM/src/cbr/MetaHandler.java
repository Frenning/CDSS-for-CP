package cbr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;

import similarity.SimCalculator;
import similarity.SimilarityHistory;

public class MetaHandler
{
	private static int nrOfColumns;
	private static String[] columnNames;
	private static String[] descriptions;
	private static double[] weights;
	private static PanelData[] data;
	private static int[] averages;
	private static SimCalculator[] calculators;

	public static void init()
	{
		BufferedReader reader = null;
		Vector<String> lines = new Vector<String>();
		String line;
		try
		{
			reader = new BufferedReader(new FileReader("meta.csv"));
			while ((line = reader.readLine()) != null)
			{
				lines.add(line);
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		nrOfColumns = lines.size();
		columnNames = new String[nrOfColumns];
		descriptions = new String[nrOfColumns];
		weights = new double[nrOfColumns];
		averages = new int[nrOfColumns];
		data = new PanelData[nrOfColumns];
		calculators = new SimCalculator[nrOfColumns];
		for (int i = 0; i < nrOfColumns; i++)
		{
			String[] cells = lines.get(i).split(",");
			String columnName = cells[0];
			String description = cells[1];
			double weight = Double.parseDouble(cells[2]);
			int min = Integer.parseInt(cells[3]);
			int max = Integer.parseInt(cells[4]);
			data[i] = new PanelData(columnName, description, min, max);
			double maxDiff = Math.abs(min) + Math.abs(max);
			calculators[i] = new SimCalculator(weight, maxDiff, description);
			averages[i] = Integer.parseInt(cells[5]);
			weights[i] = weight;
			columnNames[i] = columnName;
			descriptions[i] = description;
		}

		// Reading values for similarity calculations
		lines = new Vector<String>();
		try
		{
			reader = new BufferedReader(new FileReader("similarities.csv"));
			while ((line = reader.readLine()) != null)
			{
				lines.add(line);
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		if (lines.size() != nrOfColumns)
		{
			System.out.println("Problem får få eller många rader i similarites.csv !!!"); // Todo kasta undantag
		}
		else
		{
			for (int i = 0; i < nrOfColumns; i++)
			{
				String[] breakpoints = lines.get(i).split(",");
				calculators[i].addBreakpoints(breakpoints);
			}
		}
	}

	public static String[] getColumnNames()
	{
		return columnNames;
	}

	public static String getColumnsName(int index)
	{
		return columnNames[index];
	}

	public static String getColumnNamesCommaSeparated()
	{
		String string = "";
		for (String column : MetaHandler.getColumnNames())
		{
			string += column + ", ";
		}
		return string;
	}

	public static String[] getDescriptions()
	{
		return descriptions;
	}

	public static String[] getDescriptionsExtended()
	{
		String[] desc = new String[descriptions.length + 1];
		desc[0] = "Ålder";
		for (int i = 0; i < descriptions.length; i++)
		{
			desc[i + 1] = descriptions[i];
		}
		return desc;
	}

	public static int getNrOfColumns()
	{
		return nrOfColumns;
	}

	public static PanelData[] getPanelData()
	{
		return data;
	}

	public static double getNormaizedDiff(int index, int diff)
	{
		return data[index].getNormaizedDiff(diff);
	}

	public static double[] getWeighs()
	{
		return weights;
	}

	public static double getWeight(int index)
	{
		return weights[index];
	}

	public static int getAverage(int index)
	{
		return averages[index];
	}

	public static double calculateSimilarity(int index, int current, int other)
	{
		return calculators[index].getWeight(current, other);
	}

	public static void showSimCalculatorGraph(int index, SimilarityHistory history, String patientName)
	{
		calculators[index].showWindow(history, patientName);
	}

	public static JPanel[] getSimCalculatorGraphs()
	{
		JPanel[] panels = new JPanel[calculators.length];
		for (int i = 0; i < panels.length; i++)
		{
			panels[i] = calculators[i].getPanel();
		}
		return panels;
	}

}
