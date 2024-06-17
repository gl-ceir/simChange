package com.gl.eirs.simchange.builder;

import com.gl.eirs.simchange.entity.app.BlackList;
import com.gl.eirs.simchange.entity.app.BlackListHis;
import lombok.Builder;
import org.springframework.stereotype.Component;

import static com.gl.eirs.simchange.constants.Constants.remarks;


@Component
@Builder
public class BlackListHisBuilder {

    public BlackListHis forInsert(BlackList blackList, int operation) {
        BlackListHis blackListHis = new BlackListHis();
        blackListHis.setImei(blackList.getImei());
        blackListHis.setImsi(blackList.getImsi());
        blackListHis.setMsisdn(blackList.getMsisdn());
        blackListHis.setOperation(operation);
        blackListHis.setRemarks(remarks);
        blackListHis.setActualImei(blackList.getActualImei());
        blackListHis.setComplainType(blackList.getComplainType());
        blackListHis.setExpiryDate(blackList.getExpiryDate());
        blackListHis.setModeType(blackList.getModeType());
        blackListHis.setOperatorName(blackList.getOperatorName());
        blackListHis.setOperatorId(blackList.getOperatorId());
        blackListHis.setRequestType(blackList.getRequestType());
        blackListHis.setTxnId(blackList.getTxnId());
        blackListHis.setUserId(blackList.getUserId());
        blackListHis.setUserType(blackList.getUserType());
        blackListHis.setTac(blackList.getTac());
        blackListHis.setSource(blackList.getSource());
        return blackListHis;
    }
}
