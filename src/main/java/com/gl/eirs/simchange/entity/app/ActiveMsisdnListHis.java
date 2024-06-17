package com.gl.eirs.simchange.entity.app;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "active_msisdn_list_his")
public class ActiveMsisdnListHis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "modified_on")
    private Timestamp modifiedOn;

    @Column(name = "msisdn")
    private String msisdn;

    @Column(name = "imsi")
    private String imsi;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "operation")
    private int operation;

    @Column(name = "operator")
    private String operator;


    @Column(name = "activation_date")
    private String activationDate; // Initialize with current date



}
