package com.gl.eirs.simchange.builder;

import com.gl.eirs.simchange.entity.app.ImeiList;

import lombok.Builder;
import org.springframework.stereotype.Component;

import static com.gl.eirs.simchange.constants.Constants.remarks;

@Component
@Builder
public class ImeiListBuilder {

    public ImeiList forInsert(ImeiList imeiList, String newImsi) {
        ImeiList imeiList1 = new ImeiList();
        imeiList1.setImei(imeiList.getImei());
        imeiList1.setImsi(newImsi);
        imeiList1.setMsisdn(imeiList.getMsisdn());
        imeiList1.setFileName(imeiList.getFileName());
        imeiList1.setGsmaStatus(imeiList.getGsmaStatus());
        imeiList1.setPairingDate(imeiList.getPairingDate());
        imeiList1.setRecordTime(imeiList.getRecordTime());
        imeiList1.setCreatedOn(imeiList.getCreatedOn());
        imeiList1.setOperatorName(imeiList.getOperatorName());
        imeiList1.setAllowedDays(imeiList.getAllowedDays());
        imeiList1.setExpiryDate(imeiList.getExpiryDate());
        imeiList1.setPairMode(imeiList.getPairMode());
        imeiList1.setActualImei(imeiList.getActualImei());
        return imeiList1;
    }
}