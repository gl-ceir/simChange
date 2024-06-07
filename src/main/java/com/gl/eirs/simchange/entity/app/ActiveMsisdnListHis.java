package com.gl.eirs.simchange.entity.app;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "active_msisdn_list_his")
public class ActiveMsisdnListHis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_on", nullable = false)
    private Timestamp createdOn;

    @Column(name = "modified_on", nullable = false)
    private Timestamp modifiedOn;

    @Column(name = "msisdn", length = 15, nullable = false)
    private String msisdn;

    @Column(name = "imsi", length = 15, nullable = false)
    private String imsi;

    @Column(name = "remarks", length = 50)
    private String remarks;

    @Column(name = "operation")
    private int operation;

    @Column(name = "operator", length = 50)
    private String operator= "SM";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deactivation_date")
    private Date deactivationDate = new Date(); // Initialize with current date


    public ActiveMsisdnListHis() {
        // Default constructor
    }

    public ActiveMsisdnListHis(ActiveMsisdnList activeMsisdnList) {
        this.msisdn = activeMsisdnList.getMsisdn();
        this.imsi = activeMsisdnList.getImsi();
        this.createdOn = new Timestamp(System.currentTimeMillis());
        this.modifiedOn = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Timestamp modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setDeactivationDate(Date deactivationDate) {
        this.deactivationDate = deactivationDate;
    }
}
