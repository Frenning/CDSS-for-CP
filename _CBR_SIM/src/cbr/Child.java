package cbr;

public class Child
{
	public static String GENDER_FEMALE = "kvinna";
	public static String GENDER_MALE = "man";
	public static String GENDER_UNKNOWN = "okänt";
	private int id;
	private int birthYear;
	private String gender;
	private String[] columnValues;
	public static String[] columnDescriptions = { "undersökningsdatum", "symptom", "GMFCS", "plantar hö", "plantar vä",
			"dorsal f hö", "dorsal f vä", "dorsal e hö", "dorsal e vä" };

	public Child(int id, int birthYear, String gender, String[] columnValues)
	{
		this.id = id;
		this.birthYear = birthYear;
		this.gender = gender;
		this.columnValues = columnValues;
	}

	public int getId()
	{
		return this.id;
	}

	public int getBirthYear()
	{
		return birthYear;
	}

	public String getGender()
	{
		return gender;
	}

	public String[] getColumnValues()
	{
		return columnValues;
	}

	@Override
	public String toString()
	{
		return "Child " + this.id + ": " + this.gender + " " + this.birthYear;
	}

}
