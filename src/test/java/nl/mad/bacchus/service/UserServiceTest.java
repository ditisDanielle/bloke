/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import nl.mad.bacchus.AbstractSpringTest;
import nl.mad.bacchus.builder.DataBuilder;
import nl.mad.bacchus.model.User;

public class UserServiceTest extends AbstractSpringTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DataBuilder dataBuilder;

    @Test
    public void testFindAllByEmail() {
        User user = dataBuilder.newEmployee().withEmail("jan@42.nl").withFullName("Jan de Man").save();
        User result = userService.findByEmail("jan@42.nl");
        Assert.assertEquals(user.getId(), result.getId());
    }
}
