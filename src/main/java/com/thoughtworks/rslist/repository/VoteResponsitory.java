package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

;
import java.util.List;

public interface VoteResponsitory extends CrudRepository<VoteEntity, Integer> {
    List<VoteEntity> findAll();


    List<VoteEntity> findByRsEventId(int id);

//    @Query("SELECT v FROM VoteEntity v WHERE v.userId = ?1 AND v.rsEventId = ?2")
    List<VoteEntity> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);
}
