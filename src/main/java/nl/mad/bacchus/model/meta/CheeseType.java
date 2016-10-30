package nl.mad.bacchus.model.meta;

/**
 * Created by fd on 1-10-16.
 */
public enum CheeseType implements Labeled{

    HARD("Hard"),
    SOFT("Zacht"),
    BLUE_MOLD("Blauwschimmel"),
    WHITE_MOLD("Witschimmel");

    private String label;

    private CheeseType(String label) {
        this.label=label;
    }

    public String getLabel() {
        return label;
    }


}
