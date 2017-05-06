package cbr;

import java.sql.ResultSet;

public class Utility
{
	public static double similarityStanding (ResultSet resultSet, String standing_currentPatient)
	{
		try{
			// Om raden är tom, returnera similarity 0.0
			if (!resultSet.next())
			{
				return 0.0;
			}
			else
			{
				String standing_otherPatient = resultSet.getString("UsesHelp");
				
				if(standing_otherPatient != standing_currentPatient)
				{
					return 0.0;
				}
				else
				{
					double similarity = 0.1;
					// Kolla skillnaden i hur länge de använd ståhjälp och addera på similarityn beroende på hur lika de är.
					// TODO: räkna ut hur många timmar per vecka patienten använder ståhjälpmedel (DaysPerWeek * HoursPerDay)
					// och jämför detta värde för båda patienterna. Detta kommer kräva fler sliders på main-windowen för DaysPerWeek och HoursPerDay och 
					// forwarding av dessa värden genom hela fetchSimilar grejen.
					String standingTime_otherPatient = resultSet.getString("DaysPerWeek");
                }

			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		double dub = 0.0;
		
		return dub;
	}
}
