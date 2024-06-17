package com.gl.eirs.simchange.service.intf;

import com.gl.eirs.simchange.entity.app.*;
import jakarta.transaction.Transactional;

public interface IDbTransactions {

    public boolean dbTransaction(GreyList greyList, String newImsi);
    public boolean dbTransaction(BlackList blackList, String newImsi);
    public boolean dbTransaction(ExceptionList exceptionList, String newImsi);
    public boolean dbTransaction(ImeiList imeiList, String newImsi);
    public boolean dbTransaction(DuplicateDeviceDetail duplicateDeviceDetail, String newImsi);
    public boolean dbTransaction(ActiveMsisdnList activeMsisdnList, String newImsi);


}
