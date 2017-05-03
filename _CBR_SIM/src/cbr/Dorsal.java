package cbr;

import java.awt.Color;
import java.sql.ResultSet;

public class Dorsal
{
	private int[] dorsal = new int[4];

	public Dorsal(ResultSet result)
	{
		try
		{
			dorsal[0] = result.getInt(3);
			dorsal[1] = result.getInt(4);
			dorsal[2] = result.getInt(5);
			dorsal[3] = result.getInt(6);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	public Dorsal(int dorsal0, int dorsal1, int dorsal2, int dorsal3)
	{
		dorsal[0] = dorsal0;
		dorsal[1] = dorsal1;
		dorsal[2] = dorsal2;
		dorsal[3] = dorsal3;
	}

	public int[] getDorsal()
	{
		return this.dorsal;
	}

	public static Color getDorsalColor(int index)
	{
		if (index == 0 || index == 2)
		{
			return Color.blue;
		}
		else if (index == 1 || index == 3)
		{
			return Color.red;
		}
		return Color.gray;
	}

	public static boolean isSquare(int index)
	{
		return (index == 2 || index == 3);
	}

}
