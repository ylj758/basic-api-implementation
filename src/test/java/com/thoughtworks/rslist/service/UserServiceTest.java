package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void sholud_delete_by_id() {
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);
        List<UserEntity> userEntityList = userService.findAll();
        assertEquals(1, userEntityList.size());
        userService.deleteById(1);
        userEntityList = userService.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void sholud_register_and_find_by_id(){
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);
        Optional<UserEntity> userEntity = userService.findById(1);
        assertEquals(userEntity.isPresent(),true);
    }

    @Test
    void sholud_register_and_find_all_success() {
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);
        List<UserEntity> userEntityList = userService.findAll();
        assertEquals(1, userEntityList.size());
        assertEquals("XiaoMing", userEntityList.get(0).getName());
    }


    public UserDto getAConcreteUser(){
        return UserDto.builder()
                .name("XiaoMing")
                .age(22)
                .gender("male")
                .email("768@qq.com")
                .phone("12345678900")
                .vote(10)
                .build();
    }
}