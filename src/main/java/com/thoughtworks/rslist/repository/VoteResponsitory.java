package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteResponsitory extends CrudRepository<VoteEntity, Integer> {
    List<VoteEntity> findAll();


    @Query("select SUM(v.voteNum) from VoteEntity v where v.id = ?1")
    List<Object[]> sumVoteNumByRsEventId(int id);

}