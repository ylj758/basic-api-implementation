package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
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


    //  接口要求：当userId和rsEventId所关联的User匹配时，更新rsEvent信息
//  当userId和rsEventId所关联的User不匹配时，返回400
//          userId为必传字段
//  当只传了eventName没传keyword时只更新eventName
//          当只传了keyword没传eventName时只更新keyword
    @Test
    void should_update_one_rs_event_when_user_id_and_rs_user_id_not_match() throws Exception {
        mockMvc.perform(put("/patch/rs/2?userId=1&eventName=猪肉终于跌价了&keyword=民生"))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    void should_update_one_rs_event_when_id_match_eventName() throws Exception {
//        UserDto userDto = getAConcreteUser();
//        userService.register(userDto);
//        assertEquals(1, userService.findAll().size());
//        RsEvent rsEvent = RsEvent.builder()
//                .eventName("猪肉涨价了")
//                .keyword("经济")
//                .userId(1)
//                .build();
//        rsEventService.save(rsEvent);
//        assertEquals(1, rsEventService.findAll().size());
//
//        mockMvc.perform(put("/patch/rs/{rsEventId}?userId=1&eventName=猪肉终于跌价了&keyword=民生"))
//                .andExpect(status().isBadRequest());
//    }
//
//
//
//    @Test
//    void should_update_one_rs_event_by_eventName_and_keyword() throws Exception {
//
//        mockMvc.perform(put("/patch/rs/{rsEventId}?userId=1&eventName=猪肉终于跌价了&keyword=民生"))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("index", "3"));
//
//        mockMvc.perform(get("/rs/3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("猪肉终于跌价了")))
//                .andExpect(jsonPath("$.keyword", is("民生")));
//
//    }
//
//    @Test
//    void should_update_one_rs_event_by_eventName() throws Exception {
//        mockMvc.perform(get("/rs/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第二条事件")))
//                .andExpect(jsonPath("$.keyword", is("无分类")));
//
//        mockMvc.perform(put("/rs/update?id=2&eventName=猪肉终于跌价了"))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("index", "3"));;
//
//        mockMvc.perform(get("/rs/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("猪肉终于跌价了")))
//                .andExpect(jsonPath("$.keyword", is("无分类")));
//
//    }
//
//    @Test
//    void should_update_one_rs_event_by_keyword() throws Exception {
//        mockMvc.perform(get("/rs/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第二条事件")))
//                .andExpect(jsonPath("$.keyword", is("无分类")));
//
//        mockMvc.perform(put("/rs/update?id=2&keyword=经济"))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("index", "3"));;;
//
//        mockMvc.perform(get("/rs/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第二条事件")))
//                .andExpect(jsonPath("$.keyword", is("经济")));
//
//    }

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