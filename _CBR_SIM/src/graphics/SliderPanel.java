package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;

import cbr.PanelData;

public class SliderPanel extends JPanel
{
	private int min;
	private int max;
	private int range;
	private JSlider slider = new JSlider();

	public SliderPanel(PanelData data)
	{
		this.min = data.getMin();
		this.max = data.getMax();
		this.range = max - min;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createRigidArea(new Dimension(1, 5)));
		add(new JLabel(data.getName()));
		add(Box.createRigidArea(new Dimension(1, 7)));

		slider.setMinimum(this.min);
		slider.setMaximum(this.max);
		slider.setValue(min + range / 2);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		int majorTickSpacing = range / 10;
		if (majorTickSpacing < 1)
			majorTickSpacing = 1;
		slider.setMajorTickSpacing(majorTickSpacing);
		slider.setMinorTickSpacing(1);
		slider.setBackground(Color.white);
		add(slider);

		setBackground(Color.white);
		setPreferredSize(new Dimension(700, 80));
		setBorder(new EtchedBorder());
	}
	
	public int getSliderValue()
	{
		return slider.getValue();
	}

}
