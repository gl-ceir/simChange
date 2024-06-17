package com.gl.eirs.simchange.builder;


import com.gl.eirs.simchange.entity.app.ExceptionList;
import lombok.Builder;
import org.springframework.stereotype.Component;

import static com.gl.eirs.simchange.constants.Constants.remarks;

@Component
@Builder
public class ExceptionListBuilder {

    public ExceptionList forInsert(ExceptionList exceptionList, String newImsi) {
        ExceptionList exceptionList1 = new ExceptionList();
        exceptionList1.setImei(exceptionList.getImei());
        exceptionList1.setImsi(newImsi);
        exceptionList1.setMsisdn(exceptionList.getMsisdn());
        exceptionList1.setRemarks(remarks);
        exceptionList1.setActualImei(exceptionList.getActualImei());
        exceptionList1.setComplainType(exceptionList.getComplainType());
        exceptionList1.setExpiryDate(exceptionList.getExpiryDate());
        exceptionList1.setModeType(exceptionList.getModeType());
        exceptionList1.setOperatorName(exceptionList.getOperatorName());
        exceptionList1.setOperatorId(exceptionList.getOperatorId());
        exceptionList1.setRequestType(exceptionList.getRequestType());
        exceptionList1.setTxnId(exceptionList.getTxnId());
        exceptionList1.setUserId(exceptionList.getUserId());
        exceptionList1.setUserType(exceptionList.getUserType());
        exceptionList1.setTac(exceptionList.getTac());
        exceptionList1.setSource(exceptionList.getSource());
        return exceptionList1;
    }

}
