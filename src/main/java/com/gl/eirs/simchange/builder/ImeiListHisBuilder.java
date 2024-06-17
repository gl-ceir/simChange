package com.gl.eirs.simchange.builder;

import com.gl.eirs.simchange.entity.app.ImeiList;
import com.gl.eirs.simchange.entity.app.ImeiListHis;
import lombok.Builder;
import org.springframework.stereotype.Component;

import static com.gl.eirs.simchange.constants.Constants.remarks;

@Component
@Builder
public class ImeiListHisBuilder {

    public ImeiListHis forInsert(ImeiList imeiList, int operation, String action) {
        ImeiListHis imeiListHis = new ImeiListHis();
        imeiListHis.setImei(imeiList.getImei());
        imeiListHis.setImsi(imeiList.getImsi());
        imeiListHis.setMsisdn(imeiList.getMsisdn());
        imeiListHis.setFileName(imeiList.getFileName());
        imeiListHis.setGsmaStatus(imeiList.getGsmaStatus());
        imeiListHis.setPairingDate(imeiList.getPairingDate());
        imeiListHis.setRecordTime(imeiList.getRecordTime());
        imeiListHis.setCreatedOn(imeiList.getCreatedOn());
        imeiListHis.setOperatorName(imeiList.getOperatorName());
        imeiListHis.setAllowedDays(imeiList.getAllowedDays());
        imeiListHis.setExpiryDate(imeiList.getExpiryDate());
        imeiListHis.setPairMode(imeiList.getPairMode());
        imeiListHis.setAction(action);
        imeiListHis.setActualImei(imeiList.getActualImei());
        imeiListHis.setActionRemark(remarks);
        return imeiListHis;
    }
}