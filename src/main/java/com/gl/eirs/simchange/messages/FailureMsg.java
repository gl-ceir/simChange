package com.gl.eirs.simchange.messages;


import lombok.Builder;
import lombok.Data;

@Data

public class FailureMsg {

    String errMsg;
//    String alertMsg;

    public FailureMsg() {

    }
    public static FailureMsg FailureMsgBuilder(String failureMsg) {
        FailureMsg failureMsg1 = new FailureMsg();
        failureMsg1.setErrMsg(failureMsg);
        return failureMsg1;
    }


}
