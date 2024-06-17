package com.gl.eirs.simchange.builder;

import com.gl.eirs.simchange.entity.app.ActiveMsisdnList;
import com.gl.eirs.simchange.entity.app.ActiveMsisdnListHis;
import lombok.Builder;
import org.springframework.stereotype.Component;

import static com.gl.eirs.simchange.constants.Constants.remarks;
@Component
@Builder
public class ActiveMsisdnListHisBuilder {

    public ActiveMsisdnListHis forInsert(ActiveMsisdnList activeMsisdnList, int operation) {
        ActiveMsisdnListHis activeMsisdnListHis = new ActiveMsisdnListHis();
        activeMsisdnListHis.setImsi(activeMsisdnList.getImsi());
        activeMsisdnListHis.setMsisdn(activeMsisdnList.getMsisdn());
        activeMsisdnListHis.setRemarks(remarks);
        activeMsisdnListHis.setOperator(activeMsisdnList.getOperator());
        activeMsisdnListHis.setOperation(operation);
        activeMsisdnListHis.setActivationDate(activeMsisdnList.getActivationDate());
        return activeMsisdnListHis;
    }
}
