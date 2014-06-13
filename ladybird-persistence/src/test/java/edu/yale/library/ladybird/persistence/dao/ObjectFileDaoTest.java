package edu.yale.library.ladybird.persistence.dao;

import edu.yale.library.ladybird.entity.ObjectFile;
import edu.yale.library.ladybird.entity.ObjectFileBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class ObjectFileDaoTest extends AbstractPersistenceTest {

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
    private ObjectFileDAO dao;

    @Test
    public void shouldSave() {
        final ObjectFile item = build();
        ObjectFile itemByOid = null;
        List<ObjectFile> list = null;
        try {
            dao.save(item);
            list = dao.findAll();
            itemByOid = dao.findByOid(55);
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Error testing saving or finding item");
        }

        assertEquals("Item count incorrect", list.size(), 1);
        assertEquals("Value mismatch", list.get(0).getFileName(), "tmpFile");
        assertEquals(itemByOid.getFileName(), "tmpFile");
    }

    private ObjectFile build() {
        final ObjectFile item = new ObjectFileBuilder().setOid(55).setFileName("tmpFile").createObjectFile();
        final Date date = new Date(System.currentTimeMillis());
        item.setDate(date);
        return item;
    }
}