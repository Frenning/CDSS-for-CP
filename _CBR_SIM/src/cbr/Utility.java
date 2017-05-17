package cbr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.spi.DirStateFactory.Result;
import javax.print.attribute.ResolutionSyntax;

import examination.Examination;
import examination.Treatment;

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
					double similarity = 0.2;
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
						similarity += 0.1;
					else if(hoursDifference > 1.0 && hoursDifference <= 3.0)
						similarity += 0.075;
					else if(hoursDifference > 3.0 && hoursDifference <= 5.0)
						similarity += 0.05;
					else if(hoursDifference > 5.0 && hoursDifference <= 10.0)
						similarity += 0.02;
						
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
	
	public static double similaritySurgery (ExaminationHistory currentPatientHistory, ResultSet otherPatientDate, double currentAge, ResultSet treatmentsOther)
	{
		double recentTreatmentAge = getLatestSurgeryAge(currentPatientHistory.getExaminations());
		
		// If no surgeries were done on current patient
		if(recentTreatmentAge == 0)
			return 0.0;
		
		// Calculates how relevant the latest surgery is (age-wise) of the current patient
		Age.similarityFallOff = 4;
		Age.ageWeight = 1.0;
		Age.addBreakPoints(currentAge, "null");
		double simLatestTreatment = Age.calculateAgeSim(recentTreatmentAge);		
		
		double ageClosestDiff = 100;
		double ageClosest = 0;
		double ageOther = 0;
		try
		{		
			otherPatientDate.next();//get date
			while (treatmentsOther.next())
			{				
				int birthyear = treatmentsOther.getInt("birth_year");
				String date = treatmentsOther.getString("date");
				ageOther = Age.getAgeAtExamination(birthyear, otherPatientDate.getString("date"));
				if(birthyear == 0 || date == null)
					continue;
				double ageAtTreatment = Age.getAgeAtExamination(birthyear, date);			
				
				double ageDiff = Math.abs(recentTreatmentAge - ageAtTreatment);
				// The closest surgery by age is found
				if(ageDiff < ageClosestDiff)
				{
					ageClosestDiff = ageDiff;
					ageClosest = ageAtTreatment;
				}
					
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(ageClosest == 0 || ageOther <= currentAge)
			return 0.0;
		
		//Define how much similarity-value the surgeries should have and how much of a difference is accepted (fall-off)
		Age.similarityFallOff = 4;
		Age.ageWeight = 0.2;
		// Compare most recent surgery-age of current patient with closest surgery found (by age) of other patient. 
		// Multiply it by how relevant the latest surgery of current patient is.
		Age.addBreakPoints(recentTreatmentAge, "null");
		double totalSim = simLatestTreatment * Age.calculateAgeSim(ageClosest);
		return totalSim;
	}
	
	public static double getLatestSurgeryAge(Vector <Examination> examinations)
	{
		Vector <Treatment> treatments = new Vector<Treatment>();
		
		for(int i = 0; i < examinations.size(); i++)
		{
			treatments.addAll(examinations.get(i).getTreatments());
		}
		
		// If no treatments were done
		if(treatments.size() == 0)
			return 0;
		
		double biggestAge = 0;
		
		for(int i = 0; i < treatments.size(); i++)
		{
			double tAge = treatments.get(i).getAgeAtTreatment();
			if(tAge > biggestAge)
				biggestAge = tAge;
		}
		
		return biggestAge;
	}
	
}
