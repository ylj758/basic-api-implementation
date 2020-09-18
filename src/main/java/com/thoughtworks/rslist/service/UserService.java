package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.CriteriaBuilder;
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

    public Optional<UserEntity> findById(int id){
        return userRepository.findById(id);
    }

    public void deleteById(int id){
        userRepository.deleteById(id);
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

    public boolean existsById(int id){
        return userRepository.existsById(id);
    }

    public void deleteAll(){
        userRepository.deleteAll();
    }

    public void updateLeftVoteNum(int id, int voteNum){
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);
        UserEntity userEntity = userEntityOptional.get();
        userEntity.setVote(voteNum);
        userRepository.save(userEntity);
    }
}
