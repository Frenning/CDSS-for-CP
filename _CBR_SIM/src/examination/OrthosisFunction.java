package examination;

public class OrthosisFunction extends Orthosis
{
	private boolean goalGait;
	private boolean goalBalance;
	private boolean goalExercise;
	private String goalGaitAchieved;
	private String goalBalanceAchieved;
	private String goalExercieAchieved;

	public OrthosisFunction(int id, String type, int right, int left, int goalGait, int goalBalance, int goalExercise,
			String goalGaitAchieved, String goalBalanceAchieved, String goalExercieAchieved)
	{
		this.description = "funktion";
		this.id = id;
		this.type = type;
		this.right = (right == 1);
		this.left = (left == 1);
		this.goalGait = (goalGait == 1);
		this.goalBalance = (goalBalance == 1);
		this.goalExercise = (goalExercise == 1);
		this.goalGaitAchieved = goalGaitAchieved;
		this.goalBalanceAchieved = goalBalanceAchieved;
		this.goalExercieAchieved = goalExercieAchieved;
	}

}
