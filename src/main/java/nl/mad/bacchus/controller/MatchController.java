/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.controller;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link org.springframework.web.bind.annotation.RestController} for {@link nl.mad.bacchus.model.Match} stuff.
 */
@RestController
@RequestMapping("/match")
public class MatchController {

    @RequestMapping("/**/*")
    public void notImplementedAPI() {
        throw new NotImplementedException("Change this match all endpoint with real implementation.");
    }

}
