package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {
    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService){
        this.userService = userService;
    }

    @Test
    void sholud_register_and_find_all_success() {
        UserDto userDto = UserDto.builder()
                .name("XiaoMing")
                .age(22)
                .gender("male")
                .email("768@qq.com")
                .phone("12345678900")
                .vote(10)
                .build();
        userService.register(userDto);
        List<UserEntity> userEntityList = userService.findAll();
        assertEquals(1, userEntityList.size());
        assertEquals("XiaoMing", userEntityList.get(0).getName());
    }
}