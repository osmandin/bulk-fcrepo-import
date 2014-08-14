package edu.yale.library.ladybird.engine.exports;

import edu.yale.library.ladybird.engine.AbstractDBTest;
import edu.yale.library.ladybird.engine.ObjectTestsHelper;
import edu.yale.library.ladybird.engine.metadata.FieldDefinitionValue;
import edu.yale.library.ladybird.engine.metadata.MetadataEditor;
import edu.yale.library.ladybird.entity.AuthorityControl;
import edu.yale.library.ladybird.entity.FieldDefinition;
import edu.yale.library.ladybird.entity.ObjectAcidVersion;
import edu.yale.library.ladybird.entity.ObjectBuilder;
import edu.yale.library.ladybird.entity.ObjectString;
import edu.yale.library.ladybird.entity.ObjectStringVersion;
import edu.yale.library.ladybird.persistence.dao.AuthorityControlDAO;
import edu.yale.library.ladybird.persistence.dao.ObjectAcidVersionDAO;
import edu.yale.library.ladybird.persistence.dao.ObjectStringVersionDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.AuthorityControlHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectAcidHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectAcidVersionHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectStringHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectStringVersionHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectVersionHibernateDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Tests metadata editing. Multiple values are tested.
 */
public class MetadataEditorTest extends AbstractDBTest {

    private Logger logger = LoggerFactory.getLogger(MetadataEditorTest.class);

    /**
     * Tests handling of multiple fdid metadata.
     * Makes an edit and tests whether edit was applied and the metadata items were versioned.
     */
    @Test
    public void shouldUpdateMultipleMetadata() {
        MetadataEditor metadataEditor = new MetadataEditor();
        int testOid = 1;
        int testUserId = 1;
        List<FieldDefinitionValue> fieldDefinitionValueList = getFdidValueList();
        assert (fieldDefinitionValueList.size() == 2);

        int oid = writeDummyObject(); // write object values

        //Let us write 2 strings and 2 acids for 2 fdids:
        ObjectTestsHelper.writeDummyObjAcid(oid, 69, "String Acid value");
        ObjectTestsHelper.writeDummyObjAcid(oid, 69, "String Acid value 2");
        ObjectTestsHelper.writeDummyObjString(oid, 71, "String Value");
        ObjectTestsHelper.writeDummyObjString(oid, 71, "Another String Value");

        assert (ObjectTestsHelper.fdidValue(oid, 71).size() == 2);
        assert (ObjectTestsHelper.fdidAcidValueList(oid, 69).size() == 2);

        metadataEditor.updateOidMetadata(testOid, testUserId, fieldDefinitionValueList);

        //read back metadata:
        //1. make sure only the same number of fields exist
        assert (new ObjectStringHibernateDAO().findAll().size() == 2);
        assert (new ObjectAcidHibernateDAO().findAll().size() == 2);

        //2. make sure object string edits were applied
        List<ObjectString> objectStrings = ObjectTestsHelper.fdidValue(oid, 71);
        assert (objectStrings.get(0).getValue().equalsIgnoreCase("New string value 1"));
        assert (objectStrings.get(1).getValue().equalsIgnoreCase("New string value 2"));

        //3. make sure object acid edits were applied
        List<AuthorityControl> acList = ObjectTestsHelper.fdidAcidValueList(oid, 69);
        logger.debug(acList.toString());

        assert (acList.get(0).getValue().equalsIgnoreCase("WHATEVER 1"));
        assert (acList.get(1).getValue().equalsIgnoreCase("Whatever 2"));

        //4. Test for versioning
        assert (new ObjectVersionHibernateDAO().findByOid(oid).size() == 1);

        ObjectStringVersionDAO osvDAO = new ObjectStringVersionHibernateDAO();
        List<ObjectStringVersion> osvList = osvDAO.findListByOidAndFdidAndVersion(oid, 71, 1);
        assert (osvList.size() == 2);
        assert (osvList.get(0).getValue().equalsIgnoreCase("String value"));
        assert (osvList.get(1).getValue().equalsIgnoreCase("Another String value"));

        ObjectAcidVersionDAO oavDAO = new ObjectAcidVersionHibernateDAO();
        List<ObjectAcidVersion> oavList = oavDAO.findListByOidAndFdidAndVersion(oid, 69, 1);
        assert (oavList.size() == 2);

        AuthorityControlDAO authDAO = new AuthorityControlHibernateDAO();
        int acid1 = oavList.get(0).getValue();
        assert (authDAO.findByAcid(acid1).getValue().equalsIgnoreCase("String Acid Value"));

        int acid2 = oavList.get(1).getValue();
        assert (authDAO.findByAcid(acid2).getValue().equalsIgnoreCase("String Acid Value 2"));

        assert (authDAO.findAll().size() == 4);
    }

    private int writeDummyObject() {
        Date d = new Date();
        edu.yale.library.ladybird.entity.Object object = new ObjectBuilder().setUserId(0).setProjectId(0).setDate(d).createObject();
        return new ObjectHibernateDAO().save(object);
    }

    /**
     * @return The update to be applied
     */
    private List<FieldDefinitionValue> getFdidValueList() {
        List<FieldDefinitionValue> fdidList = new ArrayList<>();
        fdidList.add(getFdidValue(69, Arrays.asList("WHATEVER 1", "WHATEVER 2")));
        fdidList.add(getFdidValue(71, Arrays.asList("New String value 1", "New String value 2")));
        return fdidList;
    }

    private FieldDefinitionValue getFdidValue(int fdid, List<String> value) {
        FieldDefinition fieldDefinition = new FieldDefinition(fdid);
        return  new FieldDefinitionValue(fieldDefinition, value);
    }

    @Before
    public void init() {
        super.init();
    }

    @After
    public void stop() throws SQLException {
        super.stop();
    }

}
