package edu.yale.library.ladybird.entity;

import java.util.Date;

/**
 * @author Osman Din {@literal <osman.din.yale@gmail.com>}
 */
public class ImportSourceBuilder {

    private Date createdDate;
    private String url;
    private Integer xmlType = 0;
    private String getPrefix;

    public ImportSourceBuilder setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ImportSourceBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public ImportSourceBuilder setXmlType(Integer xmlType) {
        this.xmlType = xmlType;
        return this;
    }

    public ImportSourceBuilder setGetPrefix(String getPrefix) {
        this.getPrefix = getPrefix;
        return this;
    }

    public ImportSource createImportSource() {
        return new ImportSource(createdDate, url, xmlType.toString(), getPrefix);
    }
}