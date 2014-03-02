package edu.yale.library.engine.exports;

import edu.yale.library.beans.User;
import edu.yale.library.engine.imports.SpreadsheetFile;
import edu.yale.library.events.exports.ExportEvent;

/**
 * TODO field access. Subject to modification.
 */
public final class ExportCompleteEvent extends ExportEvent {

    int rowsProcessed;
    int passCount;
    int failCount;
    int failedValidations;

    private SpreadsheetFile spreadsheetFile;

    public ExportCompleteEvent(User user, SpreadsheetFile spreadsheetFile, int rowsProcessed, int passCount,
                               int failedValidations, int failCount) {
        this.spreadsheetFile = spreadsheetFile;
        this.rowsProcessed = rowsProcessed;
        this.passCount = passCount;
        this.failedValidations = failedValidations;
        this.failCount = failCount;
    }

    @Override
    public String toString() {
        return "ImportCompleteEvent{"
                + "rowsProcessed=" + rowsProcessed
                + ", passCount=" + passCount
                + ", failCount=" + failCount
                + ", failedValidations=" + failedValidations
                + ", spreadsheetFile=" + spreadsheetFile
                + '}';
    }
}
