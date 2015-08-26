package nl.mad.bacchus.model.meta;

public enum Country implements Labeled {

	UNKNOWN("Onbekend"),
	FRANCE("Frankrijk"),
	GERMANY("Duitsland"),
 ITALIA("Italië"),
	SOUTHAFRICA("Zuid Afrika"),
	CHILI("Chilie"),
	ARGENTINIA("Argentinia"),
	NEWZEALAND("Nieuw Zeeland"),
	AUSTRALIA("Australie");
	
	private String label;

	private Country(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
}
