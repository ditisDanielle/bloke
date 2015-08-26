package nl.mad.bacchus.model.meta;

public enum WineRegion implements Labeled {

	UNKNOWN("Onbekend",Country.UNKNOWN),
	BORDEAUX("Bordeaux",Country.FRANCE),
	BURGUNDY("Bourgogne",Country.FRANCE),
	JURA("Jura",Country.FRANCE),
	CHAMPAGNE("Champagne",Country.FRANCE),
	RHONE("Rhone",Country.FRANCE),
	MOSEL("Moezel",Country.GERMANY),
	STELLENBOSCH("Stellenbosch",Country.SOUTHAFRICA),
	HAWKES_BAY("Hawke's Bay",Country.NEWZEALAND),
	MARLBOROUGH("Malborough",Country.NEWZEALAND),
	BAROSSA_VALLEY("Barossa Valley",Country.AUSTRALIA),
	MCLAREN_VALE("McLaren Vale",Country.AUSTRALIA),
	RAPEL_VALLEY("Rapel Valley",Country.CHILI),
	UCO_VALLEY("Uco Valley",Country.ARGENTINIA);
	
	private String label;
	private Country country;

	private WineRegion(String label,Country country) {
		this.label = label;
		this.country = country;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
	public Country getCountry() {
		return country;
	}
	
}
