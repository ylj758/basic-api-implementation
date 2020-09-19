package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import org.junit.jupiter.api.AfterEach;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    }

    @AfterEach
    void tearDown() {
        voteService.deleteAll();
        rsEventService.deleteAll();
        userService.deleteAll();
    }

    @Test
    void should_get_vote_by_vote_time_between_when_start_more_than_end() throws Exception {
        setData();
        String startStr = "2020-09-18 15:15:00";
        String endStr = "2020-09-18 12:30:00";
        mockMvc.perform(get("/vote/time")
                .param("start", startStr)
                .param("end", endStr))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_get_vote_by_vote_time_between() throws Exception {
        setData();
        String startStr = "2020-09-18 10:15:00";
        String endStr = "2020-09-18 12:30:00";
        mockMvc.perform(get("/vote/time")
                .param("start", startStr)
                .param("end", endStr))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }


    @Test
    void should_get_vote_by_user_id_and_rs_event_id_page() throws Exception {
        setData();
        mockMvc.perform(get("/vote?userId=1&rsEventId=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(1)));

        mockMvc.perform(get("/vote?userId=1&rsEventId=2&pageIndex=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].voteNum", is(1)));
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
                .voteTime(null)
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
                .voteTime(null)
                .userId(1)
                .rsEventId(2)
                .build();

        mockMvc.perform(post("/rs/vote/2")
                .content(new ObjectMapper().writeValueAsString(voteDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        assertEquals(1, voteService.findAll().size());
        Optional<UserEntity> userEntityOptional = userService.findById(1);
        assertEquals(10 - 2, userEntityOptional.get().getVote());
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

    private void setData() {
        UserEntity userEntity = UserEntity.builder()
                .name("XiaoMing")
                .age(22)
                .gender("male")
                .email("768@qq.com")
                .phone("12345678900")
                .vote(10)
                .build();
        userService.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("猪肉涨价了")
                .keyword("经济")
                .userEntity(userEntity)
                .build();
        rsEventService.save(rsEventEntity);

        String str1 = "2020年09月18日 09:10:00";
        LocalDateTime date1 = LocalDateTime.parse(str1, DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
        VoteEntity voteEntity = VoteEntity.builder()
                .voteNum(1)
                .voteTime(date1)
                .userEntity(rsEventEntity.getUserEntity())
                .rsEventEntity(rsEventEntity)
                .build();
        voteService.save(voteEntity);

        String str2 = "2020年09月18日 10:10:00";
        LocalDateTime date2 = LocalDateTime.parse(str2, DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
        voteEntity = VoteEntity.builder()
                .voteNum(1)
                .voteTime(date2)
                .userEntity(userEntity)
                .rsEventEntity(rsEventEntity)
                .build();
        voteService.save(voteEntity);


        String str3 = "2020年09月18日 10:20:00";
        LocalDateTime date3 = LocalDateTime.parse(str3, DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
        voteEntity = VoteEntity.builder()
                .voteNum(1)
                .voteTime(date3)
                .userEntity(userEntity)
                .rsEventEntity(rsEventEntity)
                .build();
        voteService.save(voteEntity);

        String str4 = "2020年09月18日 11:20:00";
        LocalDateTime date4 = LocalDateTime.parse(str4, DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
        voteEntity = VoteEntity.builder()
                .voteNum(1)
                .voteTime(date4)
                .userEntity(userEntity)
                .rsEventEntity(rsEventEntity)
                .build();
        voteService.save(voteEntity);

        String str5 = "2020年09月18日 12:20:00";
        LocalDateTime date5 = LocalDateTime.parse(str5, DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
        voteEntity = VoteEntity.builder()
                .voteNum(1)
                .voteTime(date5)
                .userEntity(userEntity)
                .rsEventEntity(rsEventEntity)
                .build();
        voteService.save(voteEntity);

        String str6 = "2020年09月18日 13:20:00";
        LocalDateTime date6 = LocalDateTime.parse(str6, DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
        voteEntity = VoteEntity.builder()
                .voteNum(1)
                .voteTime(date6)
                .userEntity(userEntity)
                .rsEventEntity(rsEventEntity)
                .build();
        voteService.save(voteEntity);

        String str7 = "2020年09月18日 14:20:00";
        LocalDateTime date7 = LocalDateTime.parse(str7, DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
        voteEntity = VoteEntity.builder()
                .voteNum(1)
                .voteTime(date7)
                .userEntity(userEntity)
                .rsEventEntity(rsEventEntity)
                .build();
        voteService.save(voteEntity);

        String str8 = "2020年09月18日 15:20:00";
        LocalDateTime date8 = LocalDateTime.parse(str8, DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
        voteEntity = VoteEntity.builder()
                .voteNum(1)
                .voteTime(date8)
                .userEntity(userEntity)
                .rsEventEntity(rsEventEntity)
                .build();
        voteService.save(voteEntity);
}
}
