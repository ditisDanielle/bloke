package nl.mad.bacchus.model;

import javax.persistence.*;

/**
 * Created by fd on 24-10-16.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Match extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "wine_id")
    private Wine wine;

    @ManyToOne
    @JoinColumn(name = "cheese_id")
    private Cheese cheese;

    public Match(){}

    public Match(Wine wine, Cheese cheese)
    {
        this.wine = wine;
        this.cheese = cheese;
    }

    public void setWine(Wine wine)
    {
        this.wine = wine;
    }

    public void setCheese(Cheese cheese)
    {
        this.cheese = cheese;
    }

    public Wine getWine()
    {
        return wine;
    }

    public Cheese getCheese()
    {
        return cheese;
    }

}
