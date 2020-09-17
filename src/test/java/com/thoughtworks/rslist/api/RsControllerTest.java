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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private RsEventService rsEventService;

    @BeforeEach
    void setUp(){
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

        mockMvc.perform(post("/rs/vote/{rsEventId}")
                .content(new ObjectMapper().writeValueAsString(voteDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_vote_success_when_left_votenum_more_than_votenum(){

    }

    @Test
    void should_add_one_rs_event_when_user_not_exist() throws Exception {
        String json = "{\"eventName\":\"猪肉涨价了\",\"keyword\":\"经济\",\"userId\":\"1\"}";
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_user_is_exist() throws Exception {
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);

        String json = "{\"eventName\":\"猪肉涨价了\",\"keyword\":\"经济\",\"userId\":\"1\"}";
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventEntity> rsEventEntities = rsEventService.findAll();
        assertEquals(1,rsEventEntities.size());
        assertEquals("猪肉涨价了", rsEventEntities.get(0).getEventName());
    }


//    //隐藏userDto属性
//    @Test
//    void should_get_one_rs_event() throws Exception {
//        mockMvc.perform(get("/rs/3"))
//                .andExpect(jsonPath("$.eventName", is("第三条事件")))
//                .andExpect(jsonPath("$.keyword", is("无分类")))
//                .andExpect(status().isOk())//.andReturn()
//                .andExpect(jsonPath("$", not(hasKey("userDto"))));
//    }
//
//    //隐藏userDto属性
//    @Test
//    void should_get_rs_event_by_range_when_start_and_end_is_not_null() throws Exception {
//        mockMvc.perform(get("/rs/list?start=1&end=3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyword", is("无分类")))
//                .andExpect(jsonPath("$[2]", not(hasKey("userDto"))));
//    }
//
//    @Test
//    void should_get_rs_event_by_range_when_start_is_null() throws Exception {
//        mockMvc.perform(get("/rs/list?end=3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyword", is("无分类")));
//    }
//
//    @Test
//    void should_get_rs_event_by_range_when_end_is_null() throws Exception {
//        mockMvc.perform(get("/rs/list?start=1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyword", is("无分类")));
//    }

    @Test
    void should_update_one_rs_event_when_user_id_and_rs_user_id_not_match() throws Exception {
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

        mockMvc.perform(put("/patch/rs/2?userId=3&eventName=猪肉终于跌价了&keyword=民生"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_update_one_rs_event_when_id_match_eventName_empty() throws Exception {
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

        mockMvc.perform(put("/patch/rs/2?userId=1&keyword=民生"))
                .andExpect(status().isCreated());

        Optional<RsEventEntity> rsEventEntityOptional = rsEventService.findById(2);
        RsEventEntity rsEventEntity = rsEventEntityOptional.get();
        assertEquals("猪肉涨价了", rsEventEntity.getEventName());
        assertEquals("民生", rsEventEntity.getKeyword());
    }

    @Test
    void should_update_one_rs_event_when_id_match_keyword_empty() throws Exception {
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

        mockMvc.perform(put("/patch/rs/2?userId=1&eventName=猪肉终于跌价了"))
                .andExpect(status().isCreated());

        Optional<RsEventEntity> rsEventEntityOptional = rsEventService.findById(2);
        RsEventEntity rsEventEntity = rsEventEntityOptional.get();
        assertEquals("猪肉终于跌价了", rsEventEntity.getEventName());
        assertEquals("经济", rsEventEntity.getKeyword());
    }

    @Test
    void should_update_one_rs_event_when_id_match_eventName_keyword_not_empty() throws Exception {
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

        mockMvc.perform(put("/patch/rs/2?userId=1&eventName=猪肉终于跌价了&keyword=民生"))
                .andExpect(status().isCreated());

        Optional<RsEventEntity> rsEventEntityOptional = rsEventService.findById(2);
        RsEventEntity rsEventEntity = rsEventEntityOptional.get();
        assertEquals("猪肉终于跌价了", rsEventEntity.getEventName());
        assertEquals("民生", rsEventEntity.getKeyword());
    }



//    @Test
//    void should_delete_one_rs_event() throws Exception {
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)));
//
//        mockMvc.perform(delete("/rs/delete?id=3"))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("index", "2"));
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)));
//
//    }

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