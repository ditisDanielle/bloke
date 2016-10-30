package nl.mad.bacchus.service;

import nl.mad.bacchus.AbstractSpringTest;
import nl.mad.bacchus.builder.DataBuilder;
import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.Match;
import nl.mad.bacchus.model.Wine;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * Created by fd on 24-10-16.
 */
public class MatchServiceTest  extends AbstractSpringTest {

    @Autowired
    private MatchService matchService;
    @Autowired
    private DataBuilder dataBuilder;

    @Before
    public void setUp() {
        loginWithRoles(Employee.ADMIN);
    }

    @Test
    public void testGetCheesesOrWines() {

        Wine wine1 = dataBuilder.newWine().withName("wijntje").save();
        Wine wine2 = dataBuilder.newWine().withName("wine").save();

        Cheese cheese1 = dataBuilder.newCheese().withName("kaasje").save();
        Cheese cheese2 = dataBuilder.newCheese().withName("cheese").save();

        Match match1 = dataBuilder.newMatch().withSpecs(wine1,cheese1).save();
        Match match2 = dataBuilder.newMatch().withSpecs(wine2, cheese1).save();
        Match match3 = dataBuilder.newMatch().withSpecs(wine1, cheese2).save();


        List<Match> result1 = matchService.getCheesesForWine(wine1.getId());
        List<Match> result2 = matchService.getCheesesForWine(wine2.getId());

        List<Match> result3 = matchService.getWinesForCheese(cheese1.getId());
        List<Match> result4 = matchService.getWinesForCheese(cheese2.getId());

        Assert.assertEquals(2, result1.size());
        Assert.assertEquals(1, result2.size());
        Assert.assertEquals(2, result3.size());
        Assert.assertEquals(1, result4.size());
    }

    @Test
    public void testSave() throws IOException {

        Cheese cheese = dataBuilder.newCheese().withName("Kaas").save();
        Wine wine = dataBuilder.newWine().withName("Wijn").save();

        Match match = dataBuilder.newMatch().withSpecs(wine, cheese).save();

        Match result = matchService.create(wine.getId(),cheese.getId());

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getCheese());
        Assert.assertNotNull(result.getWine());

        List<Match> lijst = matchService.getCheesesForWine(result.getWine().getId());
        Match created = lijst.get(0);

        Assert.assertEquals(match.getId(), created.getId());
        Assert.assertEquals(match.getCheese().getId(), created.getCheese().getId());
        Assert.assertEquals(match.getWine().getId(), created.getWine().getId());

    }

    @Test
    public void testDelete()
    {
        Cheese cheese = dataBuilder.newCheese().withName("Kaas").save();
        Wine wine = dataBuilder.newWine().withName("Wijn").save();

        Match result = matchService.create(wine.getId(),cheese.getId());

        List<Match> lijst = matchService.getCheesesForWine(result.getWine().getId());

        Assert.assertEquals(1, lijst.size());

        matchService.delete(result.getWine().getId(), result.getCheese().getId());

        Assert.assertEquals(0, matchService.getCheesesForWine(result.getWine().getId()).size());
    }

    @Test
    public void testMatch()
    {
        Cheese cheese = dataBuilder.newCheese().withName("Kaas").save();
        Wine wine = dataBuilder.newWine().withName("Wijn").save();

        matchService.create(wine.getId(),cheese.getId());

        Boolean result = matchService.getMatch(wine.getId(), cheese.getId());

        Assert.assertEquals(true, result);
    }


}
