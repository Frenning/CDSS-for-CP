package cbr;

public class ValueData
{
	private int[] sliderValues = new int[MetaHandler.getNrOfColumns()];
	private int age;

	public ValueData(int[] sliderValues, int age)
	{
		this.sliderValues = sliderValues;
		this.age = age;
	}

	public int[] getSliderValues()
	{
		return sliderValues;
	}

	public int getAge()
	{
		return age;
	}

	public String[] getValues()
	{
		String[] data = new String[sliderValues.length + 1];
		data[0] = "" + age;
		for (int i = 0; i < sliderValues.length; i++)
		{
			data[i + 1] = "" + sliderValues[i];
		}
		return data;
	}

}
