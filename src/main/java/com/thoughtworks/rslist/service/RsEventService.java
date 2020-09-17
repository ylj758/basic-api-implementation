package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RsEventService {
    @Autowired
    RsEventRepository rsEventRepository;

    public List<RsEventEntity> findAll(){
       return rsEventRepository.findAll();
    }

    public void save(RsEvent rsEvent){
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .userId(rsEvent.getUserId())
                .build();
        rsEventRepository.save(rsEventEntity);
    }
}
