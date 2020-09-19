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
    @Autowired
    VoteResponsitory voteResponsitory;

    public void save(VoteEntity voteEntity){
        voteResponsitory.save(voteEntity);
    }

//    public void save(VoteDto voteDto){
//
//        VoteEntity voteEntity = VoteEntity.builder()
//                .voteNum(voteDto.getVoteNum())
//                .voteTime(LocalDateTime.now())
//                .rsEventEntity(voteDto.getRsEventId())
//                .userEntity(voteDto.)
//                .build();
//
//        voteResponsitory.save(voteEntity);
//    }
//
//    public List<VoteEntity> findAll(){
//        return voteResponsitory.findAll();
//    }
//
//    public List<VoteEntity> findByRsEventId(int id){
//       return voteResponsitory.findByRsEventId(id);
//    }
//
    public List<VoteEntity> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable){
        return voteResponsitory.findAllByUserEntityIdAndRsEventEntityId(userId, rsEventId, pageable);
    }

    public void deleteAll(){
        voteResponsitory.deleteAll();
    }

}
