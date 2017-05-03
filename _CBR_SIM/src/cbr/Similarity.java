package cbr;

import similarity.SimilarityHistoryComplete;

public class Similarity implements Comparable {
	private int id;
	private double similarity;
	private String examinationDate;
	private SimilarityHistoryComplete simHistory = null;
	
	public Similarity(int index, double similarity, String examinationDate) {
		this.id = index;
		this.similarity = similarity;
		this.examinationDate = examinationDate;
	}
	
	public Similarity(int index, double similarity, String examinationDate, SimilarityHistoryComplete simHistory) {
		this.id = index;
		this.similarity = similarity;
		this.examinationDate = examinationDate;
		this.simHistory = simHistory;
	}
	
	public int getId() {
		return this.id;
	}
	
	public double getSimilarity() {
		return this.similarity;
	}
	
	public String getExaminationDate() {
		return this.examinationDate;
	}
	
	@Override
	public String toString() {
		return id + "(" + examinationDate + "): " + similarity;
	}

	@Override
	public int compareTo(Object o) {
		Similarity other = (Similarity) o;
		if (this.similarity > other.getSimilarity())
			return -1;	// Minus because we want the largest value first
		else if (other.getSimilarity() > this.similarity)
			return 1;
		return 0;
	}
	
	public SimilarityHistoryComplete getSimilarityHistory() {
		return this.simHistory;
	}
	

}
