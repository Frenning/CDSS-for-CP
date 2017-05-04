package cbr;

import java.util.Date;
import java.util.Vector;

import examination.Examination;
import similarity.SimilarityHistoryComplete;

public class ExaminationHistory
{
	private Vector<Examination> examinations = new Vector<Examination>();
	private int relevantId = -1;
	private int childId;
	private int birthYear;
	private int gender;
	private Date birthDate;
	private SimilarityHistoryComplete simHistory = null;

	public ExaminationHistory(int childId, int birthYear, int gender)
	{
		this.childId = childId;
		this.birthYear = birthYear;
		this.gender = gender;
		birthDate = Examination.birthYearToDate(birthYear);
	}

	public void addExamination(Examination examination)
	{
		if (examination.isRelevant())
		{
			this.relevantId = examinations.size();
		}
		examinations.add(examination);
	}

	public Vector<Examination> getExaminations()
	{
		return this.examinations;
	}

	public Examination first()
	{
		return examinations.get(0);
	}
	
	public Examination last()
	{
		return examinations.lastElement();
	}

	public void setRelevant(Examination relevant)
	{
		this.relevantId = this.examinations.indexOf(relevant);
		examinations.get(this.relevantId).setRelevant();
	}

	public Examination relevant()
	{
		if (relevantId == -1)
		{
			return examinations.lastElement();
		}
		else
		{
			return examinations.get(relevantId);
		}
	}

	public int getChild()
	{
		return this.childId;
	}

	public Date getBirthDate()
	{
		return this.birthDate;
	}

	@Override
	public String toString()
	{
		return "Här finns " + examinations.size() + " undersökningar";
	}

	public void addSimilarityHistory(SimilarityHistoryComplete simHistory)
	{
		this.simHistory = simHistory;
	}

	public SimilarityHistoryComplete getSimilarityHistory()
	{
		return this.simHistory;
	}

}
