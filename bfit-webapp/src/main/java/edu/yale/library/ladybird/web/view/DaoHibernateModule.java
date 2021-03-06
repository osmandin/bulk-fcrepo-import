package edu.yale.library.ladybird.web.view;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import edu.yale.library.ladybird.engine.ProgressEventListener;
import edu.yale.library.ladybird.engine.cron.scheduler.ExportFileMailerScheduler;
import edu.yale.library.ladybird.engine.cron.scheduler.ExportScheduler;
import edu.yale.library.ladybird.engine.cron.scheduler.ImportScheduler;
import edu.yale.library.ladybird.entity.Project;
import edu.yale.library.ladybird.entity.User;
import edu.yale.library.ladybird.kernel.cron.ScheduledJobsList;
import edu.yale.library.ladybird.kernel.cron.ScheduledJobs;
import edu.yale.library.ladybird.persistence.dao.AccessconditionProjectDAO;
import edu.yale.library.ladybird.persistence.dao.AuthorityControlDAO;
import edu.yale.library.ladybird.persistence.dao.AuthorityControlVersionDAO;
import edu.yale.library.ladybird.persistence.dao.CollectionDAO;
import edu.yale.library.ladybird.persistence.dao.EventTypeDAO;
import edu.yale.library.ladybird.persistence.dao.FieldDefinitionDAO;
import edu.yale.library.ladybird.persistence.dao.FieldMarcMappingDAO;
import edu.yale.library.ladybird.persistence.dao.GenericDAO;
import edu.yale.library.ladybird.persistence.dao.ImportFileDAO;
import edu.yale.library.ladybird.persistence.dao.ImportJobDAO;
import edu.yale.library.ladybird.persistence.dao.ImportJobNotificationsDAO;
import edu.yale.library.ladybird.persistence.dao.ImportSourceDAO;
import edu.yale.library.ladybird.persistence.dao.ImportSourceDataDAO;
import edu.yale.library.ladybird.persistence.dao.JobRequestDAO;
import edu.yale.library.ladybird.persistence.dao.ObjectAcidDAO;
import edu.yale.library.ladybird.persistence.dao.ObjectAcidVersionDAO;
import edu.yale.library.ladybird.persistence.dao.ObjectDAO;
import edu.yale.library.ladybird.persistence.dao.ObjectEventDAO;
import edu.yale.library.ladybird.persistence.dao.ObjectFileDAO;
import edu.yale.library.ladybird.persistence.dao.ObjectLongstringDAO;
import edu.yale.library.ladybird.persistence.dao.ObjectStringDAO;
import edu.yale.library.ladybird.persistence.dao.ObjectStringVersionDAO;
import edu.yale.library.ladybird.persistence.dao.ObjectVersionDAO;
import edu.yale.library.ladybird.persistence.dao.PermissionsDAO;
import edu.yale.library.ladybird.persistence.dao.ProjectDAO;
import edu.yale.library.ladybird.persistence.dao.ProjectTemplateAcidDAO;
import edu.yale.library.ladybird.persistence.dao.ProjectTemplateDAO;
import edu.yale.library.ladybird.persistence.dao.ProjectTemplateStringsDAO;
import edu.yale.library.ladybird.persistence.dao.RolesDAO;
import edu.yale.library.ladybird.persistence.dao.RolesPermissionsDAO;
import edu.yale.library.ladybird.persistence.dao.SettingsDAO;
import edu.yale.library.ladybird.persistence.dao.UserDAO;
import edu.yale.library.ladybird.persistence.dao.UserEventDAO;
import edu.yale.library.ladybird.persistence.dao.UserPreferencesDAO;
import edu.yale.library.ladybird.persistence.dao.UserProjectDAO;
import edu.yale.library.ladybird.persistence.dao.UserProjectFieldDAO;
import edu.yale.library.ladybird.persistence.dao.UserProjectFieldExportOptionsDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.AccessconditionProjectHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.AuthorityControlHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.AuthorityControlVersionHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.CollectionHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.EventTypeHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.FieldDefinitionHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.FieldMarcMappingHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ImportFileHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ImportJobHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ImportJobNotificationsHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ImportSourceDataHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ImportSourceHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.JobRequestHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectAcidHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectAcidVersionHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectEventHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectFileHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectLongstringHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectStringHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectStringVersionHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ObjectVersionHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.PermissionsHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ProjectHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ProjectTemplateAcidHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ProjectTemplateHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ProjectTemplateStringsHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.RolesHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.RolesPermissionsHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.SettingsHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.UserEventHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.UserHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.UserPreferencesHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.UserProjectFieldExportOptionsHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.UserProjectFieldHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.UserProjectHibernateDAO;
import edu.yale.library.ladybird.web.http.CollectionHttpService;
import edu.yale.library.ladybird.web.http.CronScheduleHttpService;
import edu.yale.library.ladybird.web.http.FieldDefinitionHttpService;
import edu.yale.library.ladybird.web.http.ImportSourcerHttpService;
import edu.yale.library.ladybird.web.http.ProjectHttpService;
import edu.yale.library.ladybird.web.http.UserEventHttpService;
import edu.yale.library.ladybird.web.http.UserHttpService;

/**
 * @author Osman Din {@literal <osman.din.yale@gmail.com>}
 */
public class DaoHibernateModule extends AbstractModule {
    @Override
    protected void configure() {
        TypeLiteral<GenericDAO<User, Integer>> userDaoType
                = new TypeLiteral<GenericDAO<User, Integer>>() { };

        TypeLiteral<GenericDAO<Project, Integer>> projectDaoType
                = new TypeLiteral<GenericDAO<Project, Integer>>() { };

        bind(userDaoType).to(UserDAO.class);

        bind(UserDAO.class).to(UserHibernateDAO.class);
        bind(UserPreferencesDAO.class).to(UserPreferencesHibernateDAO.class);
        bind(JobRequestDAO.class).to(JobRequestHibernateDAO.class);
        bind(ImportSourceDAO.class).to(ImportSourceHibernateDAO.class);
        bind(ImportSourceDataDAO.class).to(ImportSourceDataHibernateDAO.class);
        bind(ImportFileDAO.class).to(ImportFileHibernateDAO.class);
        bind(ProjectDAO.class).to(ProjectHibernateDAO.class);
        bind(CollectionDAO.class).to(CollectionHibernateDAO.class);
        bind(FieldDefinitionDAO.class).to(FieldDefinitionHibernateDAO.class);
        bind(FieldMarcMappingDAO.class).to(FieldMarcMappingHibernateDAO.class);
        bind(ObjectDAO.class).to(ObjectHibernateDAO.class);
        bind(ObjectFileDAO.class).to(ObjectFileHibernateDAO.class);
        bind(ObjectAcidDAO.class).to(ObjectAcidHibernateDAO.class);
        bind(ObjectStringDAO.class).to(ObjectStringHibernateDAO.class);
        bind(ObjectLongstringDAO.class).to(ObjectLongstringHibernateDAO.class);
        bind(UserProjectDAO.class).to(UserProjectHibernateDAO.class);
        bind(UserProjectFieldDAO.class).to(UserProjectFieldHibernateDAO.class);
        bind(UserEventDAO.class).to(UserEventHibernateDAO.class);
        bind(RolesDAO.class).to(RolesHibernateDAO.class);
        bind(PermissionsDAO.class).to(PermissionsHibernateDAO.class);
        bind(RolesPermissionsDAO.class).to(RolesPermissionsHibernateDAO.class);
        bind(AuthorityControlDAO.class).to(AuthorityControlHibernateDAO.class);
        bind(AuthorityControlVersionDAO.class).to(AuthorityControlVersionHibernateDAO.class);
        bind(AccessconditionProjectDAO.class).to(AccessconditionProjectHibernateDAO.class);
        bind(SettingsDAO.class).to(SettingsHibernateDAO.class);
        bind(ProjectTemplateDAO.class).to(ProjectTemplateHibernateDAO.class);
        bind(ProjectTemplateStringsDAO.class).to(ProjectTemplateStringsHibernateDAO.class);
        bind(ProjectTemplateAcidDAO.class).to(ProjectTemplateAcidHibernateDAO.class);
        bind(ImportJobDAO.class).to(ImportJobHibernateDAO.class);
        bind(ImportJobNotificationsDAO.class).to(ImportJobNotificationsHibernateDAO.class);
        bind(UserProjectFieldExportOptionsDAO.class).to(UserProjectFieldExportOptionsHibernateDAO.class);
        bind(ObjectVersionDAO.class).to(ObjectVersionHibernateDAO.class);
        bind(ObjectAcidVersionDAO.class).to(ObjectAcidVersionHibernateDAO.class);
        bind(ObjectStringVersionDAO.class).to(ObjectStringVersionHibernateDAO.class);
        bind(ObjectEventDAO.class).to(ObjectEventHibernateDAO.class);
        bind(EventTypeDAO.class).to(EventTypeHibernateDAO.class);

        bind(UserHttpService.class);
        bind(UserEventHttpService.class);
        bind(ImportSourcerHttpService.class);
        bind(ProjectHttpService.class);
        bind(CollectionHttpService.class);
        bind(FieldDefinitionHttpService.class);
        bind(CronScheduleHttpService.class);

        bind(ImportScheduler.class);
        bind(ExportScheduler.class);
        bind(ExportFileMailerScheduler.class);
        bind(ProgressEventListener.class);

        bind(ScheduledJobs.class).to(ScheduledJobsList.class);

        bind(AuthUtil.class);
    }
}
