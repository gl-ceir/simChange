package com.gl.eirs.simchange.builder;


import com.gl.eirs.simchange.entity.app.DuplicateDeviceDetail;
import lombok.Builder;
import org.springframework.stereotype.Component;

import static com.gl.eirs.simchange.constants.Constants.remarks;

@Component
@Builder
public class DuplicateDeviceDetailBuilder {
    public DuplicateDeviceDetail forInsert(DuplicateDeviceDetail duplicateDeviceDetail , String newImsi) {
        DuplicateDeviceDetail duplicateDeviceDetail1 = new DuplicateDeviceDetail();
        duplicateDeviceDetail1.setImsi(newImsi);
        duplicateDeviceDetail1.setId(duplicateDeviceDetail.getId());
        duplicateDeviceDetail1.setImei(duplicateDeviceDetail.getImei());
        duplicateDeviceDetail1.setMsisdn(duplicateDeviceDetail.getMsisdn());
        duplicateDeviceDetail1.setCreatedOn(duplicateDeviceDetail.getCreatedOn());
        duplicateDeviceDetail1.setModifiedOn(duplicateDeviceDetail.getModifiedOn());
        duplicateDeviceDetail1.setFileName(duplicateDeviceDetail.getFileName());
        duplicateDeviceDetail1.setEdrTime(duplicateDeviceDetail.getEdrTime());
        duplicateDeviceDetail1.setStatus(duplicateDeviceDetail.getStatus());
        duplicateDeviceDetail1.setRemarks(duplicateDeviceDetail.getRemarks());
        duplicateDeviceDetail1.setOperator(duplicateDeviceDetail.getOperator());
        duplicateDeviceDetail1.setExpiryDate(duplicateDeviceDetail.getExpiryDate());
        duplicateDeviceDetail1.setUpdateBy(duplicateDeviceDetail.getUpdateBy());
        duplicateDeviceDetail1.setTransactionId(duplicateDeviceDetail.getTransactionId());
        duplicateDeviceDetail1.setDocumentType1(duplicateDeviceDetail.getDocumentType1());
        duplicateDeviceDetail1.setDocumentType2(duplicateDeviceDetail.getDocumentType2());
        duplicateDeviceDetail1.setDocumentType3(duplicateDeviceDetail.getDocumentType3());
        duplicateDeviceDetail1.setDocumentType4(duplicateDeviceDetail.getDocumentType4());
        duplicateDeviceDetail1.setDocumentPath1(duplicateDeviceDetail.getDocumentPath1());
        duplicateDeviceDetail1.setDocumentPath2(duplicateDeviceDetail.getDocumentPath2());
        duplicateDeviceDetail1.setDocumentPath3(duplicateDeviceDetail.getDocumentPath3());
        duplicateDeviceDetail1.setDocumentPath4(duplicateDeviceDetail.getDocumentPath4());
        duplicateDeviceDetail1.setReminderStatus(duplicateDeviceDetail.getReminderStatus());
        duplicateDeviceDetail1.setSuccessCount(duplicateDeviceDetail.getSuccessCount());
        duplicateDeviceDetail1.setFailCount(duplicateDeviceDetail.getFailCount());
        duplicateDeviceDetail1.setActualImei(duplicateDeviceDetail.getActualImei());
        return duplicateDeviceDetail1;
    }
}
