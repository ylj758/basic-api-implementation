package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.CommentError;
import com.thoughtworks.rslist.exceptions.InvalidIndexException;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> register(@PathVariable int id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable int id) throws InvalidIndexException {
        Optional<UserEntity> userEntityOptional = userService.findById(id);
        if(!userEntityOptional.isPresent()){
            throw new InvalidIndexException();
        }
        UserEntity userEntity = userEntityOptional.get();
        UserDto userDto = UserDto.builder()
                .name(userEntity.getName())
                .age(userEntity.getAge())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .gender(userEntity.getGender())
                .vote(userEntity.getVote())
                .build();
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/user/register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserDto userDto){
        userService.register(userDto);
        return ResponseEntity.created(null).build();
    }


    @GetMapping("/user/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() throws JsonProcessingException {
        return ResponseEntity.ok(userService.findAll());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CommentError> handleIndexOutOfBoundsException(Exception ex) {
        CommentError commentError = new CommentError();
        commentError.setError("invalid user");
        return ResponseEntity.status(400).body(commentError);
    }
}
