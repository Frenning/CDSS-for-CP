package graphics;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import cbr.Child;
import cbr.Program;

public class FetchSimilarWindow extends JFrame
{
	private Program program;
	private int width = 1500;
	private int height = 1000;
	private int topHeight = 70;
	private Vector<Child> children;
	private Vector<JPanel> clickables = new Vector<JPanel>();
	private JPanel panel = new JPanel();
	private JScrollPane scroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel descriptionPanel = new JPanel();
	private JScrollPane topScroll = new JScrollPane(descriptionPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	public FetchSimilarWindow(Program program, Vector<Child> children)
	{
		this.program = program;
		this.children = children;
		// setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS) );
		setLayout(null);
		// descriptionPanel.setBackground(Color.white);
		// topScroll.setPreferredSize(new Dimension(width, topHeight));
		// descriptionPanel.setPreferredSize(new Dimension(width, topHeight));
		descriptionPanel.add(new FramedLabel("id"));
		descriptionPanel.add(new FramedLabel("födelseår"));
		descriptionPanel.add(new FramedLabel("kön"));
		for (String desc : Child.columnDescriptions)
		{
			descriptionPanel.add(new FramedLabel(desc));
		}
		add(topScroll);
		topScroll.setBounds(0, 0, width, topHeight);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		// panel.setBackground(Color.white);
		// panel.setPreferredSize(new Dimension(width, height*10));
		for (Child child : children)
		{
			JPanel childPanel = new JPanel();
			childPanel.add(new FramedLabel(child.getId()));
			childPanel.add(new FramedLabel(child.getBirthYear()));
			childPanel.add(new FramedLabel(child.getGender()));
			for (String value : child.getColumnValues())
			{
				childPanel.add(new FramedLabel(value));
			}
			childPanel.setBorder(new LineBorder(Color.black));
			childPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			childPanel.addMouseListener(new ClickListener());
			childPanel.setBackground(Color.white);
			clickables.add(childPanel);
			panel.add(childPanel);
			// childPanel.setPreferredSize(new Dimension(this.width, 100));
		}
		this.add(scroll);
		scroll.setBounds(0, topHeight, width, height - topHeight);
		setSize(new Dimension(width + 20, height));
		setTitle("Existing patients");
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	class ClickListener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent event)
		{
			for (int i = 0; i < clickables.size(); i++)
			{
				if (event.getSource() == clickables.get(i))
				{
					clickables.get(i).setBackground(Color.cyan);
					program.fetchValuesFromDatabase(children.get(i));
					break;
				}
			}
		}
	}

	class FramedLabel extends JLabel
	{
		public FramedLabel(String text)
		{
			this.setHorizontalAlignment(JLabel.CENTER);
			this.setPreferredSize(new Dimension(100, 50));
			this.setBorder(new LineBorder(Color.gray));
			this.setText(text);
		}

		public FramedLabel(int number)
		{
			this(String.format("%d", number));
		}
	}

}
