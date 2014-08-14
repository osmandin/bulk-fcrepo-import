package edu.yale.library.ladybird.persistence.dao.hibernate;

import edu.yale.library.ladybird.entity.Project;
import edu.yale.library.ladybird.persistence.dao.ProjectDAO;
import org.hibernate.Query;

import java.util.List;

public class ProjectHibernateDAO extends GenericHibernateDAO<Project, Integer> implements ProjectDAO {

    public List<Project> findByLabel(String label) {
        final Query q = getSession().createQuery("from Project where label = :param");
        q.setParameter("param", label);
        return q.list();
    }

    @Override
    public Project findByProjectId(int projectId) {
        final Query q = getSession().createQuery("from Project where projectId = :param");
        q.setParameter("param", projectId);
        return (Project) q.list().get(0); //TODO
    }
}

