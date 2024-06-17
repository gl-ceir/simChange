package com.gl.eirs.simchange.builder;


import com.gl.eirs.simchange.entity.app.BlackList;
import lombok.Builder;
import org.springframework.stereotype.Component;

import static com.gl.eirs.simchange.constants.Constants.remarks;

@Component
@Builder
public class BlackListBuilder {

    public BlackList forInsert(BlackList blackList, String newImsi) {
        BlackList blackList1 = new BlackList();
        blackList1.setImei(blackList.getImei());
        blackList1.setImsi(newImsi);
        blackList1.setMsisdn(blackList.getMsisdn());
        blackList1.setRemarks(remarks);
        blackList1.setActualImei(blackList.getActualImei());
        blackList1.setComplainType(blackList.getComplainType());
        blackList1.setExpiryDate(blackList.getExpiryDate());
        blackList1.setModeType(blackList.getModeType());
        blackList1.setOperatorName(blackList.getOperatorName());
        blackList1.setOperatorId(blackList.getOperatorId());
        blackList1.setRequestType(blackList.getRequestType());
        blackList1.setTxnId(blackList.getTxnId());
        blackList1.setUserId(blackList.getUserId());
        blackList1.setUserType(blackList.getUserType());
        blackList1.setTac(blackList.getTac());
        blackList1.setSource(blackList.getSource());
        return blackList1;
    }
}
