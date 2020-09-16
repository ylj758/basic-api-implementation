package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Test
    void should_add_one_rs_event_when_user_empty() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_keyword_empty() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        RsEvent rsEvent = new RsEvent("猪肉涨价了", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_eventName_empty() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        RsEvent rsEvent = new RsEvent("", "经济");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_userName_not_exist() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        List<UserDto> userDtoList = userController.getUserDtos();
        assertEquals(userDtoList.size(), 3);

        UserDto userDto = new UserDto("ylj4", "femal", 25, "123@qq.com", "12345678911");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        MvcResult mvcResult = mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertEquals(mvcResult.getResponse().getHeader("index"),"4");

        userDtoList = userController.getUserDtos();
        assertEquals(userDtoList.size(), 4);

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[3].userDto.name",is("ylj4")));
    }

    @Test
    void should_add_one_rs_event_when_userName_is_exist() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        List<UserDto> userDtoList = userController.getUserDtos();
        assertEquals(userDtoList.size(), 3);

        UserDto userDto = new UserDto("ylj3", "femal", 25, "123@qq.com", "12345678911");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        MvcResult mvcResult = mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertEquals(mvcResult.getResponse().getHeader("index"),"4");

        userDtoList = userController.getUserDtos();
        assertEquals(userDtoList.size(), 3);

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[3].userDto.name",is("ylj3")));
    }

    @Test
    void should_add_one_rs_event_when_userName_empty() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        UserDto userDto = new UserDto("", "femal", 25, "123@qq.com", "12345678911");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_userName_length_not_more_than_8() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        UserDto userDto = new UserDto("yjfjsfkkhj", "femal", 25, "123@qq.com", "12345678911");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_gender_empty() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        UserDto userDto = new UserDto("ylj", null, 25, "123@qq.com", "12345678911");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_age_empty() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        UserDto userDto = new UserDto("ylj", "femal", null, "123@qq.com", "12345678911");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_age_not_more_than_18() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        UserDto userDto = new UserDto("ylj", "femal", 3, "123@qq.com", "12345678911");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_age_more_than_100() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        UserDto userDto = new UserDto("ylj", "femal", 101, "123@qq.com", "12345678911");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_email_empty() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        UserDto userDto = new UserDto("ylj", "femal", 25, "", "12345678911");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_email_invalid() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        UserDto userDto = new UserDto("ylj", "femal", 25, "123", "12345678911");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_phone_empty() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        UserDto userDto = new UserDto("ylj", "femal", 25, "123@qq.com", "");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_rs_event_when_phone_invalid() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        UserDto userDto = new UserDto("ylj", "femal", 25, "123@qq.com", "78911");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }



    @Test
    void should_get_one_rs_event() throws Exception {
        MvcResult mvcResult =
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")))
                .andExpect(status().isOk()).andReturn();
        MockHttpServletResponse response= mvcResult.getResponse();
        response.setCharacterEncoding("UTF-8");
        assertEquals(response.getContentAsString(), "{\"eventName\":\"第三条事件\",\"keyword\":\"无分类\"}");
    }

    @Test
    void should_get_rs_event_by_range_when_start_and_end_is_not_null() throws Exception {
        MvcResult mvcResult =
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类"))).andReturn();
        MockHttpServletResponse response= mvcResult.getResponse();
        response.setCharacterEncoding("UTF-8");
        List<RsEvent> rsList = new ArrayList<>();
        rsList.add(new RsEvent("第一条事件","无分类"));
        rsList.add(new RsEvent("第二条事件","无分类"));
        rsList.add(new RsEvent("第三条事件","无分类"));
        assertEquals(response.getContentAsString(),   new ObjectMapper().writeValueAsString(rsList));

    }

    @Test
    void should_get_rs_event_by_range_when_start_is_null() throws Exception {
        mockMvc.perform(get("/rs/list?end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")));
    }

    @Test
    void should_get_rs_event_by_range_when_end_is_null() throws Exception {
        mockMvc.perform(get("/rs/list?start=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")));
    }



    @Test
    void should_update_one_rs_event_by_eventName_and_keyword() throws Exception {
        mockMvc.perform(get("/rs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")));

        mockMvc.perform(put("/rs/update?id=3&eventName=猪肉终于跌价了&keyword=民生"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("猪肉终于跌价了")))
                .andExpect(jsonPath("$.keyword", is("民生")));

    }

    @Test
    void should_update_one_rs_event_by_eventName() throws Exception {
        mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")));

        mockMvc.perform(put("/rs/update?id=2&eventName=猪肉终于跌价了"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("猪肉终于跌价了")))
                .andExpect(jsonPath("$.keyword", is("无分类")));

    }

    @Test
    void should_update_one_rs_event_by_keyword() throws Exception {
        mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")));

        mockMvc.perform(put("/rs/update?id=2&keyword=经济"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("经济")));

    }



    @Test
    void should_delete_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(delete("/rs/delete?id=3"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

}