package com.gl.eirs.simchange.repository.app;


import com.gl.eirs.simchange.entity.app.DuplicateDeviceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DuplicateDeviceDetailRepository extends JpaRepository<DuplicateDeviceDetail, Integer> {
    List<DuplicateDeviceDetail> findAllByImsi(String imsi);

}