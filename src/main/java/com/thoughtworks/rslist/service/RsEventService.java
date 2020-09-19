package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RsEventService {
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;

    public List<RsEventEntity> findAll(){
        return rsEventRepository.findAll();
    }

    public Optional<RsEventEntity> findById(int id){
        return rsEventRepository.findById(id);
    }

    public void save(RsEvent rsEvent){

        Optional<UserEntity> userEntityOptional =  userRepository.findById(rsEvent.getUserId());

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .userEntity(userEntityOptional.get())
                .build();
        rsEventRepository.save(rsEventEntity);
    }
    public void save(RsEventEntity rsEventEntity){
        rsEventRepository.save(rsEventEntity);
    }
    public void update(RsEventEntity rsEventEntity){
        Optional<RsEventEntity> oldRsEventEntityOptional = rsEventRepository.findById(rsEventEntity.getId());
        RsEventEntity oldRsEventEntity = oldRsEventEntityOptional.get();
        oldRsEventEntity.setEventName(rsEventEntity.getEventName());
        oldRsEventEntity.setKeyword(rsEventEntity.getKeyword());
        rsEventRepository.save(rsEventEntity);
    }

    public void deleteById(int id){
        rsEventRepository.deleteById(id);
    }


    public void deleteAll(){
        rsEventRepository.deleteAll();
    }
}
