package com.gl.eirs.simchange.entity.app;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;


@Data
@Entity
@Table(name = "active_msisdn_list")
public class ActiveMsisdnList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imsi", nullable = false)
     String imsi;

    @Column(name = "msisdn", nullable = false)
     String msisdn;

    @Column(name = "created_on", nullable = false)
     Timestamp createdOn;

    @Column(name = "modified_on", nullable = false)
     Timestamp modifiedOn;

    @Column(name = "remarks", length = 50)
     String remarks;


    @Column(name = "operator", length = 50)
     String operator= "SM";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activation_date")
     Date activationDate = new Date();

}
