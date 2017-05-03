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
import graphics.FetchSimilarWindow;
import graphics.ResultWindow;
import graphics.Window;
import similarity.SimilarityHistory;
import similarity.SimilarityHistoryComplete;

public class Program {
	private static int HISTORY_SIZE = 10;
	private static int MIN_NR_OF_EXAMINATIONS_AFTER = 2;
	private Connection connection = null;

	public Program() {
		MetaHandler.init();
		this.connect();
		new Window(this);
	}
	
	public void fetchSimilar(ValueData data) {
		long start = System.nanoTime();
		System.out.println(System.nanoTime());
		ExaminationHistory currentPatientHistory = new ExaminationHistory(0, 2016-data.getAge(), 0);
		currentPatientHistory.addExamination(new Examination(data));
		this.fetchSimilar(data, currentPatientHistory);
		long timing = System.nanoTime() - start;
		timing /= 1000;
		System.out.println(timing + " millisekunder");
	}
	
	public void fetchSimilar(ValueData data, ExaminationHistory currentPatientHistory) {
		String query = "select " + MetaHandler.getColumnNamesCommaSeparated();
		Vector<Similarity> similar = this.fetchMostSimilar(data, query);
		Vector<ExaminationHistory> histories = this.getDetailedInfo(similar);
		new ResultWindow(histories, currentPatientHistory, data);	
	}
	
	private ExaminationHistory getChildsExaminationsHistory(int childId) {
		String query = "select " + MetaHandler.getColumnNamesCommaSeparated() + " examination.date, examination.id from child, examination where examination.child = child.id and child.id = " +  childId + " order by examination.date";
		try {
			ResultSet result = this.fetchResult("select * from child where id = " + childId);
			result.next();
			ExaminationHistory history = new ExaminationHistory(result.getInt(1), result.getInt(2), result.getInt(3));
			result = this.fetchResult(query);
			while(result.next()) {
				Examination examination = new Examination(result, history.getBirthDate());
				int examinationId = result.getInt(MetaHandler.getNrOfColumns()+2);
				ResultSet treatments = this.fetchResult("select * from treatment where examination = " + examinationId);
				while (treatments.next()) {
					String dateAtTreatment;
					if (treatments.getString(4) == null) {
						dateAtTreatment = "1980-01-01";	// Todo
					}
					else {
						dateAtTreatment = treatments.getString(4);
					}
					examination.addTreatment( new Treatment(treatments.getInt(1), treatments.getInt(2), treatments.getString(3), dateAtTreatment, history.getBirthDate()) );
				}	
				ResultSet boutoliums = this.fetchResult("select * from boutolium where examination = " + examinationId);
				while (boutoliums.next()) {
					examination.addBoutolium( new Boutolium(boutoliums.getInt(1), boutoliums.getString(2), boutoliums.getString(3), history.getBirthDate()) );
				}
				ResultSet orthosis = this.fetchResult("select * from orthosis_contracture_AFO where examination = "+ examinationId);
				while (orthosis.next()) {
					examination.addOrthosis( new OrthosisContracture(orthosis.getInt(1), Orthosis.AFO, orthosis.getInt(2), orthosis.getInt(3), orthosis.getString(4)) );
				}
				orthosis = this.fetchResult("select * from orthosis_contracture_KAFO where examination = "+ examinationId);
				while (orthosis.next()) {
					examination.addOrthosis( new OrthosisContracture(orthosis.getInt(1), Orthosis.KAFO, orthosis.getInt(2), orthosis.getInt(3), orthosis.getString(4)) );
				}
				orthosis = this.fetchResult("select * from orthosis_function_AFO where examination = " + examinationId);
				while (orthosis.next()) {
					examination.addOrthosis( 
							new OrthosisFunction( orthosis.getInt(1), Orthosis.AFO, orthosis.getInt(2), orthosis.getInt(3), // id, type right, left
								orthosis.getInt(4), orthosis.getInt(5), orthosis.getInt(6),	// goalGait, goalBalance, goalExercise
								orthosis.getString(7), orthosis.getString(8), orthosis.getString(9) ) // goal{Gait/Balance/Exercise}Achieved
						);
				}	
				orthosis = this.fetchResult("select * from orthosis_function_KAFO where examination = " + examinationId);
				while (orthosis.next()) {
					examination.addOrthosis( 
							new OrthosisFunction( orthosis.getInt(1), Orthosis.KAFO, orthosis.getInt(2), orthosis.getInt(3), // id, type right, left
								orthosis.getInt(4), orthosis.getInt(5), orthosis.getInt(6),	// goalGait, goalBalance, goalExercise
								orthosis.getString(7), orthosis.getString(8), orthosis.getString(9) ) // goal{Gait/Balance/Exercise}Achieved
						);
				}
				history.addExamination(examination);
			}
			return history;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Vector<ExaminationHistory> getDetailedInfo(Vector<Similarity> similarities) {
		Vector<ExaminationHistory> histories = new Vector<ExaminationHistory>();
		Vector<Integer> childrenInHistories = new Vector<Integer>();
		for (Similarity similarity : similarities) {
			if (histories.size() >= HISTORY_SIZE) {
				break;
			}			
			if (!childrenInHistories.contains(similarity.getId())) {
				if (this.historyIsRelevant(similarity)) {
					try {
						ExaminationHistory history = this.getChildsExaminationsHistory(similarity.getId());
						history.addSimilarityHistory(similarity.getSimilarityHistory());
						for (Examination examination : history.getExaminations()) {
							if (similarity.getExaminationDate().equals(examination.getDateOriginal())) {
								history.setRelevant(examination);
							}							
						}
						histories.add(history);
						childrenInHistories.add(similarity.getId());					
					}
					catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		}		
		return histories;
	}
	
	private boolean historyIsRelevant(Similarity similarity) {
		ResultSet result = this.fetchResult(String.format("select count(*) from examination where child = %d and date > '%s'", similarity.getId(), similarity.getExaminationDate()));
		try {
			result.next();
			return result.getInt(1) >= MIN_NR_OF_EXAMINATIONS_AFTER;
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	// Fetches all examinations and calculates similarity to the current child's values
	// Returns a list with the examinations that are most similar
	// Todo filtrera bort dem där åldern är fel
	private Vector<Similarity> fetchMostSimilar(ValueData data, String query) {
		int[] values = data.getSliderValues();
		Vector<Similarity> similarities = new Vector<Similarity>();
		query += "child, birth_year, examination.date from child, examination where examination.child = child.id";	
		ResultSet result = this.fetchResult(query.toString());
		int age = data.getAge();
		try {
			int nrOfColumns = MetaHandler.getNrOfColumns();
			while(result.next()) {
				int birthYear = result.getInt(nrOfColumns+2);
				String examinationDate = result.getString(nrOfColumns+3);
				double ageAtExamination = Age.getAgeAtExamination(birthYear, examinationDate);
				double ageSimilarity = Age.calculateSimilarity(ageAtExamination, age);
				// Only care about examinations where age difference is not too large
				if (ageSimilarity > 0) {
					double similarity = 0;
					int childId = result.getInt(nrOfColumns+1);
					SimilarityHistoryComplete simHistory = new SimilarityHistoryComplete(childId);
					double ageWeight = Age.getWeight();
					double ageTotal = ageSimilarity * ageWeight;
//					similarity += ageSimilarity;
					similarity += ageTotal;
					simHistory.addHistory( new SimilarityHistory("ålder", age, ageAtExamination, ageSimilarity, ageWeight, ageTotal) );
					for (int i=0; i<nrOfColumns; i++) {	
						String valuePrep = result.getString(i+1);
						int value;
						if (valuePrep == null) {	// Data is missing. Use rounded average. Todo: Inform user that average is used
							value = MetaHandler.getAverage(i);
						}
						else {
							value = result.getInt(i+1);					
						}
						double columnSimilarity = MetaHandler.calculateWeight(i, values[i], value);
						double weight = MetaHandler.getWeight(i);
						double theSim = columnSimilarity * weight;
						simHistory.addHistory( new SimilarityHistory(MetaHandler.getColumnsName(i), values[i], value, columnSimilarity, weight, theSim) );
						similarity += theSim;					
					}
					simHistory.setTotalSimilarity(similarity);
					similarities.add(new Similarity(result.getInt(nrOfColumns+1), similarity, examinationDate, simHistory));					
				}
			}
			Collections.sort(similarities);
			return similarities;
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	private void connect() {
		try {
			Properties properties = new Properties();
			properties.put("user", "root");
			properties.put("password", "multilate94");
			int port = 3306;	// Default?
			String server = "localhost"; // ?
			String database = "anna_test";
			this.connection = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + database, properties);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private ResultSet fetchResult(String query) {
		try {
			
			Statement statement = connection.createStatement();
			statement.executeQuery(query);
			return statement.getResultSet();
		}
		catch (Exception e) {
			e.printStackTrace();;
		}
		return null;
	}
	
	public void showSimilarWindow() {
		Vector<Child> children = new Vector<Child>();
		ResultSet result = this.fetchResult("select * from child left join examination on child.id = examination.child left join symptom on examination.symptom = symptom.id order by child.id, examination.date desc");
		try {
			int lastChild = -1;
			while (result.next()) {
				int id = result.getInt(1);
				if (id != lastChild) {
					int birthYear = result.getInt(2);
					String gender;
					if (result.getString(3) == null) {
						gender = Child.GENDER_UNKNOWN;
					}
					else if (result.getString(3).equals("1")) {
						gender = Child.GENDER_FEMALE;
					}
					else if (result.getString(3).equals("0")) {
						gender = Child.GENDER_MALE;
					}
					else {
						gender = Child.GENDER_UNKNOWN;
					}
					String symptom = result.getString(16);
					String gmfcs = result.getString(14);
					String examinationDate = result.getString(6);
					String[] columnValues = {examinationDate, symptom, gmfcs, result.getString(8), result.getString(9), result.getString(10), result.getString(11), result.getString(12), result.getString(13)};
					children.add( new Child(id, birthYear, gender, columnValues) );
					lastChild = id;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		new FetchSimilarWindow(this, children);
	}

	// Fetch the values from the latest examination of a certain child
	// Then call this.fetchSimilar to show similar patients
	public void fetchValuesFromDatabase(Child child) {
		try {
			int nrOfColumns = MetaHandler.getNrOfColumns();
			String query = "select " + MetaHandler.getColumnNamesCommaSeparated() + "date, birth_year from examination, child where child = " + child.getId() + " and examination.child = child.id order by date desc limit 1";
			ResultSet result = this.fetchResult(query.toString());
			result.next();
			int[] values = new int[MetaHandler.getNrOfColumns()]; 
			for (int i=0; i<nrOfColumns; i++) {
				String value = result.getString(i+1);
				if (value == null) {	// When the value is null, data is missing. Take the rounded average. TODO: Show that the average is used.
					values[i] = MetaHandler.getAverage(i);
				}
				else {
					values[i] = Integer.parseInt(value);
				}
			}
			Date examinationDate = Examination.sqlStringToDate(result.getString(nrOfColumns+1));
			Date birthDate = Examination.birthYearToDate(result.getInt(nrOfColumns+2));
			double ageAtExamination = Examination.ageDiffToDouble(examinationDate, birthDate);
			ValueData valueData = new ValueData(values, (int)ageAtExamination);
			this.fetchSimilar(valueData, this.getChildsExaminationsHistory(child.getId()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
 	}

	public static void main(String[] args) {
		new Program();
	}

}
