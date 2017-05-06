package cbr;

import java.sql.ResultSet;

public class Utility
{
	public static double similarityStanding (ResultSet resultSet, String standing_currentPatient)
	{
		try{
			// Om raden �r tom, returnera similarity 0.0
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
					// Kolla skillnaden i hur l�nge de anv�nd st�hj�lp och addera p� similarityn beroende p� hur lika de �r.
					// TODO: r�kna ut hur m�nga timmar per vecka patienten anv�nder st�hj�lpmedel (DaysPerWeek * HoursPerDay)
					// och j�mf�r detta v�rde f�r b�da patienterna. Detta kommer kr�va fler sliders p� main-windowen f�r DaysPerWeek och HoursPerDay och 
					// forwarding av dessa v�rden genom hela fetchSimilar grejen.
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
