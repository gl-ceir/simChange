package com.gl.eirs.simchange.builder;

import com.gl.eirs.simchange.entity.app.ActiveMsisdnList;
import lombok.Builder;
import org.springframework.stereotype.Component;

import static com.gl.eirs.simchange.constants.Constants.remarks;

@Component
@Builder
public class ActiveMsisdnListBuilder {

    public ActiveMsisdnList forInsert(ActiveMsisdnList activeMsisdnList, String newImsi) {
        ActiveMsisdnList activeMsisdnList1 = new ActiveMsisdnList();
        activeMsisdnList1.setImsi(newImsi);
        activeMsisdnList1.setMsisdn(activeMsisdnList.getMsisdn());
        activeMsisdnList1.setRemarks(remarks);
        activeMsisdnList1.setOperator(activeMsisdnList.getOperator());

        return activeMsisdnList1;
    }
}
