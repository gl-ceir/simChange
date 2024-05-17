package com.gl.eirs.simchange.service.intf;

import com.gl.eirs.simchange.entity.app.BlackList;
import com.gl.eirs.simchange.entity.app.ExceptionList;
import com.gl.eirs.simchange.entity.app.GreyList;

public interface IDbTransactions {

    public boolean dbTransaction(GreyList greyList, String newImsi);
    public boolean dbTransaction(BlackList blackList, String newImsi);
    public boolean dbTransaction(ExceptionList exceptionList, String newImsi);
}
