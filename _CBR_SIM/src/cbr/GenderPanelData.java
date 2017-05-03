package cbr;

public class GenderPanelData extends PanelData {

	public GenderPanelData(String columnName, String description, int min, int max) {
		super(columnName, description, min, max);
	}
	
	public GenderPanelData() {
		super("gender", "description", 0, 0);
	}	

}
