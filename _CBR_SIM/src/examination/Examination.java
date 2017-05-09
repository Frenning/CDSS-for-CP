package examination;

import java.awt.Color;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import cbr.Dorsal;
import cbr.MetaHandler;

public class Examination
{
	private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
	private String dateOriginal;
	private Date date;
	private int[] plantar = new int[2];
	private Dorsal dorsal;
	private double ageAtExamination;
	public int GMFCS;
	public String [] standingInformation = new String[3];
	private Vector<Treatment> treatments = new Vector<Treatment>();
	private Vector<Boutolium> boutoliums = new Vector<Boutolium>();
	private Vector<Orthosis> orthosis = new Vector<Orthosis>();

	private boolean isRelevant = false;

	public Examination(ResultSet result, Date birthDate)
	{
		try
		{
			this.plantar[0] = result.getInt(1);
			this.plantar[1] = result.getInt(2);
			this.dorsal = new Dorsal(result);
			int offset = MetaHandler.getNrOfColumns();
			this.dateOriginal = result.getString(offset + 1);
			this.date = this.dateFormat.parse(dateOriginal);
			this.ageAtExamination = ageDiffToDouble(date, birthDate);
			
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	public Examination(int[] values, double age)
	{
		this.plantar[0] = values[0];
		this.plantar[1] = values[1];
		this.dorsal = new Dorsal(values[2], values[3], values[4], values[5]);
		this.ageAtExamination = age;
		this.date = new Date();
	}

	public void addOrthosis(Orthosis o)
	{
		this.orthosis.add(o);
	}

	public boolean hasOrthosis()
	{
		return this.orthosis.size() > 0;
	}

	public Vector<Orthosis> getOrthosis()
	{
		return this.orthosis;
	}

	public void addBoutolium(Boutolium boutolium)
	{
		this.boutoliums.add(boutolium);
	}

	public boolean hasBoutolium()
	{
		return this.boutoliums.size() > 0;
	}

	public Vector<Boutolium> getBoutoliums()
	{
		return this.boutoliums;
	}

	public void addTreatment(Treatment treatment)
	{
		treatments.add(treatment);
	}

	public boolean hasTreatment()
	{
		return treatments.size() > 0;
	}

	public Vector<Treatment> getTreatments()
	{
		return treatments;
	}

	public Dorsal getDorsal()
	{
		return dorsal;
	}

	public String getDateOriginal()
	{
		return this.dateOriginal;
	}

	public Date getDate()
	{
		return this.date;
	}

	public static Date sqlStringToDate(String date)
	{
		try
		{
			return dateFormat.parse(date);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			return new Date();
		}
	}

	// Fake month and date
	public static Date birthYearToDate(int year)
	{
		try
		{
			return dateFormat.parse(year + "-01-01");
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			return new Date();
		}
	}

	public static double ageDiffToDouble(Date end, Date start)
	{
		/*
		 * long diff = end.getTime() - start.getTime(); diff /=
		 * (1000*60*60*24*365); int years = (int) (diff); years /= 22;
		 */
		double testDiff = end.getTime() - start.getTime();
		testDiff /= (1000 * 60 * 60 * 24 * 365);
		testDiff /= 22;
		return testDiff;
	}

	public static Color getColor(int i)
	{
		i -= 3;
		if (i >= 0 && i < 4)
		{
			return Dorsal.getDorsalColor(i);
		}
		return Color.gray;
	}

	public void setRelevant()
	{
		this.isRelevant = true;
	}

	public boolean isRelevant()
	{
		return this.isRelevant;
	}

	public double getAgeAtExamination()
	{
		return this.ageAtExamination;
	}

	public int getChildId()
	{
		return 1;
		// return childId;
	}

	public String[] getValues()
	{
		String[] values = new String[1 + plantar.length + dorsal.getDorsal().length];
		values[0] = "" + getAgeAtExamination();
		values[1] = "" + this.plantar[0];
		values[2] = "" + this.plantar[1];
		int[] dorsals = dorsal.getDorsal();
		values[3] = "" + dorsals[0];
		values[4] = "" + dorsals[1];
		values[5] = "" + dorsals[2];
		values[6] = "" + dorsals[3];
		return values;
	}

}
