package nl.mad.bacchus.model.meta;

public enum WineType implements Labeled {

	RED("Rood"),
	WHITE("Wit"),
	ROSE("Rose"),
	SPARKLING("Mousserend");
	
	private String label;
	
	private WineType(String label) {
		this.label=label;
	}
	
	public String getLabel() {
		return label;
	}
	
}
