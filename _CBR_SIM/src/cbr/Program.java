package cbr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;

import examination.Boutolium;
import examination.Examination;
import examination.Orthosis;
import examination.OrthosisContracture;
import examination.OrthosisFunction;
import examination.Treatment;
import graphics.ExistingPatientsWindow;
import graphics.ResultWindow;
import graphics.Window;
import similarity.Breakpoint;
import similarity.SimCalculator;
import similarity.SimilarityHistory;
import similarity.SimilarityHistoryComplete;

public class Program
{
	private static int HISTORY_SIZE = 10;
	private static int MIN_NR_OF_EXAMINATIONS_AFTER = 2;
	private Connection connection = null;
	

	public Program()
	{
		MetaHandler.init();
		
		Age.maxAgeDiffCalculator.addBreakpoint(new Breakpoint(4, 0.5));
		Age.maxAgeDiffCalculator.addBreakpoint(new Breakpoint(5, 1));
		Age.maxAgeDiffCalculator.addBreakpoint(new Breakpoint(10, 1));
		Age.maxAgeDiffCalculator.addBreakpoint(new Breakpoint(12, 1.5));
		Age.maxAgeDiffCalculator.addBreakpoint(new Breakpoint(16, 1.5));
		Age.maxAgeDiffCalculator.addBreakpoint(new Breakpoint(20, 2));
		Age.maxAgeDiffCalculator.addBreakpoint(new Breakpoint(25, 3));
		
		this.connect();
		new Window(this);
	}

	public void fetchSimilar(int[] values, double age, int GMFCS, String [] standingInformation, String puberty, Boolean usesAngles)
	{
		long start = System.nanoTime();
		System.out.println(System.nanoTime());
		ExaminationHistory currentPatientHistory = new ExaminationHistory(0, 2016 - (int)age, 0);
		currentPatientHistory.addExamination(new Examination(values, age));
		this.fetchSimilar(values, age, currentPatientHistory, GMFCS, standingInformation, puberty, usesAngles);
		long timing = System.nanoTime() - start;
		timing /= 1000;
		System.out.println(timing + " millisekunder");
	}

	public void fetchSimilar(int[] values, double age, ExaminationHistory currentPatientHistory, int GMFCS_currentPatient, String [] standingInformation, String puberty, Boolean usesAngles)
	{
		String query = "select " + MetaHandler.getColumnNamesCommaSeparated();
		Vector<Similarity> similar = this.fetchMostSimilar(values, age, query, GMFCS_currentPatient, standingInformation, currentPatientHistory, puberty, usesAngles);
		Vector<ExaminationHistory> histories = this.getDetailedInfo(similar);
		new ResultWindow(histories, currentPatientHistory, values, age, GMFCS_currentPatient, standingInformation);
	}

	private ExaminationHistory getChildsExaminationsHistory(int childId)
	{
		try
		{
			ResultSet result = this.fetchResult("select * from child where id = " + childId);
			result.next();
			ExaminationHistory history = new ExaminationHistory(result.getInt(1), result.getInt(2), result.getInt(3));		
			
			String query = "select " + MetaHandler.getColumnNamesCommaSeparated()
			+ " examination.date, examination.id from child, examination where examination.child = child.id and child.id = "
			+ childId + " order by examination.date";
			
			result = this.fetchResult(query);
			while (result.next())
			{
				Examination examination = new Examination(result, history.getBirthDate());
				int examinationId = result.getInt(MetaHandler.getNrOfColumns() + 2);
				ResultSet treatments = this.fetchResult("select * from treatment where examination = " + examinationId);
				while (treatments.next())
				{
					String dateAtTreatment;
					if (treatments.getString(4) == null)
					{
						dateAtTreatment = "1980-01-01"; // Todo
					}
					else
					{
						dateAtTreatment = treatments.getString(4);
					}
					examination.addTreatment(new Treatment(treatments.getInt(1), treatments.getInt(2),
							treatments.getString(3), dateAtTreatment, history.getBirthDate()));
				}
				ResultSet boutoliums = this.fetchResult("select * from boutolium where examination = " + examinationId);
				while (boutoliums.next())
				{
					examination.addBoutolium(new Boutolium(boutoliums.getInt(1), boutoliums.getString(2),
							boutoliums.getString(3), history.getBirthDate()));
				}
				ResultSet orthosis = this
						.fetchResult("select * from orthosis_contracture_AFO where examination = " + examinationId);
				while (orthosis.next())
				{
					examination.addOrthosis(new OrthosisContracture(orthosis.getInt(1), Orthosis.AFO,
							orthosis.getInt(2), orthosis.getInt(3), orthosis.getString(4)));
				}
				orthosis = this
						.fetchResult("select * from orthosis_contracture_KAFO where examination = " + examinationId);
				while (orthosis.next())
				{
					examination.addOrthosis(new OrthosisContracture(orthosis.getInt(1), Orthosis.KAFO,
							orthosis.getInt(2), orthosis.getInt(3), orthosis.getString(4)));
				}
				orthosis = this.fetchResult("select * from orthosis_function_AFO where examination = " + examinationId);
				while (orthosis.next())
				{
					examination.addOrthosis(new OrthosisFunction(orthosis.getInt(1), Orthosis.AFO, orthosis.getInt(2),
							orthosis.getInt(3), // id, type right, left
							orthosis.getInt(4), orthosis.getInt(5), orthosis.getInt(6), // goalGait,
																						// goalBalance,
																						// goalExercise
							orthosis.getString(7), orthosis.getString(8), orthosis.getString(9)) // goal{Gait/Balance/Exercise}Achieved
					);
				}
				orthosis = this
						.fetchResult("select * from orthosis_function_KAFO where examination = " + examinationId);
				while (orthosis.next())
				{
					examination.addOrthosis(new OrthosisFunction(orthosis.getInt(1), Orthosis.KAFO, orthosis.getInt(2),
							orthosis.getInt(3), // id, type right, left
							orthosis.getInt(4), orthosis.getInt(5), orthosis.getInt(6), // goalGait,
																						// goalBalance,
																						// goalExercise
							orthosis.getString(7), orthosis.getString(8), orthosis.getString(9)) // goal{Gait/Balance/Exercise}Achieved
					);
				}
				// if you find a examination where GMFCS is null you put the
				// GMFCS from later examinations as that value
				ResultSet GMFCS_result = this.fetchResult(
						"select gmfcs from child, examination where examination.child = child.id and child.id = "
								+ childId);
				GMFCS_result.next();
				int GMFCS = GMFCS_result.getInt(1);

				if (GMFCS < 1 || GMFCS > 5)
				{
					while (GMFCS_result.next() && (GMFCS < 1 || GMFCS > 5))
					{
						GMFCS = GMFCS_result.getInt(1);
					}
				}

				examination.GMFCS = GMFCS;
			
				ResultSet standingInformation = this.fetchResult("select * from standing where examinationID = " + examinationId);
				
				if(standingInformation.next())
				{
					if( standingInformation.getString("UsesHelp") == null)
						examination.standingInformation[0] = null;
					else 
						examination.standingInformation[0] = standingInformation.getString("UsesHelp");
						
					if( standingInformation.getString("DaysPerWeek") == null)
						examination.standingInformation[1] = null;
					else 
						examination.standingInformation[1] = standingInformation.getString("DaysPerWeek");
						
					if( standingInformation.getString("HoursPerDay") == null)
						examination.standingInformation[2] = null;
					else
						examination.standingInformation[2] = standingInformation.getString("HoursPerDay");
				}
				else
				{
					examination.standingInformation[0] = null;
					examination.standingInformation[1] = null;
					examination.standingInformation[2] = null;
				}
				
				
				
				
				

				history.addExamination(examination);
			}
			return history;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private Vector<ExaminationHistory> getDetailedInfo(Vector<Similarity> similarities)
	{
		Vector<ExaminationHistory> histories = new Vector<ExaminationHistory>();
		Vector<Integer> childrenInHistories = new Vector<Integer>();
		for (Similarity similarity : similarities)
		{
			if (histories.size() >= HISTORY_SIZE)
			{
				break;
			}
			if (!childrenInHistories.contains(similarity.getId()))
			{
				if (this.historyIsRelevant(similarity))
				{
					try
					{
						ExaminationHistory history = this.getChildsExaminationsHistory(similarity.getId());
						history.addSimilarityHistory(similarity.getSimilarityHistory());
						for (Examination examination : history.getExaminations())
						{
							if (similarity.getExaminationDate().equals(examination.getDateOriginal()))
							{
								history.setRelevant(examination);
							}
						}
						histories.add(history);
						childrenInHistories.add(similarity.getId());
					}
					catch (Exception e)
					{
						System.out.println(e);
					}
				}
			}
		}
		return histories;
	}

	private boolean historyIsRelevant(Similarity similarity)
	{
		ResultSet result = this
				.fetchResult(String.format("select count(*) from examination where child = %d and date > '%s'",
						similarity.getId(), similarity.getExaminationDate()));
		try
		{
			result.next();
			return result.getInt(1) >= MIN_NR_OF_EXAMINATIONS_AFTER;
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		return false;
	}

	// Fetches all examinations and calculates similarity to the current child's
	// values
	// Returns a list with the examinations that are most similar

	private Vector<Similarity> fetchMostSimilar(int[] values, double age, String query, int GMFCS_currentPatient, String [] standingInformation, ExaminationHistory currentPatientHistory, String puberty, Boolean usesAngles)
	{
		// getNrOfCol är lines i meta.csv
		Vector<Similarity> similarities = new Vector<Similarity>();
		// Check so that the CMFCS-level is matching
		query += "child, birth_year, examination.date, examination.id, gmfcs, examinationID, UsesHelp, DaysPerWeek, HoursPerDay "
				+ "from child, examination, standing "
				+ "where examination.child = child.id and examinationID = examination.id and gmfcs = " + GMFCS_currentPatient;
		ResultSet result = this.fetchResult(query.toString());

		try
		{
			int nrOfColumns = MetaHandler.getNrOfColumns();
			
			//Add breakpoints needed to calculate similarity for current age
			Age.addBreakPoints(age, "null");
		
			while(result.next())
			{
				int birthYear = result.getInt(nrOfColumns + 2);
				String examinationDate = result.getString(nrOfColumns + 3);
				double ageAtExamination = Age.getAgeAtExamination(birthYear, examinationDate);
				
				// Only care about examinations where age difference is not too
				// large
				
				// Calculate age similarity
				Age.similarityFallOff = 2;
				Age.ageWeight = 1;
				Age.addBreakPoints(age, "null");
				double similarity = Age.calculateAgeSim(ageAtExamination);
				int childId = result.getInt(nrOfColumns + 1);
				SimilarityHistoryComplete simHistory = new SimilarityHistoryComplete(childId);
				double ageSimilarity = similarity;
				
				String [] otherStandingInfo = new String [3];
				otherStandingInfo[0]=result.getString("UsesHelp");
				otherStandingInfo[1]=result.getString("DaysPerWeek");
				otherStandingInfo[2]=result.getString("HoursPerDay");
				
				// Add standing similarity to total similarity
				double standingSimilarity = Utility.similarityStanding(otherStandingInfo, standingInformation);
				similarity += standingSimilarity;
				
				//If getting patient database child id will be other than 0 and we can match operations
				if(currentPatientHistory.getChild() != 0)
				{
					ResultSet treatments = this.fetchResult("SELECT birth_year, treatment.date, examination.id FROM examination, treatment, child "
															+ "where child = " + childId + " and treatment.examination = examination.id and child.id = examination.child "
															+ "order by treatment.date");
					similarity += Utility.similaritySurgery(currentPatientHistory, age, treatments);
				}
				
				simHistory.addHistory(new SimilarityHistory("ålder", age, ageAtExamination, ageSimilarity, ageSimilarity));
				simHistory.addHistory(new SimilarityHistory("Help with standing", 0, 0, standingSimilarity, standingSimilarity));
				
				for (int i = 0; i < nrOfColumns; i++)
				{
					String valuePrep = result.getString(i + 1);
					int value;
					if (valuePrep == null)
					{ // Data is missing. Use rounded average. Todo:
						// Inform
						// user that average is used
						value = MetaHandler.getAverage(i);
					}
					else
					{
						value = result.getInt(i + 1);
					}
					double columnSimilarity = MetaHandler.calculateSimilarity(i, values[i], value);
					double weight = MetaHandler.getWeight(i);
					double theSim = 0;
					if(usesAngles)
						theSim = columnSimilarity * weight;
					simHistory.addHistory(new SimilarityHistory(MetaHandler.getColumnsName(i), values[i], value,
							columnSimilarity, theSim));
					similarity += theSim;
				}
				simHistory.setTotalSimilarity(similarity);
				similarities.add(new Similarity(result.getInt(nrOfColumns + 1), similarity, examinationDate,
						simHistory));
			} 
			
			Collections.sort(similarities);
			return similarities;
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	private void connect()
	{
		try
		{
			Properties properties = new Properties();
			properties.put("user", "root");
			properties.put("password", "123");
			int port = 3306; // Default?
			String server = "localhost"; // ?
			String database = "anna_test";
			this.connection = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + database,
					properties);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	private ResultSet fetchResult(String query)
	{
		try
		{

			Statement statement = connection.createStatement();
			statement.executeQuery(query);
			return statement.getResultSet();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			;
		}
		return null;
	}

	public void showSimilarWindow(Boolean usesAngles)
	{
		Vector<Child> children = new Vector<Child>();
		ResultSet result = this.fetchResult(
				"select * from child left join examination on child.id = examination.child left join symptom on examination.symptom = symptom.id order by child.id, examination.date desc");
		try
		{
			int lastChild = -1;
			while (result.next())
			{
				int id = result.getInt(1);
				if (id != lastChild)
				{
					int birthYear = result.getInt(2);
					String gender;
					if (result.getString(3) == null)
					{
						gender = Child.GENDER_UNKNOWN;
					}
					else if (result.getString(3).equals("1"))
					{
						gender = Child.GENDER_FEMALE;
					}
					else if (result.getString(3).equals("0"))
					{
						gender = Child.GENDER_MALE;
					}
					else
					{
						gender = Child.GENDER_UNKNOWN;
					}
					String symptom = result.getString(16);
					String gmfcs = result.getString(14);
					String examinationDate = result.getString(6);
					String[] columnValues = { examinationDate, symptom, gmfcs, result.getString(8), result.getString(9),
							result.getString(10), result.getString(11), result.getString(12), result.getString(13) };
					children.add(new Child(id, birthYear, gender, columnValues));
					lastChild = id;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		new ExistingPatientsWindow(this, children, usesAngles);
	}

	// Fetch the values from the latest examination of a certain child
	// Then call this.fetchSimilar to show similar patients
	public void fetchValuesFromDatabase(Child child, Boolean usesAngles)
	{
		try
		{
			int nrOfColumns = MetaHandler.getNrOfColumns();
			String query = "select " + MetaHandler.getColumnNamesCommaSeparated()
					+ "date, birth_year, gmfcs, UsesHelp, DaysPerWeek, HoursPerDay from examination, child, standing where child = " + child.getId()
					+ " and examination.child = child.id order by date desc limit 1";
			ResultSet result = this.fetchResult(query.toString());
			result.next();
			int[] values = new int[MetaHandler.getNrOfColumns()];
			for (int i = 0; i < nrOfColumns; i++)
			{
				String value = result.getString(i + 1);
				if (value == null)
				{ // When the value is null, data is missing. Take the rounded
					// average. TODO: Show that the average is used.
					values[i] = MetaHandler.getAverage(i);
				}
				else
				{
					values[i] = Integer.parseInt(value);
				}
			}
			
			String [] standingInformation = new String [3];
			standingInformation[0] = result.getString(nrOfColumns + 4);
			standingInformation[1] = result.getString(nrOfColumns + 5);
			standingInformation[2] = result.getString(nrOfColumns + 6);
			
			Date examinationDate = Examination.sqlStringToDate(result.getString(nrOfColumns + 1));
			Date birthDate = Examination.birthYearToDate(result.getInt(nrOfColumns + 2));
			double ageAtExamination = Examination.ageDiffToDouble(examinationDate, birthDate);
			this.fetchSimilar(values, ageAtExamination, this.getChildsExaminationsHistory(child.getId()),
					result.getInt(nrOfColumns + 3), standingInformation, "null", usesAngles);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		new Program();
	}

}
