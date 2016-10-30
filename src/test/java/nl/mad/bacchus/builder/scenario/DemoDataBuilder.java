/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.builder.scenario;

import java.math.BigDecimal;
import java.util.Random;

import javax.annotation.PostConstruct;

import nl.mad.bacchus.builder.DataBuilder;
import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.Customer;
import nl.mad.bacchus.model.Match;
import nl.mad.bacchus.model.Wine;
import nl.mad.bacchus.model.meta.CheeseType;
import nl.mad.bacchus.model.meta.MilkType;
import nl.mad.bacchus.model.meta.WineRegion;
import nl.mad.bacchus.model.meta.WineType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Fills the database with test data.
 *
 * @author Jeroen van Schagen
 * @since Jul 6, 2015
 */
public class DemoDataBuilder {

    private static final Random RANDOM = new Random();

    @Autowired
    private DataBuilder dataBuilder;

    /**
     * After the builder has it's dependencies injected
     * we start filling the database with data.
     */
    @PostConstruct
    public void start() {
        Wine sauternes = dataBuilder.newWine().withName("Chateau d’Yquem Sauternes").withCost(randomPrice())
                .withSpecs(WineType.WHITE, WineRegion.BORDEAUX, 2010).save();
        dataBuilder.newPhoto(sauternes).fromTestResource("/wines/sauternes.jpeg").save();

        Wine rotschild = dataBuilder.newWine().withName("Chateau Lafite Rothschild").withCost(randomPrice())
                .withSpecs(WineType.WHITE, WineRegion.BORDEAUX, 2008).save();
        dataBuilder.newPhoto(rotschild).fromTestResource("/wines/lafite.jpeg").save();

        Wine conti = dataBuilder.newWine().withName("Domaine Romanée Conti").withCost(randomPrice()).withSpecs(WineType.RED, WineRegion.BURGUNDY, 2006).save();
        dataBuilder.newPhoto(conti).fromTestResource("/wines/conti.jpeg").save();

        Wine chorey = dataBuilder.newWine().withName("Chorey-Les-Beaune").withCost(randomPrice()).withSpecs(WineType.RED, WineRegion.BURGUNDY, 2008).save();
        dataBuilder.newPhoto(chorey).fromTestResource("/wines/chorley.jpeg").save();

        Wine ch1 = dataBuilder.newWine().withName("Salon Champagne Brut Blanc de Blancs").withCost(randomPrice())
                .withSpecs(WineType.SPARKLING, WineRegion.CHAMPAGNE).save();
        dataBuilder.newPhoto(ch1).fromTestResource("/wines/salon.jpeg").save();

        Wine ch2 = dataBuilder.newWine().withName("Krug Champagne Clos du Mesnil").withCost(randomPrice()).withSpecs(WineType.SPARKLING, WineRegion.CHAMPAGNE)
                .save();
        dataBuilder.newPhoto(ch2).fromTestResource("/wines/krug.jpeg").save();

        Wine brion = dataBuilder.newWine().withName("Chateau Haut Brion").withCost(randomPrice()).withSpecs(WineType.RED, WineRegion.BORDEAUX, 2011).save();
        dataBuilder.newPhoto(brion).fromTestResource("/wines/hautbrion.jpeg").save();

        Wine chave = dataBuilder.newWine().withName("JL Chave Hermitage").withCost(randomPrice()).withSpecs(WineType.RED, WineRegion.RHONE, 2008).save();
        dataBuilder.newPhoto(chave).fromTestResource("/wines/hermitage.jpeg").save();

        Wine tache = dataBuilder.newWine().withName("Domaine Romanée Conti La Tache").withCost(randomPrice())
                .withSpecs(WineType.RED, WineRegion.BURGUNDY, 2008).save();
        dataBuilder.newPhoto(tache).fromTestResource("/wines/latache.jpeg").save();

        Wine dom = dataBuilder.newWine().withName("Moet et Chandon Dom Perignon").withCost(randomPrice()).withSpecs(WineType.SPARKLING, WineRegion.CHAMPAGNE)
                .save();
        dataBuilder.newPhoto(dom).fromTestResource("/wines/domperignon.jpeg").save();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Cheese boucanier = dataBuilder.newCheese().withName("Boucanier").withCost(randomPrice()).withSpecs(CheeseType.SOFT, MilkType.COW).save();
        dataBuilder.newPhoto(boucanier).fromTestResource("/cheeses/boucanier.jpeg").save();

        Cheese comte = dataBuilder.newCheese().withName("Comte").withCost(randomPrice()).withSpecs(CheeseType.HARD, MilkType.EWE).save();
        dataBuilder.newPhoto(comte).fromTestResource("/cheeses/comte.jpeg").save();

        Cheese fourme = dataBuilder.newCheese().withName("Fourme").withCost(randomPrice()).withSpecs(CheeseType.BLUE_MOLD, MilkType.COW).save();
        dataBuilder.newPhoto(fourme).fromTestResource("/cheeses/fourme.jpeg").save();

        Cheese tomme = dataBuilder.newCheese().withName("Tomme").withCost(randomPrice()).withSpecs(CheeseType.SOFT, MilkType.GOAT).save();
        dataBuilder.newPhoto(tomme).fromTestResource("/cheeses/tomme.jpeg").save();

        dataBuilder.newMatch().withSpecs(dom, comte).save();
        dataBuilder.newMatch().withSpecs(tache, tomme).save();

        Customer bas = dataBuilder.newCustomer()
                .withEmail("bas@42.nl")
                .withFullName("Bas de Klant")
                .withBalance(BigDecimal.valueOf(42))
                .withPassword(passwordEncoder.encode("welkom42"))
                .save();

        dataBuilder.newEmail(bas).withMessage("Romeo + Juliet", "Two households, both alike in dignitiy in fair Verona where we lay our scene.")
                .withEmailFrom("William").saveAndSend();
        dataBuilder.newEmail(bas).withMessage("War of the Worlds",
                "And yet accross the gulf of space, minds immeasurable superior to ours regarded this earth with envious eyes.").withEmailFrom("Jules")
                .save();


        dataBuilder.newCustomer()
                .withEmail("rob@42.nl")
                .withFullName("Rob de Koper")
                .withBalance(BigDecimal.valueOf(42))
                .withPassword(passwordEncoder.encode("welkom42"))
                .save();

        dataBuilder.newEmployee()
                .withEmail("erik@42.nl")
                .withFullName("Erik de Medewerker")
                .withPassword(passwordEncoder.encode("welkom42"))
                .save();

        dataBuilder.newEmployee()
                .withEmail("jeroen@42.nl")
                .withFullName("Jeroen de Baas")
                .withPassword(passwordEncoder.encode("welkom42"))
                .save();

        dataBuilder.newOrder().withCustomer(bas).withLine(42, sauternes).withLine(16, brion).withLine(2, conti).save();
        dataBuilder.newOrder().withCustomer(bas).withLine(1, rotschild).withLine(1, brion).save();
    }

    private BigDecimal randomPrice() {
        return BigDecimal.valueOf(RANDOM.nextInt(99));
    }

}
