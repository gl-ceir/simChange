package com.gl.eirs.simchange.builder;

import com.gl.eirs.simchange.entity.app.ActiveMsisdnListHis;

public class ActiveMsisdnListHisBuilder {

    private String msisdn;
    private String imsi;
    private String remarks;
    private int operation;
    private String operator;

    public ActiveMsisdnListHisBuilder setMsisdn(String msisdn) {
        this.msisdn = msisdn;
        return this;
    }

    public ActiveMsisdnListHisBuilder setImsi(String imsi) {
        this.imsi = imsi;
        return this;
    }

    public ActiveMsisdnListHisBuilder setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public ActiveMsisdnListHisBuilder setOperation(int operation) {
        this.operation = operation;
        return this;
    }

    public ActiveMsisdnListHisBuilder setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public ActiveMsisdnListHis build() {
        ActiveMsisdnListHis activeMsisdnListHis = new ActiveMsisdnListHis();
        activeMsisdnListHis.setMsisdn(msisdn);
        activeMsisdnListHis.setImsi(imsi);
        activeMsisdnListHis.setRemarks(remarks);
        activeMsisdnListHis.setOperation(operation);
        activeMsisdnListHis.setOperator(operator);
        return activeMsisdnListHis;
    }
}
