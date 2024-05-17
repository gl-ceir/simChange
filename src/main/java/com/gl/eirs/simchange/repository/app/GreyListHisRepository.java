package com.gl.eirs.simchange.repository.app;


import com.gl.eirs.simchange.entity.app.GreyListHis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GreyListHisRepository extends JpaRepository<GreyListHis, Integer> {
}
