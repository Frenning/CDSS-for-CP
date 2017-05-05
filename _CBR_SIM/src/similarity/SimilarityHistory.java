package similarity;

public class SimilarityHistory
{
	private String description;
	private double valueCurrentPatient;
	private double valueFromCaseBase;
	private double similarity;
	private double totalSimilarity;

	public SimilarityHistory(String description, double valueCurrentPatient, double valueFromCaseBase,
			double similarity, double totalSimilarity)
	{
		this.description = description;
		this.valueCurrentPatient = valueCurrentPatient;
		this.valueFromCaseBase = valueFromCaseBase;
		this.similarity = similarity;
		this.totalSimilarity = totalSimilarity;
	}

	public String getDescription()
	{
		return description;
	}

	public double getValueCurrentPatient()
	{
		return valueCurrentPatient;
	}

	public double getValueFromCaseBase()
	{
		return valueFromCaseBase;
	}

	public double getDifference()
	{
		return this.getValueFromCaseBase() - this.getValueCurrentPatient();
	}

	public double getSimilarity()
	{
		return similarity;
	}

	public double getTotal()
	{
		return this.totalSimilarity;
	}

	@Override
	public String toString()
	{
		return "SimilarityHistory [description=" + description + ", valueCurrentPatient=" + valueCurrentPatient
				+ ", valueFromCaseBase=" + valueFromCaseBase + ", similarity=" + similarity + "]";
	}

}
