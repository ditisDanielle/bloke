package nl.mad.bacchus.controller;

import mockit.Expectations;
import mockit.Injectable;
import nl.mad.bacchus.AbstractControllerTest;
import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.Match;
import nl.mad.bacchus.model.Wine;
import nl.mad.bacchus.repository.MatchRepository;
import nl.mad.bacchus.service.MatchService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

/**
 * Created by fd on 24-10-16.
 */
public class MatchControllerTest extends AbstractControllerTest {

    @Injectable
    private MatchService matchService;
    @Injectable
    private MatchRepository matchRepository;

    @Before
    public void setUp() {
        initWebClient(new MatchController(matchService));
    }



}
