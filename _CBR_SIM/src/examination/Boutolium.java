package examination;

import java.text.DateFormat;
import java.util.Date;

public class Boutolium {
	private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
	private int id;
	private Date date;
	private double age = -1;
	private String muscles;
	
	public Boutolium(int id, String muscles, String date, Date birthDate) {
		this.id = id;
		this.muscles = muscles;
		try {
			if (date != null) {
				this.date = dateFormat.parse(date);
				this.age = Examination.ageDiffToDouble(this.date, birthDate);				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getAge() {
		return this.age;
	}
	
	@Override
	public String toString() {
		if (this.date == null) {
			return "Boutolium: " + this.muscles;
		}
		else {
			return "Boutolium " + this.age + " år: " + this.muscles + " " + DateFormat.getDateInstance(DateFormat.SHORT).format(this.date);
		}
	}
}
