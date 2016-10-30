/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.controller;

import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.Match;
import nl.mad.bacchus.model.Wine;
import nl.mad.bacchus.repository.MatchRepository;
import nl.mad.bacchus.service.MatchService;
import nl.mad.bacchus.service.dto.CheeseDTO;
import nl.mad.bacchus.service.dto.MatchDTO;
import nl.mad.bacchus.service.dto.WineDTO;
import nl.mad.bacchus.service.CheeseService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static nl.mad.bacchus.service.dto.CheeseDTO.toResultDTO;

/**
 * {@link org.springframework.web.bind.annotation.RestController} for {@link nl.mad.bacchus.model.Match} stuff.
 */
@RestController
@RequestMapping("/match")
public class MatchController {

    private MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService)
    {
        this.matchService = matchService;
    }




    @RequestMapping(value ="/cheese/{cheeseId}" ,method = RequestMethod.GET)
    public List<WineDTO> getWines(@PathVariable Long cheeseId) {

        List<Match> matchLijst = matchService.getWinesForCheese(cheeseId);

        List<Wine> wijnLijst = new ArrayList<Wine>();

        for(Match match : matchLijst)
        {
            wijnLijst.add(match.getWine());
        }

        return stream(wijnLijst.spliterator(), false).map(wine -> WineDTO.toResultDTO(wine)).collect(toList());
    }

    @RequestMapping(value ="/wine/{wineId}" ,method = RequestMethod.GET)
    public List<CheeseDTO> getCheeses(@PathVariable Long wineId) {

        List<Match> matchLijst = matchService.getCheesesForWine(wineId);

        List<Cheese> kaasLijst = new ArrayList<Cheese>();

        for(Match match : matchLijst)
        {
            kaasLijst.add(match.getCheese());
        }

        return stream(kaasLijst.spliterator(), false).map(cheese -> CheeseDTO.toResultDTO(cheese)).collect(toList());
    }

    @RequestMapping(value="/wine/{wineId}/cheese/{cheeseId}", method={RequestMethod.POST})
    public void create(@PathVariable(value="wineId") @RequestBody Long wineId, @PathVariable(value="cheeseId") @RequestBody Long cheeseId) {
        this.matchService.create(wineId, cheeseId);
    }

    @RequestMapping(value = "/wine/{wineId}/cheese/{cheeseId}", method = RequestMethod.GET)
    public Map<String, Boolean> getMatch( @PathVariable(value="wineId") @RequestBody Long wineId, @PathVariable(value="cheeseId") @RequestBody Long cheeseId) {
        boolean result = matchService.getMatch(wineId, cheeseId);
        return Collections.singletonMap("result", result);
    }

    @RequestMapping(value = "/wine/{wineId}/cheese/{cheeseId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value="wineId") @RequestBody Long wineId, @PathVariable(value="cheeseId") @RequestBody Long cheeseId) {
        matchService.delete(wineId, cheeseId);
    }



}
