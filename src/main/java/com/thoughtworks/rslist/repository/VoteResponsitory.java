package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

;
import java.time.LocalDateTime;
import java.util.List;

public interface VoteResponsitory extends CrudRepository<VoteEntity, Integer> {
    List<VoteEntity> findAll();

    List<VoteEntity> findAllByRsEventId(int id);

    List<VoteEntity> findAllByVoteTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query(nativeQuery = true, value = "SELECT * FROM vote WHERE user_id = ?1 AND rs_event_id = ?2")
//    @Query("SELECT v FROM VoteEntity v WHERE v.userId = ?1 AND v.rsEventId = ?2")
    List<VoteEntity> findAllByUserEntityIdAndRsEventEntityId(int userEntityId, int rsEventEntityId, Pageable pageable);
}
