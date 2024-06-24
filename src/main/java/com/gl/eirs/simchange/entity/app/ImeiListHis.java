package com.gl.eirs.simchange.entity.app;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="imei_pair_detail_his")
public class ImeiListHis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="imsi")
    String imsi;

    @Column(name="msisdn")
    String msisdn;

    @Column(name="imei")
    String imei;

    @Column(name="file_name")
    String fileName;

    @Column(name="expiry_date")
    LocalDateTime expiryDate;

    @Column(name="gsma_status")
    String gsmaStatus;


    @Column(name="pairing_date")
    LocalDateTime pairingDate;

    @Column(name="record_time")
    LocalDateTime recordTime;

    @Column(name="created_on")
    Timestamp createdOn;

    @Column(name="operator")
    String operatorName;

    @Column(name="allowed_days")
    String allowedDays;

    @Column(name="pair_mode")
    String pairMode;

//    @Column(name="operation")
//    int operation;

    @Column(name = "action")
    String action;

    @Column(name = "action_remark")
    String actionRemark;

    @Column(name = "actual_imei")
    String actualImei;

}
