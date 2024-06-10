package com.gl.eirs.simchange.builder;

import com.gl.eirs.simchange.entity.app.ActiveMsisdnList;
import com.gl.eirs.simchange.entity.app.ActiveMsisdnListHis;
import lombok.Builder;
import org.springframework.stereotype.Component;

import static com.gl.eirs.simchange.constants.Constants.remarks;
@Component
@Builder
public class ActiveMsisdnListHisBuilder {

    public ActiveMsisdnListHis forInsert(ActiveMsisdnList activeMsisdnList, String oldImsi) {
        ActiveMsisdnListHis activeMsisdnListHis = new ActiveMsisdnListHis();
        activeMsisdnListHis.setImsi(oldImsi);
        activeMsisdnListHis.setMsisdn(activeMsisdnList.getMsisdn());
        activeMsisdnListHis.setCreatedOn(activeMsisdnList.getCreatedOn());
        activeMsisdnListHis.setModifiedOn(activeMsisdnList.getModifiedOn());
        activeMsisdnListHis.setRemarks(remarks);
        activeMsisdnListHis.setOperator(activeMsisdnList.getOperator());
        activeMsisdnListHis.setOperation(activeMsisdnListHis.getOperation());
        activeMsisdnListHis.setDeactivationDate(activeMsisdnList.getActivationDate());
        return activeMsisdnListHis;
    }
}
