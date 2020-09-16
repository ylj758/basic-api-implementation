package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exceptions.CommentError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> register(@Valid @RequestBody UserDto userDto){
        userDtos.add(userDto);
        return ResponseEntity.created(null).header("index",String.valueOf(userDtos.size())).build();
    }

    @GetMapping("/user/users")
    public ResponseEntity<String> getAllUsers() throws JsonProcessingException {
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(userDtos));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CommentError> handleIndexOutOfBoundsException(Exception ex) {
        CommentError commentError = new CommentError();
        commentError.setError("invalid user");
        return ResponseEntity.status(400).body(commentError);
    }
}
