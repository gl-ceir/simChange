package com.gl.eirs.simchange.repository.app;

import com.gl.eirs.simchange.entity.app.ActiveMsisdnListHis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveMsisdnListHisRepository extends JpaRepository<ActiveMsisdnListHis, Long> {
    @Query("SELECT COUNT(h) FROM ActiveMsisdnListHis h WHERE h.imsi = :imsi AND h.msisdn = :msisdn AND h.remarks = :remarks")
    int countByImsiAndMsisdnAndRemarks(@Param("imsi") String imsi, @Param("msisdn") String msisdn, @Param("remarks") String remarks);
}

