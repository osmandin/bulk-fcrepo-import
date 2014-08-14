package edu.yale.library.ladybird.engine.exports;

import edu.yale.library.ladybird.engine.AbstractDBTest;
import edu.yale.library.ladybird.engine.ObjectTestsHelper;
import edu.yale.library.ladybird.engine.metadata.ObjectVersioner;
import edu.yale.library.ladybird.entity.ObjectAcid;
import edu.yale.library.ladybird.entity.ObjectString;
import edu.yale.library.ladybird.persistence.dao.ObjectAcidVersionDAO;
import edu.yale.library.ladybird.persistence.dao.ObjectStringVersionDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectAcidHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectAcidVersionHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectStringHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectStringVersionHibernateDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ObjectVersionerTest extends AbstractDBTest {

    @Test
    public void versionStrings() {
        ObjectStringVersionDAO objectStringVersionDAO = new ObjectStringVersionHibernateDAO();
        int obStr = ObjectTestsHelper.writeDummyObjString(1, 70, "Old value");

        ObjectVersioner metadataEditor = new ObjectVersioner();
        List<ObjectString> list = getEmptyList();
        list.add(new ObjectStringHibernateDAO().findAll().get(0));
        metadataEditor.versionObjectStrings(list);

        assert (objectStringVersionDAO.findAll().get(0).getVersionId() == 1);
    }

    @Test
    public void shouldVersionAcid() {
        ObjectAcidVersionDAO dao = new ObjectAcidVersionHibernateDAO();
        int objAcid = ObjectTestsHelper.writeDummyObjAcid(1, 70, "Old Acid Value");

        assert (dao.findAll().isEmpty());

        List<ObjectAcid> list = getEmptyList();
        list.add(new ObjectAcidHibernateDAO().findAll().get(0));

        ObjectVersioner metadataEditor = new ObjectVersioner();
        metadataEditor.versionObjectAcid(list);

        //logger.debug("Object acid versions={}", dao.findAll().toString());
        assert (dao.findAll().get(0).getVersionId() == 1);
    }

    @Test
    public void shouldVersionObject() {
        ObjectVersioner metadataEditor = new ObjectVersioner();
        metadataEditor.versionObject(1, 1);
        assert (metadataEditor.getLastObjectVersion(1) == 1);
    }

    @Test
    public void shouldGetLastVersion() {
        ObjectTestsHelper.writeDummyObjVersion(new int[]{1, 2});
        ObjectVersioner metadataEditor = new ObjectVersioner();
        assert (metadataEditor.getLastObjectVersion(1) == 2);
    }

    @Before
    public void init() {
        super.init();

    }

    @After
    public void stop() throws SQLException {
        super.stop();
    }

    public List getEmptyList() {
        return new ArrayList();
    }
}
