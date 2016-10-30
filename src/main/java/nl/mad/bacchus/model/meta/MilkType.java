package nl.mad.bacchus.model.meta;

/**
 * Created by fd on 1-10-16.
 */
public enum MilkType implements Labeled{

    COW("Koe"),
    GOAT("Geit"),
    EWE("Schaap");

    private String label;

    private MilkType(String label) {
        this.label=label;
    }

    public String getLabel() {
        return label;
    }


}
