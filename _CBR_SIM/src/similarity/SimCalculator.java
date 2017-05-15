package similarity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collections;
import java.util.Vector;

import javax.security.auth.x500.X500Principal;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimCalculator
{
	protected double weight;
	protected double maxDiff;
	protected String description;
	protected Vector<Breakpoint> breakpoints = new Vector<Breakpoint>();

	// Todo this should only be in a subclass of SimCalculator
	private int width = 1000;
	private int paddingX = 80;
	private int height = 500;
	private int paddingY = 50;

	public SimCalculator()
	{
		
	}
	public SimCalculator(double weight, double maxDiff, String description)
	{
		this.weight = weight;
		this.maxDiff = maxDiff;
		this.description = description;
	}

	public void addBreakpoint(Breakpoint breakpoint)
	{
		this.breakpoints.addElement(breakpoint);
		Collections.sort(this.breakpoints);
	}

	public void addBreakpoints(String[] values)
	{
		for (int i = 0; i < values.length; i += 2)
		{
			double x = Double.parseDouble(values[i]);
			double sim = Double.parseDouble(values[i + 1]);
			breakpoints.add(new Breakpoint(x, sim));
		}
		Collections.sort(this.breakpoints);
	}
	
	public double getWeight(double current)
	{
		int indexOfHigher = -1;
		for (int i = 0; i < this.breakpoints.size(); i++)
		{
			if (this.breakpoints.get(i).getX() >= current)
			{
				indexOfHigher = i;
				break;
			}
		}
		if (indexOfHigher == -1)
		{
			return 0;
		}
		else if (indexOfHigher == 0)
		{
			return this.breakpoints.get(indexOfHigher).getSimilarity();
		}
		else
		{
			double lowerX = this.breakpoints.get(indexOfHigher - 1).getX(); // 1
			double higherX = this.breakpoints.get(indexOfHigher).getX(); // 1,5
			double distanceX = higherX - lowerX;	// 0,5
			double diffToLowerInPercent = current - lowerX;	// 0,2
			diffToLowerInPercent /= distanceX;	// 0,2 / 0,5 = 0,4
			double similarityLower = this.breakpoints.get(indexOfHigher - 1).getSimilarity();	// 1
			double distanceSimilarity = this.breakpoints.get(indexOfHigher).getSimilarity() - similarityLower;	// 0 - 1 = -1
			double similarity = similarityLower + (diffToLowerInPercent * distanceSimilarity); // 1 + (0,4 * -1) = 0,6
			return similarity; // 0,6
			
			// ((((current-lowerX)/distanceX) * distanceSimilarity) + similarityLower) + current = 11.5
			// ((((current - 8) / 2) * 1) + 2) + current = 11.5
		}
	}

	public double getWeight(double current, double other)
	{
		double diff = other - current;
		int indexOfHigher = -1;
		for (int i = 0; i < this.breakpoints.size(); i++)
		{
			if (this.breakpoints.get(i).getX() >= diff)
			{
				indexOfHigher = i;
				break;
			}
		}
		if (indexOfHigher == 0)
		{
			return this.breakpoints.get(indexOfHigher).getSimilarity();
		}
		else
		{
			double lowerX = this.breakpoints.get(indexOfHigher - 1).getX(); // 1
			double higherX = this.breakpoints.get(indexOfHigher).getX(); // 1,5
			double distanceX = higherX - lowerX;	// 0,5
			double diffToLowerInPercent = diff - lowerX;	// 0,2
			diffToLowerInPercent /= distanceX;	// 0,2 / 0,5 = 0,4
			double similarityLower = this.breakpoints.get(indexOfHigher - 1).getSimilarity();	// 1
			double distanceSimilarity = this.breakpoints.get(indexOfHigher).getSimilarity() - similarityLower;	// 0 - 1 = -1
			double similarity = similarityLower + (diffToLowerInPercent * distanceSimilarity); // 1 + (0,4 * -1) = 0,6
			return similarity; // 0,6
		}
	}
	
	public double calculateLowestAgeDiff(double actualAge)
	{
		int indexOfHigher = -1;
		for (int i = 0; i < this.breakpoints.size(); i++)
		{
			if (this.breakpoints.get(i).getX() >= actualAge)
			{
				indexOfHigher = i;
				break;
			}
		}
		if (indexOfHigher == 0)
		{
			return actualAge-0.5;
		}
		else
		{
			indexOfHigher--;
			if(indexOfHigher == 0)
			{
				return actualAge-0.5;
			}
			double lowerX = this.breakpoints.get(indexOfHigher - 1).getX(); // 1
			double higherX = this.breakpoints.get(indexOfHigher).getX(); // 1,5
			double distanceX = higherX - lowerX;	// 0,5
			//double diffToLowerInPercent = actualAge - lowerX;	// 0,2
			//diffToLowerInPercent /= distanceX;	// 0,2 / 0,5 = 0,4
			double similarityLower = this.breakpoints.get(indexOfHigher - 1).getSimilarity();	// 1
			double distanceSimilarity = this.breakpoints.get(indexOfHigher).getSimilarity() - similarityLower;	// 0 - 1 = -1
			//double similarity = similarityLower + (diffToLowerInPercent * distanceSimilarity); // 1 + (0,4 * -1) = 0,6
			
			//Calculates lowest age difference for actualAge, for example if actualAge = 11.5, current will be = 9
			double current = (distanceSimilarity * lowerX + actualAge * distanceX - similarityLower * distanceX) / (distanceSimilarity + distanceX);
			return current; // 0,6
		}
	}

	public void showWindow()
	{
		this.showWindow(null, "");
	}

	public void showWindow(SimilarityHistory history, String patientName)
	{
		new SimilarityGraphWindow(history, patientName);
	}

	public JPanel getPanel()
	{
		return new SimilarityGraphPanel();
	}

	class SimilarityGraphWindow extends JFrame
	{
		public SimilarityGraphWindow(SimilarityHistory history, String patientName)
		{
			add(new SimilarityGraphPanel(history));
			setSize(new Dimension(width + (4 * paddingX), height + (2 * paddingY)));
			setTitle(description + " " + patientName);
			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
	}

	class SimilarityGraphPanel extends JPanel
	{
		private SimilarityHistory history;

		public SimilarityGraphPanel()
		{
			setBackground(Color.white);
		}

		public SimilarityGraphPanel(SimilarityHistory history)
		{
			this.history = history;
			setBackground(Color.lightGray);
		}

		@Override
		public void paint(Graphics g)
		{
			super.paint(g);
			int nrOfLines = 10;
			if (breakpoints.size() > 1)
			{
				double start = breakpoints.get(0).getX();
				double end = breakpoints.get(breakpoints.size() - 1).getX();
				double span = end - start;
				double step = span / nrOfLines;
				double spaceX = width / nrOfLines;
				g.setColor(Color.white);
				g.fillRect(paddingX, paddingY, width, height - paddingY);
				g.setColor(Color.gray);
				
				double convertX = width / span;
				// Year, month
				if(history.getDescription() == "ålder")
				{
					for (int i = 0; i < breakpoints.size(); i++)
					{
						double value = start + (i * step);
						int x = (int) ((breakpoints.get(i).getX() - start) * convertX) + paddingX;
						g.setColor(Color.LIGHT_GRAY);
						g.drawLine(x, paddingY, x, height);
						g.setColor(Color.black);
						double fracPart = breakpoints.get(i).getX() % 1;
						double integralPart = breakpoints.get(i).getX() - fracPart;
						g.drawString(String.format("%.0f y, %d m", integralPart, (int)(fracPart * 12)), x - 20, height + 25);
					}
					g.drawString(String.format("Age in years, months (y, m)"), width + 130, height + 25);
					g.drawString(String.format("Similarity"), paddingX / 6, paddingY - 25);
				}
				else
				{
				// Writing out in decimal form
					for (int i = 0; i <= nrOfLines; i++)
					{
						double value = start + (i * step);
						int x = (int) ((i * spaceX) + paddingX);
						g.setColor(Color.LIGHT_GRAY);
						g.drawLine(x, paddingY, x, height);
						g.setColor(Color.black);
						g.drawString(String.format("%.1f", value), x, height + 25);
					}
				}
				
				
				
				for (double i = 0; i <= 1; i += 0.1)
				{
					int x = (int) (paddingX / 3);
					int y = (int) (height - (i * (height - paddingY)));
					g.setColor(Color.black);
					g.drawString(String.format("%.1f", i), x, y);
					g.setColor(Color.LIGHT_GRAY);
					g.drawLine(paddingX, y, width + paddingX, y);
				}
				
				double lastX = paddingX;
				double lastY = (height) - (breakpoints.get(0).getSimilarity() * (height - paddingY));
				g.setColor(Color.black);
				for (int i = 1; i < breakpoints.size(); i++)
				{
					double currentX = ((breakpoints.get(i).getX() - start) * convertX) + paddingX;
					double currentY = (height) - (breakpoints.get(i).getSimilarity() * (height - paddingY));
					g.drawLine((int) lastX, (int) lastY, (int) currentX, (int) currentY);
					lastX = currentX;
					lastY = currentY;
				}

				if (this.history != null)
				{
					double x;
					double y = (height) - (history.getSimilarity() * (height - paddingY));
					if(history.getDescription() == "ålder")
					{
						x = ((history.getValueFromCaseBase() - start) * convertX) + paddingX;
					}
					else 
						x = ((history.getDifference() - start) * convertX) + paddingX;
					
					int diameter = 10;
					g.drawOval(((int) x - (diameter / 2)), ((int) y) - (diameter / 2), diameter, diameter);
					// Todo kanske rita hjälplinjer också

				}
			}

		}
	}

}
