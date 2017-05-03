package cbr;

public class Child
{
	public static String GENDER_FEMALE = "kvinna";
	public static String GENDER_MALE = "man";
	public static String GENDER_UNKNOWN = "ok�nt";
	private int id;
	private int birthYear;
	private String gender;
	private String[] columnValues;
	public static String[] columnDescriptions = { "unders�kningsdatum", "symptom", "GMFCS", "plantar h�", "plantar v�",
			"dorsal f h�", "dorsal f v�", "dorsal e h�", "dorsal e v�" };

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
