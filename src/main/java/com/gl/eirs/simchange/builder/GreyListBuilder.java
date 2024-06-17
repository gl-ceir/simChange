package com.gl.eirs.simchange.builder;

import com.gl.eirs.simchange.entity.app.GreyList;
import lombok.Builder;
import org.springframework.stereotype.Component;

import static com.gl.eirs.simchange.constants.Constants.remarks;


@Component
@Builder
public class GreyListBuilder {

    public GreyList forInsert(GreyList greyList, String newImsi) {
        GreyList greyList1 = new GreyList();
        greyList1.setImei(greyList.getImei());
        greyList1.setImsi(newImsi);
        greyList1.setMsisdn(greyList.getMsisdn());
        greyList1.setRemarks(remarks);
        greyList1.setActualImei(greyList.getActualImei());
        greyList1.setComplainType(greyList.getComplainType());
        greyList1.setExpiryDate(greyList.getExpiryDate());
        greyList1.setModeType(greyList.getModeType());
        greyList1.setOperatorName(greyList.getOperatorName());
        greyList1.setOperatorId(greyList.getOperatorId());
        greyList1.setRequestType(greyList.getRequestType());
        greyList1.setTxnId(greyList.getTxnId());
        greyList1.setUserId(greyList.getUserId());
        greyList1.setUserType(greyList.getUserType());
        greyList1.setTac(greyList.getTac());
        greyList1.setSource(greyList.getSource());
        return greyList1;
    }

}
