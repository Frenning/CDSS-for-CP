package examination;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class Treatment
{
	private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
	private int id;
	private int answer;
	private String description;
	private Date dateAtTreatment;
	private double ageAtTreatment;

	public Treatment(int id, int answer, String description, String dateString, Date birthDate)
	{
		this.id = id;
		this.answer = answer;
		this.description = description;
		try
		{
			if (dateString.equals("null"))
			{
				this.dateAtTreatment = new Date(); // Todo
			}
			else
			{
				this.dateAtTreatment = dateFormat.parse(dateString);
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		this.ageAtTreatment = Examination.ageDiffToDouble(this.dateAtTreatment, birthDate);
	}

	public double getAgeAtTreatment()
	{
		return this.ageAtTreatment;
	}

	@Override
	public String toString()
	{
		return "Operation vid " + String.format("%.1f", this.ageAtTreatment) + " år: " + this.description + " "
				+ DateFormat.getDateInstance(DateFormat.SHORT).format(dateAtTreatment);
	}
}
