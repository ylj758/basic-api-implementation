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

    @BeforeEach
    void setUp() {
        userService.deleteAll();
        rsEventService.deleteAll();
    }

//    request: post /rs/vote/{rsEventId}
//    request body: {
//        voteNum: 5,
//                userId: 1,
//                voteTime: "current time"
//    }
//    接口要求：如果用户剩的票数大于等于voteNum，则能成功给rsEventId对应的热搜事件投票
//    如果用户剩的票数小于voteNum,则投票失败，返回400
//    考虑到以后需要查询投票记录的需求（根据userId查询他投过票的所有热搜事件，票数和投票时间，根据rsEventId查询所有给他投过票的用户，票数和投票时间），
//    创建一个Vote表是一个明智的选择
//            目前不用考虑给热搜事件列表排序的问题

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
