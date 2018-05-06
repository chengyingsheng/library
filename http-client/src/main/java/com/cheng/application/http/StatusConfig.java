package com.cheng.application.http;

public class StatusConfig {
    private String domain;

    private String succCode;
    private String repeatCpde;

    private String statusField;
    private String messageField;
    private String dataField;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSuccCode() {
        return succCode;
    }

    public void setSuccCode(String succCode) {
        this.succCode = succCode;
    }

    public String getRepeatCpde() {
        return repeatCpde;
    }

    public void setRepeatCpde(String repeatCpde) {
        this.repeatCpde = repeatCpde;
    }

    public String getStatusField() {
        return statusField;
    }

    public void setStatusField(String statusField) {
        this.statusField = statusField;
    }

    public String getMessageField() {
        return messageField;
    }

    public void setMessageField(String messageField) {
        this.messageField = messageField;
    }

    public String getDataField() {
        return dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }
}
