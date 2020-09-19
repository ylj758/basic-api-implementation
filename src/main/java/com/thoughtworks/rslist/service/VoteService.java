package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.VoteResponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoteService {

    VoteResponsitory voteResponsitory;
    @Autowired
    public VoteService(VoteResponsitory voteResponsitory){
        this.voteResponsitory = voteResponsitory;
    }

    public void save(VoteEntity voteEntity){
        voteResponsitory.save(voteEntity);
    }

    public List<VoteEntity> findAll(){
        return voteResponsitory.findAll();
    }

    public List<VoteEntity> findAllByRsEventId(int id){
       return voteResponsitory.findAllByRsEventEntityId(id);
    }

    public List<VoteEntity> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable){
        return voteResponsitory.findAllByUserEntityIdAndRsEventEntityId(userId, rsEventId, pageable);
    }

    public List<VoteEntity> findAllByVoteTimeBetween(LocalDateTime start, LocalDateTime end){
        return voteResponsitory.findAllByVoteTimeBetween(start, end);
    }

    public void deleteAll(){
        voteResponsitory.deleteAll();
    }

}
