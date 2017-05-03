package graphics;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import examination.Examination;

public class PatientValuePanel extends JPanel
{
	public static int HEIGHT = 65;
	private Vector<JLabel> labels = new Vector<JLabel>();
	private PatientPanel parent = null;
	private boolean[] changeable = { false, false, false, true, true, true, true };
	private boolean[] active = { false, false, false, true, false, false, false };

	public PatientValuePanel(String[] values, boolean interactive)
	{
		setBackground(Color.white);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(1300, HEIGHT));

		for (int i = 0; i < values.length; i++)
		{
			String value = values[i];
			if (values[i].contains("."))
			{ // Restrict number of decimals in floatnig point numbers
				value = String.format("%.1f", Float.parseFloat(value));
			}
			JLabel label = new JLabel("<html>" + value + "</html>");
			label.setBorder(new EtchedBorder());
			label.setPreferredSize(new Dimension(150, 50));
			if (interactive && changeable[i])
			{
				label.addMouseListener(new MyMouseListener());
				label.setCursor(new Cursor(Cursor.HAND_CURSOR));
				if (active[i])
				{
					label.setBackground(Examination.getColor(i));
				}
			}
			labels.add(label);
			add(label);
		}
	}

	public void setParent(PatientPanel parent)
	{
		this.parent = parent;
	}

	public class MyMouseListener extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent event)
		{
			for (int i = 0; i < labels.size(); i++)
			{
				if (event.getComponent() == labels.get(i))
				{
					if (changeable[i])
					{
						if (active[i])
						{
							labels.get(i).setBackground(Color.white);
							parent.alertDisActivated(i);
						}
						else
						{
							labels.get(i).setBackground(Examination.getColor(i));
							parent.alertActivated(i);
						}
						active[i] = !active[i];
					}
					break;
				}
			}
		}
	}

}
