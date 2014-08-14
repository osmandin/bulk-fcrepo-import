package edu.yale.library.ladybird.engine.cron;

import edu.yale.library.ladybird.entity.ImportJob;
import edu.yale.library.ladybird.entity.ImportJobNotifications;
import edu.yale.library.ladybird.entity.User;
import edu.yale.library.ladybird.kernel.events.EMailNotificationHandler;
import edu.yale.library.ladybird.kernel.events.NotificationHandler;
import edu.yale.library.ladybird.kernel.events.exports.ExportEvent;
import edu.yale.library.ladybird.persistence.dao.ImportJobDAO;
import edu.yale.library.ladybird.persistence.dao.ImportJobNotificationsDAO;
import edu.yale.library.ladybird.persistence.dao.UserDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ImportJobHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.ImportJobNotificationsHibernateDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.UserHibernateDAO;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 *  Schedules file notification job
 *  TODO test e.g. to ensure that only max num of tries is executed)
 */
public class DefaultExportMailerJob implements Job, ExportMailerJob {

    private Logger logger = getLogger(this.getClass());

    /* maximum number of notification attemtps */
    private short MAX_TRIES = 5;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.trace("Executing export mailer job");

        try {
            //1. Look up to see if any notifications need to be sent
            ImportJobNotificationsDAO importJobNotficationsDAO = new ImportJobNotificationsHibernateDAO(); //TODO
            List<ImportJobNotifications> unsent = importJobNotficationsDAO.findAllUnsent();

            NotificationHandler notificationHandler = new EMailNotificationHandler(); //TODO
            UserDAO userDAO = new UserHibernateDAO(); //TODO
            ImportJobDAO importJobDAO = new ImportJobHibernateDAO(); //TODO

            if (!unsent.isEmpty()) {
                logger.trace("Unsent notifications list={}", unsent);
            }

            //2. Send those notifications
            for (final ImportJobNotifications unsentNotification : unsent) {
                if (unsentNotification.getNumTries() < MAX_TRIES) {
                    try {
                        //1. look up user who initiated this import:
                        int userId = unsentNotification.getUserId();
                        User user = userDAO.findByUserId(userId);

                        final ImportJob importJob = importJobDAO.findByJobId(unsentNotification.getImportJobId()).get(0);

                        logger.trace("Found Import job={}", importJob.toString());

                        final String path = importJob.getExportJobDir();
                        logger.trace("File path of export file={}", path);

                        File f = new File(path);

                        logger.debug("Attempting to send file={} to user={}", f.getAbsolutePath(), user.getUsername());

                        notificationHandler.notifyUserWithFile(user, new ExportEvent() {
                            @Override
                            public String getEventName() {
                                return "FILE-READY";
                            }
                        }, f);

                        //2. update number of tries count and notification success
                        unsentNotification.setNumTries(unsentNotification.getNumTries() + 1);
                        unsentNotification.setNotified((byte) 1);
                        unsentNotification.setDateTried(new Date());
                        importJobNotficationsDAO.updateItem(unsentNotification);
                        logger.trace("Updated item={}", unsentNotification);
                        logger.trace("Full list={}", importJobNotficationsDAO.findAll()); //TODO remove
                    } catch (Exception e) {
                        logger.error("Error notifing user with file attachment", e);
                        unsentNotification.setNumTries(unsentNotification.getNumTries() + 1);
                        unsentNotification.setDateTried(new Date());
                        importJobNotficationsDAO.updateItem(unsentNotification);
                    }
                } else {
                    logger.error("Exhausted maximum number of tries notifiying userId={} for importId={}.", unsentNotification.getUserId(), unsentNotification.getImportJobId());
                }
            }
        } catch (Exception e) {
            logger.error("Error", e);
            throw new JobExecutionException(e);
        }
    }

}
