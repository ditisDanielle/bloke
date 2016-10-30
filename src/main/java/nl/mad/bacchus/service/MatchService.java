package nl.mad.bacchus.service;

import com.google.common.base.Preconditions;

import java.util.List;

import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.Match;
import nl.mad.bacchus.model.Wine;
import nl.mad.bacchus.repository.MatchRepository;
import nl.mad.bacchus.service.dto.MatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

/**
 * Created by fd on 24-10-16.
 */
@Service
public class MatchService {

    private MatchRepository matchRepository;
    private CheeseService cheeseService;
    private WineService wineService;

    @Autowired
    public MatchService(MatchRepository matchRepository, CheeseService cheeseService, WineService wineService)
    {
        this.matchRepository = matchRepository;
        this.cheeseService = cheeseService;
        this.wineService = wineService;
    }

    public List<Match> getWinesForCheese(Long cheeseId)
    {
        List<Match> result =  matchRepository.findAllByCheeseId(cheeseId);
        return Preconditions.checkNotNull(result, "no matches from cheese: ");
    }

    public List<Match> getCheesesForWine(Long wineId)
    {
        List<Match> result =  matchRepository.findAllByWineId(wineId);
        return Preconditions.checkNotNull(result, "no matches from wine: ");
    }

    @Secured(Employee.ADMIN)
    public Match create(Long wineId, Long cheeseId)
    {
        Cheese cheese = cheeseService.findById(cheeseId);
        Wine wine =  wineService.findById(wineId);


        Match match = new Match(wine,cheese);
        return save(match);
    }

    private Match save(Match match) {
        return matchRepository.save(match);
    }

    public Boolean getMatch(Long wineId, Long cheeseId)
    {
        if(matchRepository.findByWineIdAndCheeseId(wineId,cheeseId) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Secured(Employee.ADMIN)
    public void delete(Long wineId, Long cheeseId) {
        Match match = matchRepository.findByWineIdAndCheeseId(wineId,cheeseId);
        matchRepository.delete(match.getId());
    }



}
