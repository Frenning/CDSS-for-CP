package similarity;

public class Breakpoint implements Comparable
{
	private double x;
	private double similarity;

	public Breakpoint(double x, double similarity)
	{
		this.x = x;
		this.similarity = similarity;
	}

	public double getX()
	{
		return this.x;
	}

	public double getSimilarity()
	{
		return this.similarity;
	}

	@Override
	public int compareTo(Object o)
	{
		Breakpoint other = (Breakpoint) o;
		if (this.getX() > other.getX())
			return 1;
		else if (this.getX() < other.getX())
			return -1;
		return 0;
	}

}
