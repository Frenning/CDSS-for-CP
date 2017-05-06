package cbr;

public class PanelData
{
	private String columnName;
	private String description;
	private int min = 0;
	private int max = 0;
	
	public String option1;
	public String option2;

	public PanelData(String columnName, String description, int min, int max)
	{
		this.columnName = columnName;
		this.description = description;
		this.min = min;
		this.max = max;
	}
	
	public PanelData(String columnName, String description, String option1, String option2)
	{
		this.columnName = columnName;
		this.description = description;
		this.option1 = option1;
		this.option2 = option2;
	}

	public String getColumnName()
	{
		return this.columnName;
	}

	public String getName()
	{
		return this.description;
	}

	public String getDescription()
	{
		return this.description;
	}

	public int getMin()
	{
		return this.min;
	}

	public int getMax()
	{
		return this.max;
	}

	public double getNormaizedDiff(int diff)
	{
		int range = max - min;
		double percent;
		if (diff == 0)
			percent = 0;
		else
			percent = (double) diff / range;
		return 1 - percent;
	}

}
