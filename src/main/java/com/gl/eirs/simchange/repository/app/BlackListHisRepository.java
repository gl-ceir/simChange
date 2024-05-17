package com.gl.eirs.simchange.repository.app;

import com.gl.eirs.simchange.entity.app.BlackListHis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListHisRepository extends JpaRepository<BlackListHis, Integer> {
}
