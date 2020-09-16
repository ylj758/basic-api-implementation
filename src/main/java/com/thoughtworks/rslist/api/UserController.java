package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private List<UserDto> userDtos = initUserDtos();

    private List<UserDto> initUserDtos() {
        List<UserDto> tempUserDtos = new ArrayList<>();
        UserDto userDto1 = new UserDto("ylj1", "femal", 25, "123@qq.com", "12345678911");
        UserDto userDto2 = new UserDto("ylj2", "femal", 25, "123@qq.com", "12345678911");
        UserDto userDto3 = new UserDto("ylj3", "femal", 25, "123@qq.com", "12345678911");
        tempUserDtos.add(userDto1);
        tempUserDtos.add(userDto2);
        tempUserDtos.add(userDto3);
        return tempUserDtos;
    }

    public List<UserDto> getUserDtos() {
        return userDtos;
    }

    @PostMapping("/user/register")
    public void register(@Valid @RequestBody UserDto userDto){
        userDtos.add(userDto);
    }
}
