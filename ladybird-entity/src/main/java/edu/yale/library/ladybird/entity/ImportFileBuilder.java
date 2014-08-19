package edu.yale.library.ladybird.entity;

import java.util.Date;

public class ImportFileBuilder {
    private Integer importId;
    private Date date;
    private String fileLocation;
    private int oid;
    private int userId;
    private Integer code;
    private String error;
    private String type;
    private String label;

    public ImportFileBuilder setImportId(Integer importId) {
        this.importId = importId;
        return this;
    }

    public ImportFileBuilder setDate(Date date) {
        this.date = date;
        return this;
    }

    public ImportFileBuilder setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
        return this;
    }

    public ImportFileBuilder setOid(int oid) {
        this.oid = oid;
        return this;
    }

    public ImportFileBuilder setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public ImportFileBuilder setCode(Integer code) {
        this.code = code;
        return this;
    }

    public ImportFileBuilder setError(String error) {
        this.error = error;
        return this;
    }

    public ImportFileBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public ImportFileBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public ImportFile createImportFile() {
        final ImportFile importFile = new ImportFile();
        importFile.setImportId(importId);
        importFile.setDate(date);
        importFile.setFileLocation(fileLocation);
        importFile.setOid(oid);
        importFile.setUserId(userId);
        importFile.setCode(code);
        importFile.setError(error);
        importFile.setType(type);
        importFile.setLabel(label);
        return importFile;
    }
}