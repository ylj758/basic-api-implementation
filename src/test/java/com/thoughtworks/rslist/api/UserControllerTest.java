package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private RsEventService rsEventService;

    @Test
    void should_delete_user_cascade_rs_event() throws Exception {
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);
        assertEquals(1, userService.findAll().size());
        RsEvent rsEvent = RsEvent.builder()
                .eventName("猪肉涨价了")
                .keyword("经济")
                .userId(1)
                .build();
        rsEventService.save(rsEvent);
        assertEquals(1, rsEventService.findAll().size());

        mockMvc.perform(delete("/user/delete/1"))
                .andExpect(status().isNoContent());

        assertEquals(0, userService.findAll().size());
        assertEquals(0, rsEventService.findAll().size());
    }

    @Test
    void should_delete_user_by_id() throws Exception {
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);
        List<UserEntity> userEntityList = userService.findAll();
        assertEquals(1, userEntityList.size());

        mockMvc.perform(delete("/user/delete/1"))
                .andExpect(status().isNoContent());

        userEntityList = userService.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void should_get_user_by_id() throws Exception {
        UserDto userDto = new UserDto("ylj", "femal", 25, "123@qq.com", "12345678911");
        userService.register(userDto);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("ylj")));
    }

    @Test
    void should_register_user() throws Exception {
        UserDto userDto = new UserDto("ylj", "femal", 25, "123@qq.com", "12345678911");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        Optional<UserEntity> userEntity = userService.findById(1);
        assertEquals(userEntity.isPresent(),false);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        userEntity = userService.findById(1);
        assertEquals(userEntity.isPresent(),true);
    }

    @Test
    void should_register_name_not_empty() throws Exception {
        UserDto userDto = new UserDto("", "femal", 25, "123@qq.com", "12345678911");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_register_name_length_not_more_than_8() throws Exception {
        UserDto userDto = new UserDto("yulvjun888", "femal", 25, "123@qq.com", "12345678911");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_register_gender_not_empty() throws Exception {
        UserDto userDto = new UserDto("ylj", "", 25, "123@qq.com", "12345678911");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_register_age_not_empty() throws Exception {
        UserDto userDto = new UserDto("ylj", "female", null, "123@qq.com", "12345678911");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_register_age_more_than_18() throws Exception {
        UserDto userDto = new UserDto("ylj", "female", 12, "123@qq.com", "12345678911");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_register_age_not_more_than_100() throws Exception {
        UserDto userDto = new UserDto("ylj", "female", 111, "123@qq.com", "12345678911");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_register_email_not_empty() throws Exception {
        UserDto userDto = new UserDto("ylj", "female", 25, "", "12345678911");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_register_email_is_valid() throws Exception {
        UserDto userDto = new UserDto("ylj", "female", 25, "", "12345678911");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_register_phone_not_empty() throws Exception {
        UserDto userDto = new UserDto("ylj", "female", 25, "78@qq.com", "");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_register_phone_is_valid() throws Exception {
        UserDto userDto = new UserDto("ylj", "female", 25, "78@qq.com", "0900");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_get_all_user() throws Exception {
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("XiaoMing")));
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