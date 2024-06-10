package com.gl.eirs.simchange.repository.app;

import com.gl.eirs.simchange.entity.app.ActiveMsisdnList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiveMsisdnListRepository extends JpaRepository<ActiveMsisdnList, Long> {
    ActiveMsisdnList findByImsi(String oldImsi);


    void deleteByImsi(String imsi);
}
