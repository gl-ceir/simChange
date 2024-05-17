package com.gl.eirs.simchange.repository.app;

import com.gl.eirs.simchange.entity.app.GreyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GreyListRepository extends JpaRepository<GreyList, Integer> {



    List<GreyList> findAllByImsi(String imsi);
}
