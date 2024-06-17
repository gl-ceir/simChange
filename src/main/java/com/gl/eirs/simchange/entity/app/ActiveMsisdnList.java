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

    @Column(name = "imsi")
     String imsi;

    @Column(name = "msisdn")
     String msisdn;

    @Column(name = "created_on")
     Timestamp createdOn;

    @Column(name = "modified_on")
     Timestamp modifiedOn;

    @Column(name = "remarks")
     String remarks;


    @Column(name = "operator")
     String operator;


    @Column(name = "activation_date")
     String activationDate;

}
