package com.gl.eirs.simchange.repository.app;

import com.gl.eirs.simchange.entity.app.ImeiListHis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImeiListHisRepository extends JpaRepository<ImeiListHis, Integer> {
}
