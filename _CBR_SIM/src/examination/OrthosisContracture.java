package examination;

// Ortoser för kontrakturbehandling
public class OrthosisContracture extends Orthosis
{

	private String usageTime;

	public OrthosisContracture(int id, String type, int left, int right, String usageTime)
	{
		this.description = "kontraktur";
		this.id = id;
		this.type = type;
		this.left = (left == 1);
		this.right = (right == 1);
		this.usageTime = usageTime;
	}

}
