package com.gl.eirs.simchange.builder;

import com.gl.eirs.simchange.entity.app.GreyList;
import com.gl.eirs.simchange.entity.app.GreyListHis;
import lombok.Builder;
import org.springframework.stereotype.Component;

import static com.gl.eirs.simchange.constants.Constants.remarks;

@Component
@Builder
public class GreyListHisBuilder {

    public GreyListHis forInsert(GreyList greyList, int operation) {
        GreyListHis greyListHis = new GreyListHis();
        greyListHis.setImei(greyList.getImei());
        greyListHis.setImsi(greyList.getImsi());
        greyListHis.setMsisdn(greyList.getMsisdn());
        greyListHis.setOperation(operation);
        greyListHis.setRemarks(remarks);
        greyListHis.setActualImei(greyList.getActualImei());
        greyListHis.setComplainType(greyList.getComplainType());
        greyListHis.setExpiryDate(greyList.getExpiryDate());
        greyListHis.setModeType(greyList.getModeType());
        greyListHis.setOperatorName(greyList.getOperatorName());
        greyListHis.setOperatorId(greyList.getOperatorId());
        greyListHis.setRequestType(greyList.getRequestType());
        greyListHis.setTxnId(greyList.getTxnId());
        greyListHis.setUserId(greyList.getUserId());
        greyListHis.setUserType(greyList.getUserType());
        greyListHis.setTac(greyList.getTac());
        greyListHis.setSource(greyList.getSource());
        return greyListHis;
    }
}
