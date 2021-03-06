package edu.yale.library.ladybird.persistence.dao;

import edu.yale.library.ladybird.entity.User;
import edu.yale.library.ladybird.entity.UserBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class UserDaoTest extends AbstractPersistenceTest {

    {
        TestDaoInitializer.injectFields(this);
    }

    @Before
    public void init() {
        initDB();
    }

    @After
    public void stop() throws SQLException {
        //TODO
    }

    @Inject
    private UserDAO dao;

    @Test
    public void testSave() {
        final User user = build();
        try {
            dao.save(user);
            final List list = dao.findAll();
            assertEquals("Item count incorrect", list.size(), 1);
        } catch (Throwable e) {
            fail("Error testing saving or finding item");
        }
    }

    private User build() {
        User item = new UserBuilder().createUser();
        item.setUsername("test user");
        item.setPassword("test_pw");
        Date date = new Date(System.currentTimeMillis());
        item.setDate(date);
        item.setDateCreated(date);
        item.setDateEdited(date);
        item.setDateEdited(date);
        item.setDateLastused(date);
        return item;
    }


}