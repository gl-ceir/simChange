package com.gl.eirs.simchange.alert;

public interface IAlert {
    public void raiseAnAlert(String alertId, String alertMessage, String alertProcess, int userId);
}
