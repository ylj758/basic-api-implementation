package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.VoteResponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {
    @Autowired
    VoteResponsitory voteResponsitory;

    public void save(VoteDto voteDto){
        VoteEntity voteEntity = VoteEntity.builder()
                .voteNum(voteDto.getVoteNum())
                .voteTime(voteDto.getVoteTime())
                .rsEventId(voteDto.getRsEventId())
                .userId(voteDto.getUserId())
                .build();
        voteResponsitory.save(voteEntity);
    }

    public List<VoteEntity> findAll(){
        return voteResponsitory.findAll();
    }

    public int sumVoteNumByRsEventId(int id){
        List<Object[]> list = voteResponsitory.sumVoteNumByRsEventId(id);
        return 0;
    }
}
