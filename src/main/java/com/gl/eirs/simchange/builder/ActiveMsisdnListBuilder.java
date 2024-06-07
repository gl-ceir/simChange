package com.gl.eirs.simchange.builder;

import com.gl.eirs.simchange.entity.app.ActiveMsisdnList;

public class ActiveMsisdnListBuilder {

    private String msisdn;
    private String imsi;

    public ActiveMsisdnListBuilder setMsisdn(String msisdn) {
        this.msisdn = msisdn;
        return this;
    }

    public ActiveMsisdnListBuilder setImsi(String imsi) {
        this.imsi = imsi;
        return this;
    }

    public ActiveMsisdnList build() {
        ActiveMsisdnList activeMsisdnList = new ActiveMsisdnList();
        activeMsisdnList.setMsisdn(msisdn);
        activeMsisdnList.setImsi(imsi);
        return activeMsisdnList;
    }
}
