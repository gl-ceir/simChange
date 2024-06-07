package com.gl.eirs.simchange.entity.app;


import jakarta.persistence.*;



@Entity
@Table(name = "active_msisdn_list")
public class ActiveMsisdnList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imsi", nullable = false)
    private String imsi;

    @Column(name = "msisdn", nullable = false)
    private String msisdn;

    public ActiveMsisdnList() {
        // Default constructor
    }

    public ActiveMsisdnList(String imsi, String msisdn) {
        this.imsi = imsi;
        this.msisdn = msisdn;
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
