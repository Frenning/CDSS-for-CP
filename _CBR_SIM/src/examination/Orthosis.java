package examination;

import java.awt.Color;

public class Orthosis
{
	public static String AFO = "AFO";
	public static String KAFO = "KAFO";
	public static String GOAL_ACHIEVED_YES = "Ja";
	public static String GOAL_ACHIEVED_NO = "Nej";
	public static String GOAL_ACHIEVED_DONT_KNOW = "Vet inte";
	public static String GOAL_ACHIEVED_NULL = "";

	protected String description = "";
	protected int id;
	protected String type;
	protected boolean left;
	protected boolean right;

	public Color getColor()
	{
		if (this.right && this.left)
		{
			return new Color(255, 0, 255, 70);
		}
		if (this.right)
		{
			return new Color(0, 0, 255, 70);
		}
		else if (this.left)
		{
			return new Color(255, 0, 0, 70);
		}
		return new Color(200, 200, 200, 70);
	}

	@Override
	public String toString()
	{
		String info = this.type + " " + this.description + " ";
		if (this.right)
			info += "höger ";
		if (this.left)
			info += "vänster ";
		// info += this.usageTime;
		return info;
	}

}
