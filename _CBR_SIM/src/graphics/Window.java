package graphics;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import cbr.Age;
import cbr.MetaHandler;
import cbr.PanelData;
import cbr.Program;
import cbr.ValueData;

public class Window extends JFrame
{
	private Program program;
	private SliderPanel[] panels = new SliderPanel[MetaHandler.getNrOfColumns()];
	private SliderPanel agePanel = new SliderPanel(new PanelData("birth_year", "Ålder", Age.MIN, Age.MAX));
	private JButton buttonFetch = new JButton("Hämta patient från databas");
	private JButton button = new JButton("Visa liknande patienter");
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu = new JMenu("Arkiv");
	private JMenuItem item = new JMenuItem("Visa/ändra similarity ålder");
	private JMenuItem itemSim = new JMenuItem("Visa similarity övriga");

	public Window(Program program)
	{
		this.program = program;
		menu.add(item);
		menu.add(itemSim);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		menu.setMnemonic(KeyEvent.VK_A);
		item.setMnemonic(KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		item.addActionListener(new MenuListener());
		itemSim.setMnemonic(KeyEvent.VK_O);
		itemSim.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		itemSim.addActionListener(new MenuListener());
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(buttonFetch);
		buttonFetch.addActionListener(new ButtonListenerFetch());
		add(agePanel);
		PanelData[] data = MetaHandler.getPanelData();
		for (int i = 0; i < data.length; i++)
		{
			SliderPanel panel = new SliderPanel(data[i]);
			panels[i] = panel;
			add(panel);
		}
		add(button);
		button.addActionListener(new ButtonListenerShow());
		setSize(new Dimension(800, 750));
		setTitle("Decision support - Din patient");
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	class ButtonListenerShow implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int[] values = new int[panels.length];
			for (int i = 0; i < panels.length; i++)
			{
				values[i] = panels[i].getSliderValue();
			}
			int age = agePanel.getSliderValue();
			program.fetchSimilar(new ValueData(values, age));
		}
	}

	class MenuListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == item)
			{
				Age.showAgeSimWindow();
			}
			else if (e.getSource() == itemSim)
			{
				new SimGraphWindow(MetaHandler.getSimCalculatorGraphs());
			}
		}
	}

	class ButtonListenerFetch implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			program.showSimilarWindow();
		}
	}

	class SimGraphWindow extends JFrame
	{
		public SimGraphWindow(JPanel[] panels)
		{
			JPanel background = new JPanel();
			add(background);
			background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
			JScrollPane scroll = new JScrollPane(background, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			add(scroll);
			String[] descriptions = MetaHandler.getDescriptions();
			for (int i = 0; i < panels.length; i++)
			{
				background.add(panels[i]);
				panels[i].setPreferredSize(new Dimension(1000, 540));
				background.add(new JLabel(descriptions[i]));
			}
			setSize(1200, 1000);
			setTitle("Similarity - grafer");
			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
	}

}
