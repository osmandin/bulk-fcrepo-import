package edu.yale.library.ladybird.web.view;

import edu.yale.library.ladybird.engine.ProgressEventListener;
import edu.yale.library.ladybird.entity.ImportJob;
import edu.yale.library.ladybird.persistence.dao.ImportJobDAO;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Osman Din {@literal <osman.din.yale@gmail.com>}
 */
@ManagedBean
@SessionScoped
public class ImportProgressView extends AbstractView implements Serializable {

    private Logger logger = LoggerFactory.getLogger(ImportProgressView.class);

    @Inject
    ProgressEventListener progressEventListener;

    @Inject
    ImportJobDAO importJobDAO;

    private int STEPS_TO_COMPLETE;

    private int count = 0;

    @PostConstruct
    public void init() {
        initFields();
        STEPS_TO_COMPLETE = progressEventListener.getExpectedTotalSteps();
    }

    public int count(int jobId) {
        return progressEventListener.getSteps(jobId);
    }

    public boolean jobInMap(int jobId) {
        return progressEventListener.jobInMap(jobId);
    }

    public void progress(int jobId) {
        count = progressEventListener.getSteps(jobId);
    }

    public int getSTEPS_TO_COMPLETE() {
        return STEPS_TO_COMPLETE;
    }

    //TODO change SQL lookup since it might be polled frequently
    public String status(final int jobRequestId) {
        try {
            final int importId = convertToJobId(jobRequestId);
            List<String> jobStatus = new ArrayList<>();

            if (importId != -1) {
                jobStatus = progressEventListener.getJobStatus(importId);
            }

            return importId == -1 ? "" : jobStatus.toString();
        } catch (Exception e) {
            logger.error("Error finding status for monitorId={} Problem ={}", jobRequestId, e);
            return "ERR";
        }
    }

    //TODO change SQL lookup since it might be polled frequently
    public String statusInProgress(final int jobRequestId) {
        try {
            return progressEventListener.getJobStatus(jobRequestId).toString();
        } catch (Exception e) {
            logger.error("Error finding status for monitorId={} Problem ={}", jobRequestId, e);
            return "ERR";
        }
    }

    public int numberExceptions(final int jobRequestId) {
        try {
            //convert moinitor id to import job id
            final int importId = convertToJobId(jobRequestId);
            return importId == -1 ? 0 : (progressEventListener.getRawException(importId) == null
                    ? 0 : progressEventListener.getRawException(importId).size());
        } catch (Exception e) {
            logger.error("Error finding exception trace for item={}", jobRequestId, e);
            return 0;
        }
    }

    public List<ContextTrace> getContextTrace(final int jobRequestId) {
        logger.trace("Getting stack trace info for={} from Event bean", jobRequestId);

        final int importId = convertToJobId(jobRequestId);

        if (importId == -1) {
            return Collections.emptyList();
        }

        final List<ContextTrace> list = new ArrayList<>();
        List<ContextedRuntimeException> e = progressEventListener.getRawException(importId);

        if (e == null) {
            return Collections.emptyList();
        }

        for (final ContextedRuntimeException ie : e) {
            ContextTrace contextTrace = new ContextTrace();
            contextTrace.setContext(getExceptionContext(ie));
            contextTrace.setTrace(getExceptionStackTrace(ie));
            contextTrace.setMessage(ie.getMessage());
            list.add(contextTrace);
        }
        return list;
    }

    private int convertToJobId(final int jobRequestId) {
        if (jobRequestId < 0) {
            return -1;
        }

        final List<ImportJob> importJobs = importJobDAO.findByRequestId(jobRequestId);

        if (importJobs == null || importJobs.isEmpty()) {
            return -1;
        }

        return importJobs.get(0).getImportId();
    }


    public String getExceptionContext(ContextedRuntimeException e) {
        return String.format("%nRow : " + (e).getFirstContextValue("Row"))
                + ", Column : " + (e).getFirstContextValue("Column");
    }

    public List<String> getExceptionStackTrace(ContextedRuntimeException e) {
        final List<String> list = new ArrayList<>();
        final StackTraceElement[] ste = e.getStackTrace();

        for (final StackTraceElement s : ste) {
            list.add(String.format("%n at " + s.getClassName() + "." + s.getMethodName() + ":" + s.getLineNumber()));
        }

        return list;
    }


    public class ContextTrace {

        private String context;
        private String message;
        private List<String> trace;

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public List<String> getTrace() {
            return trace;
        }

        public void setTrace(List<String> trace) {
            this.trace = trace;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
