package edu.yale.library.ladybird.persistence.dao.hibernate;


import edu.yale.library.ladybird.entity.Monitor;
import edu.yale.library.ladybird.persistence.dao.MonitorDAO;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class MonitorHibernateDAO extends GenericHibernateDAO<Monitor, Integer> implements MonitorDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<Monitor> findByUserAndProject(int userId, int projectId) {
        final Session s = getSession();
        try {
            final Query q = s.createQuery("from edu.yale.library.ladybird.entity.Monitor where currentUserId = :param1 and currentProjectId =:param2");
            q.setParameter("param1", userId);
            q.setParameter("param2", projectId);
            return q.list();
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }
}