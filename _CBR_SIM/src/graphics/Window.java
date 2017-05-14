package graphics;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import cbr.Age;
import cbr.MetaHandler;
import cbr.PanelData;
import cbr.Program;

public class Window extends JFrame
{
	private Program program;
	private SliderPanel[] panels = new SliderPanel[MetaHandler.getNrOfColumns()];
	private SliderPanel agePanel = new SliderPanel(new PanelData("birth_year", "Ålder", Age.MIN, Age.MAX));
	private SliderPanel gmfcsPanel = new SliderPanel(new PanelData("gmfcs", "GMFCS", 1, 5));
	private JPanel standingBulletsFrame = new JPanel();
	private JPanel standingHoursBulletsFrame = new JPanel();
	private JPanel standingDaysBulletsFrame = new JPanel();
	private JPanel pubertyBulletsFrame = new JPanel();
	private ButtonGroup standingBulletGroup = new ButtonGroup();
	private ButtonGroup standingHoursBulletGroup = new ButtonGroup();
	private ButtonGroup standingDaysBulletGroup = new ButtonGroup();
	private ButtonGroup pubertyBulletGroup = new ButtonGroup();
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
		
		JLabel standingLabel = new JLabel("Använder ståhjälpmedel: ");
		JRadioButton btn1 = new JRadioButton("Ja");
		JRadioButton btn2 = new JRadioButton("Nej");
		standingBulletGroup.add(btn1);
		standingBulletGroup.add(btn2);
		btn2.setSelected(true);
		standingBulletsFrame.add(standingLabel);
		standingBulletsFrame.add(btn1);
		standingBulletsFrame.add(btn2);
		
		JLabel standingHoursLabel = new JLabel("Använder ståhjälpmedel: Antal timmar per dag");
		JRadioButton btn_lt1 = new JRadioButton("<1");
		JRadioButton btn0102 = new JRadioButton("1-2");
		JRadioButton btn0304 = new JRadioButton("3-4");
		JRadioButton btn_gt4 = new JRadioButton(">4");
		standingHoursBulletGroup.add(btn_lt1);
		standingHoursBulletGroup.add(btn0102);
		standingHoursBulletGroup.add(btn0304);
		standingHoursBulletGroup.add(btn_gt4);
		standingHoursBulletsFrame.add(standingHoursLabel);
		standingHoursBulletsFrame.add(btn_lt1);
		standingHoursBulletsFrame.add(btn0102);
		standingHoursBulletsFrame.add(btn0304);
		standingHoursBulletsFrame.add(btn_gt4);
		
		JLabel standingDaysLabel = new JLabel("Använder ståhjälpmedel: Dagar per vecka");
		JRadioButton btn12 = new JRadioButton("1-2");
		JRadioButton btn34 = new JRadioButton("3-4");
		JRadioButton btn56 = new JRadioButton("5-6");
		JRadioButton btn7 = new JRadioButton("7");
		standingDaysBulletGroup.add(btn12);
		standingDaysBulletGroup.add(btn34);
		standingDaysBulletGroup.add(btn56);
		standingDaysBulletGroup.add(btn7);
		standingDaysBulletsFrame.add(standingDaysLabel);
		standingDaysBulletsFrame.add(btn12);
		standingDaysBulletsFrame.add(btn34);
		standingDaysBulletsFrame.add(btn56);
		standingDaysBulletsFrame.add(btn7);
		
		JLabel pubertyLabel = new JLabel("Har kommit in i puberteten: ");
		JRadioButton pubertyBtnYes = new JRadioButton("Ja");
		JRadioButton pubertyBtnNo = new JRadioButton("Nej");
		JRadioButton pubertyBtnDN = new JRadioButton("Vet ej");
		pubertyBulletGroup.add(pubertyBtnYes);
		pubertyBulletGroup.add(pubertyBtnNo);
		pubertyBulletGroup.add(pubertyBtnDN);
		pubertyBulletsFrame.add(pubertyLabel);
		pubertyBulletsFrame.add(pubertyBtnYes);
		pubertyBulletsFrame.add(pubertyBtnNo);
		pubertyBulletsFrame.add(pubertyBtnDN);
		
		add(agePanel);
		add(gmfcsPanel);
		add(standingBulletsFrame);
		add(standingHoursBulletsFrame);
		add(standingDaysBulletsFrame);
		add(pubertyBulletsFrame);
		PanelData[] data = MetaHandler.getPanelData();
		for (int i = 0; i < data.length; i++)
		{
			SliderPanel panel = new SliderPanel(data[i]);
			panels[i] = panel;
			add(panel);
		}
		add(button);
		button.addActionListener(new ButtonListenerShow());
		setSize(new Dimension(800, 900));
		setTitle("Decision support - Din patient");
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	class ButtonListenerShow implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(standingBulletGroup.getSelection() == null || standingHoursBulletGroup == null || standingDaysBulletGroup == null)
				return;
			int[] values = new int[panels.length];
			for (int i = 0; i < panels.length; i++)
			{
				values[i] = panels[i].getSliderValue();
			}
			int age = agePanel.getSliderValue();
			
			
			String [] standingInformation = new String [3];
			String puberty = new String();
			
	        for (Enumeration<AbstractButton>buttons = standingBulletGroup.getElements(); buttons.hasMoreElements();) {
	            AbstractButton button = buttons.nextElement();

	            if (button.isSelected()) {
	            	standingInformation[0] = button.getText();
	            }
	        }
	        
	        for (Enumeration<AbstractButton>buttons = standingDaysBulletGroup.getElements(); buttons.hasMoreElements();) {
	            AbstractButton button = buttons.nextElement();

	            if (button.isSelected()) {
	            	standingInformation[1] = button.getText();
	            }
	        }
	        
	        for (Enumeration<AbstractButton>buttons = standingHoursBulletGroup.getElements(); buttons.hasMoreElements();) {
	            AbstractButton button = buttons.nextElement();

	            if (button.isSelected()) {
	            	standingInformation[2] = button.getText();
	            }
	        }
	        
	        for (Enumeration<AbstractButton>buttons = pubertyBulletGroup.getElements(); buttons.hasMoreElements();) {
	            AbstractButton button = buttons.nextElement();

	            if (button.isSelected()) {
	            	puberty = button.getText();
	            }
	        }
			
			program.fetchSimilar(values, age, gmfcsPanel.getSliderValue(), standingInformation, puberty);
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
