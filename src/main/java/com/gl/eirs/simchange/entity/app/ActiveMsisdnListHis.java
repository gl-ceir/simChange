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



}
