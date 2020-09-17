package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService{
    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserEntity> findAll(){
       return userRepository.findAll();
    }

    public Optional<UserEntity> findById(Integer integer){
        return userRepository.findById(integer);
    }
    public void register(UserDto userDto){
        UserEntity userEntity = UserEntity.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .age(userDto.getAge())
                .gender(userDto.getGender())
                .phone(userDto.getPhone())
                .vote(userDto.getVote())
                .build();
        userRepository.save(userEntity);
    }

    public void clearData(){
        userRepository.deleteAll();
    }
}
