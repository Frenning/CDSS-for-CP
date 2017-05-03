package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DateFormat;

import javax.swing.JPanel;

import cbr.Dorsal;
import cbr.ExaminationHistory;
import examination.Boutolium;
import examination.Examination;
import examination.Orthosis;
import examination.Treatment;

public class HistoryPanel extends JPanel
{
	private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
	private ExaminationHistory history;
	public static int HEIGHT = 220;
	public static int PADDING_Y = 65;
	private static int PADDING_X = 80;
	private static int WIDTH = 1300 - PADDING_X;
	int max = 140;
	int min = -80;
	int span = max - min;
	double ratio = span / HEIGHT;
	int diameter = 10;
	int radius = diameter / 2;
	private PatientPanel parent;
	private boolean[] showDorsal = { true, false, false, false };

	public HistoryPanel(PatientPanel parent, ExaminationHistory history)
	{
		this.parent = parent;
		this.history = history;
		setBackground(Color.white);
		setPreferredSize(new Dimension(WIDTH + PADDING_X, HEIGHT + PADDING_Y));
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Font defaultFont = g.getFont();
		g.setColor(new Color(250, 250, 250));
		g.fillRect(PADDING_X, 0, WIDTH, HEIGHT);
		g.setColor(Color.gray);
		g.drawRect(PADDING_X, 0, WIDTH, HEIGHT);
		g.setColor(Color.black);
		g.drawString("vinkel", 10, HEIGHT / 2);
		for (int y = 120; y > min; y -= 30)
		{
			g.setColor(Color.black);
			g.drawString("" + y, 55, this.convertY(y));
			if (y != 0)
				g.setColor(Color.gray);
			g.drawLine(PADDING_X, this.convertY(y), WIDTH, this.convertY(y));
		}
		int x;
		int y;
		double age;
		int[] lastX = new int[4];
		int[] lastY = new int[4];
		parent.removeTreatmentLabel();
		Examination examination;
		for (int exId = 0; exId < history.getExaminations().size(); exId++)
		{ // Examination examination : history.getExaminations()
			examination = history.getExaminations().get(exId);
			age = examination.getAgeAtExamination();
			x = (int) (age * 60);

			g.setColor(Color.black);
			if (examination.isRelevant())
			{
				g.setFont(new Font(defaultFont.getFontName(), Font.BOLD, defaultFont.getSize()));
				g.drawString(String.format("%.1f", age), x, HEIGHT + 14);
				g.setColor(Color.black);
				g.setFont(defaultFont);
			}
			else
			{
				g.drawString(String.format("%.1f", age), x, HEIGHT + 14);

			}
			if (examination == history.first())
			{
				g.drawString("ålder vid undersökning", x - 180, HEIGHT + 25);
			}
			if (examination.hasTreatment() || examination.hasBoutolium() || examination.hasOrthosis())
			{
				parent.updateTreatmentLabel(String.format("%.1f år:", examination.getAgeAtExamination()));
				if (examination.hasOrthosis())
				{
					for (Orthosis orthosis : examination.getOrthosis())
					{
						age = examination.getAgeAtExamination();
						g.setColor(orthosis.getColor());
						x = (int) (age * 60);
						g.fillRect(x - 40, 0, 40, (int) (span * ratio));
						parent.updateTreatmentLabel(orthosis.toString());
					}
				}
				if (examination.hasTreatment())
				{
					for (Treatment treatment : examination.getTreatments())
					{
						age = treatment.getAgeAtTreatment();
						x = (int) (age * 60);
						g.setColor(Color.black);
						g.drawLine(x, 0, x, (int) (span * ratio));
						parent.updateTreatmentLabel(treatment.toString());
					}
				}
				if (examination.hasBoutolium())
				{
					for (Boutolium boutolium : examination.getBoutoliums())
					{
						age = boutolium.getAge();
						if (age == -1)
						{ // Default if not date is given, which is most common
							age = examination.getAgeAtExamination();
							if (exId > 0)
							{
								double diff = age - history.getExaminations().get(exId - 1).getAgeAtExamination();
								age -= (diff / 2);
							}
						}
						g.setColor(Color.gray);
						x = (int) (age * 60);
						g.drawLine(x, 0, x, (int) (span * ratio));
						parent.updateTreatmentLabel(boutolium.toString());
					}
				}
			}

			Dorsal dorsal = examination.getDorsal();
			;
			int[] dorsals = dorsal.getDorsal();
			for (int i = 0; i < dorsals.length; i++)
			{
				if (showDorsal[i])
				{
					g.setColor(Dorsal.getDorsalColor(i));
					y = this.convertYRadius(dorsals[i]);
					if (examination != history.first())
					{
						g.drawLine(x + radius, y + radius, lastX[i] + radius, lastY[i] + radius);
					}
					if (Dorsal.isSquare(i))
					{
						g.fillRect(x + 2, y + 2, diameter - 4, diameter - 4);
						g.setColor(Color.gray);
						g.drawRect(x + 2, y + 2, diameter - 4, diameter - 4);
					}
					else
					{
						g.fillOval(x, y, diameter, diameter);
						g.setColor(Color.gray);
						g.drawOval(x, y, diameter, diameter);
					}
					lastX[i] = x;
					lastY[i] = y;
				}
			}

		}
	}

	private int convertY(int value)
	{
		int diff = max - value;
		diff /= ratio;
		return diff;
	}

	private int convertYRadius(int value)
	{
		return this.convertY(value) - radius;
	}

	public void setDisplay(int index, boolean active)
	{
		index -= 3;
		if (index >= 0 && index < 4)
		{
			showDorsal[index] = active;
			this.repaint();
		}
	}

}
