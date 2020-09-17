package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private RsEventService rsEventService;
    @Autowired
    private VoteService voteService;

    @BeforeEach
    void setUp() {
        userService.deleteAll();
        rsEventService.deleteAll();
    }

    @Test
    void should_vote_unsuccess_when_left_votenum_not_more_than_votenum() throws Exception {
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);
        RsEvent rsEvent = RsEvent.builder()
                .eventName("猪肉涨价了")
                .keyword("经济")
                .userId(1)
                .build();
        rsEventService.save(rsEvent);

        VoteDto voteDto = VoteDto.builder()
                .voteNum(100)
                .voteTime(new Date(new java.util.Date().getTime()))
                .userId(1)
                .rsEventId(2)
                .build();

        mockMvc.perform(post("/rs/vote/2")
                .content(new ObjectMapper().writeValueAsString(voteDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_vote_success_when_left_votenum_more_than_votenum() throws Exception {
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);
        RsEvent rsEvent = RsEvent.builder()
                .eventName("猪肉涨价了")
                .keyword("经济")
                .userId(1)
                .build();
        rsEventService.save(rsEvent);

        VoteDto voteDto = VoteDto.builder()
                .voteNum(2)
                .voteTime(new Date(new java.util.Date().getTime()))
                .userId(1)
                .rsEventId(2)
                .build();

        mockMvc.perform(post("/rs/vote/2")
                .content(new ObjectMapper().writeValueAsString(voteDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        assertEquals(1, voteService.findAll().size());
        Optional<UserEntity> userEntityOptional = userService.findById(1);
        assertEquals(10-2, userEntityOptional.get().getVote());
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
