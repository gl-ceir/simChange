package com.gl.eirs.simchange.repository.app;


import com.gl.eirs.simchange.entity.app.ImeiList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImeiListRepository extends JpaRepository<ImeiList, Integer> {
    List<ImeiList> findAllByImsi(String imsi);

}
