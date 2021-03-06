package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
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
    @Autowired
    private VoteService voteService;

    @BeforeEach
    void setUp(){
        userService.deleteAll();
        rsEventService.deleteAll();
        voteService.deleteAll();
    }

    @Test
    void should_add_one_rs_event_when_user_not_exist() throws Exception {
        String json = "{\"eventName\":\"猪肉涨价了\",\"keyword\":\"经济\",\"userId\":\"1\"}";
        mockMvc.perform(post("/rs")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_user_is_exist() throws Exception {
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);

        String json = "{\"eventName\":\"猪肉涨价了\",\"keyword\":\"经济\",\"userId\":\"1\"}";
        mockMvc.perform(post("/rs")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventEntity> rsEventEntities = rsEventService.findAll();
        assertEquals(1,rsEventEntities.size());
        assertEquals("猪肉涨价了", rsEventEntities.get(0).getEventName());
    }


    @Test
    void should_get_one_rs_event() throws Exception {
        UserEntity userEntity = getAConcreteUserEntity();
        userService.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("猪肉涨价了")
                .keyword("经济")
                .userEntity(userEntity)
                .build();
        rsEventService.save(rsEventEntity);
        VoteEntity voteEntity = VoteEntity.builder()
                .voteNum(2)
                .voteTime(null)
                .userEntity(userEntity)
                .rsEventEntity(rsEventEntity)
                .build();
        voteService.save(voteEntity);
        voteEntity = VoteEntity.builder()
                .voteNum(2)
                .voteTime(null)
                .userEntity(userEntity)
                .rsEventEntity(rsEventEntity)
                .build();
        voteService.save(voteEntity);

        MvcResult mvcResult = mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("eventName: \"猪肉涨价了\", keyword: \"经济\", id: 2, voteNum: 4", mvcResult.getResponse().getContentAsString());
    }


    @Test
    void should_get_rs_event_by_range_when_start_and_end_is_not_null() throws Exception {
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);
        RsEvent rsEvent = RsEvent.builder()
                .eventName("猪肉涨价了")
                .keyword("经济")
                .userId(1)
                .build();
        rsEventService.save(rsEvent);
        RsEvent rsEvent1 = RsEvent.builder()
                .eventName("股票涨价了")
                .keyword("经济")
                .userId(1)
                .build();
        rsEventService.save(rsEvent1);

        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void should_get_rs_event_by_range_when_start_is_null() throws Exception {
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);
        RsEvent rsEvent = RsEvent.builder()
                .eventName("猪肉涨价了")
                .keyword("经济")
                .userId(1)
                .build();
        rsEventService.save(rsEvent);

        mockMvc.perform(get("/rs/list?end=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void should_get_rs_event_by_range_when_end_is_null() throws Exception {
        UserDto userDto = getAConcreteUser();
        userService.register(userDto);
        RsEvent rsEvent = RsEvent.builder()
                .eventName("猪肉涨价了")
                .keyword("经济")
                .userId(1)
                .build();
        rsEventService.save(rsEvent);

        mockMvc.perform(get("/rs/list?start=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

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

        mockMvc.perform(patch("/patch/rs/2?userId=3&eventName=猪肉终于跌价了&keyword=民生"))
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



    @Test
    void should_delete_one_rs_event() throws Exception {
        userService.register(getAConcreteUser());
        RsEvent rsEvent = RsEvent.builder()
                .eventName("猪肉涨价了")
                .keyword("经济")
                .userId(1)
                .build();
        rsEventService.save(rsEvent);
        assertEquals(1, rsEventService.findAll().size());
        assertEquals(1, userService.findAll().size());

        mockMvc.perform(delete("rs/delete?id=2"))
                .andExpect(status().isCreated());

        assertEquals(0, rsEventService.findAll().size());
        assertEquals(1, userService.findAll().size());

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
    public UserEntity getAConcreteUserEntity(){
        return UserEntity.builder()
                .name("XiaoMing")
                .age(22)
                .gender("male")
                .email("768@qq.com")
                .phone("12345678900")
                .vote(10)
                .build();
    }

}