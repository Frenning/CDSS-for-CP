package cbr;

import java.sql.ResultSet;

public class Utility
{
	public double similarityStanding (ResultSet resultSet)
	{
		try{
			while (resultSet.next())
			{
				resultSet.next();
				// retrieve column from row which is GMFCS value
				String standing_otherPatient = resultSet.getString("UsesHelp");
				
				if(!resultSet.getString("UsesHelp").equals("ja") || !resultSet.getString("UsesHelp").equals("nej")){
					while (resultSet.next() && (!resultSet.getString("UsesHelp").equals("ja") || !resultSet.getString("UsesHelp").equals("nej")))
					{
						standing_otherPatient = resultSet.getString("UsesHelp");
					}
					if (!resultSet.getString("UsesHelp").equals("ja") || !resultSet.getString("UsesHelp").equals("nej"))
						continue;
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
