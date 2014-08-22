package edu.yale.library.ladybird.engine.exports;

import edu.yale.library.ladybird.engine.imports.SpreadsheetFile;
import edu.yale.library.ladybird.entity.User;
import edu.yale.library.ladybird.kernel.events.exports.ExportEvent;

/**
 * TODO field access. Subject to modification.
 */
public final class ExportCompleteEvent extends ExportEvent {

    private int rowsProcessed;
    private int passCount;
    private int failCount;
    private int failedValidations;
    private SpreadsheetFile spreadsheetFile;
    private int importId;

    public ExportCompleteEvent(User user, SpreadsheetFile spreadsheetFile, int rowsProcessed, int passCount,
                               int failedValidations, int failCount) {
        this.spreadsheetFile = spreadsheetFile;
        this.rowsProcessed = rowsProcessed;
        this.passCount = passCount;
        this.failedValidations = failedValidations;
        this.failCount = failCount;
    }

    public int getRowsProcessed() {
        return rowsProcessed;
    }

    public int getPassCount() {
        return passCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public int getFailedValidations() {
        return failedValidations;
    }

    public SpreadsheetFile getSpreadsheetFile() {
        return spreadsheetFile;
    }

    public void setRowsProcessed(int rowsProcessed) {
        this.rowsProcessed = rowsProcessed;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public void setFailedValidations(int failedValidations) {
        this.failedValidations = failedValidations;
    }

    public void setSpreadsheetFile(SpreadsheetFile spreadsheetFile) {
        this.spreadsheetFile = spreadsheetFile;
    }

    public int getImportId() {
        return importId;
    }

    public void setImportId(int importId) {
        this.importId = importId;
    }

    @Override
    public String toString() {
        return "ExportCompleteEvent{"
                + "rowsProcessed=" + rowsProcessed
                + ", passCount=" + passCount
                + ", failCount=" + failCount
                + ", failedValidations=" + failedValidations
                + ", spreadsheetFile=" + spreadsheetFile
                + '}';
    }
}
