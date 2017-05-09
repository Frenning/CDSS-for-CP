package cbr;

import java.sql.ResultSet;

public class Utility
{
	//standingInformation_currentPatient: [0] = UsesHelp, [1] = DaysPerWeek, [2] = HoursPerDay
	public static double similarityStanding (String [] otherPatient, String [] curPatient)
	{
		try{
			// Om raden är tom, returnera similarity 0.0
			if (otherPatient == null)
			{
				return 0.0;
			}
			else
			{
				// Om det inte stämmer överens mellan patienterna om de använder ståhjälpmedel
				if(!otherPatient[0].equals(curPatient[0]))
				{
					return 0.0;
				}
				else
				{
					// Vikten för hur mkt det spelar roll om båda patienterna använder ståhjälpmedel
					double similarity = 0.1;
					// Kolla skillnaden i hur länge de använd ståhjälp och addera på similarityn beroende på hur lika de är.
					
					//Check so that no values are null, and if they are then don't add any more similarity.
					if(otherPatient[1] == null || otherPatient[2] == null || 
							curPatient[1] == null || curPatient[2] == null)
						return similarity;
					
					// Get the hours per week for each patient
					double currentHrsPerWeek = calculateHoursPerWeek(curPatient[1], curPatient[2]);
					double otherHrsPerWeek = calculateHoursPerWeek(otherPatient[1], otherPatient[2]);
					
					double hoursDifference = Math.abs(currentHrsPerWeek - otherHrsPerWeek);
					
					// Add to the similarity value a number depending on the hours per week difference between the patients
					if(hoursDifference <= 1.0)
						similarity += 0.25;
					else if(hoursDifference > 1.0 && hoursDifference <= 3.0)
						similarity += 0.2;
					else if(hoursDifference > 3.0 && hoursDifference <= 5.0)
						similarity += 0.1;
					else if(hoursDifference > 5.0 && hoursDifference <= 10.0)
						similarity += 0.05;
						
					return similarity;
                }
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		
		return 0.0;
	}
	
	private static double calculateHoursPerWeek (String days, String hours)
	{
		double hoursConverted = 0.0;
		switch (hours)
		{
		case "<1":
			hoursConverted = 0.5;
			break;
		case "01-02":
		case "1-2":
			hoursConverted = 1.5;
			break;
		case "03-04":
		case "3-4":
			hoursConverted = 3.5;
			break;
		case ">4":
			hoursConverted =  5.0;
			break;
		}
		
		double daysConverted = 0.0;
		switch (days)
		{
		case "01-02":
		case "1-2":
			daysConverted = 1.5;
			break;
		case "03-04":
		case "3-4":
			daysConverted = 3.5;
			break;
		case "05-06":
		case "5-6":
			daysConverted = 5.5;
			break;
		case "7":
			daysConverted =  7.0;
			break;
		}
		
		return daysConverted * hoursConverted;
		
	}
	
	
}
