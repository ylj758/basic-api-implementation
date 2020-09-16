package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {

  private UserController userController;
  private List<UserDto> userDtos ;
  private List<RsEvent> rsList;
  @Autowired
  public RsController(UserController userController){
    this.userController = userController;
    userDtos = userController.getUserDtos();
    rsList = initRsList();
  }


  private List<RsEvent> initRsList() {
    List<RsEvent> tempRsList = new ArrayList<>();
    tempRsList.add(new RsEvent("第一条事件","无分类",userDtos.get(0)));
    tempRsList.add(new RsEvent("第二条事件","无分类", userDtos.get(1)));
    tempRsList.add(new RsEvent("第三条事件","无分类", userDtos.get(2)));
    return tempRsList;
  }

  @PostMapping("/rs/event")
  public ResponseEntity<Object> addRsEvent(@Valid @RequestBody RsEvent rsEvent){
    UserDto userDto = rsEvent.getUserDto();
    boolean isExistUserName = false;
    for (UserDto user : userDtos) {
      if(user.getName().equals(userDto.getName())){
        isExistUserName = true;
        break;
      }
    }
    if(!isExistUserName)
        userDtos.add(userDto);

    rsList.add(rsEvent);
    return ResponseEntity.created(null).header("index",String.valueOf(rsList.size())).build();
  }

  @JsonView(RsEvent.RsEventDetail.class)
  @GetMapping("/rs/{index}")
  public ResponseEntity<RsEvent> getRsEventByIndex(@PathVariable int index){
    return ResponseEntity.ok(rsList.get(index-1));
  }

//  @JsonView(RsEvent.RsEventDetail.class)
  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEvent>> getRsEventByRange(@RequestParam(required = false) Integer start,
                              @RequestParam(required = false) Integer end) throws JsonProcessingException {
    if(start == null || end == null){
      return ResponseEntity.ok(rsList);
    }
    return ResponseEntity.ok(rsList.subList(start-1,end));
  }

  @PutMapping("/rs/update")
  public ResponseEntity<Object> updateRsEvent(@RequestParam Integer id,
                            @RequestParam(required = false) String eventName,
                            @RequestParam(required = false) String keyword){
    RsEvent rsEvent = rsList.get(id-1);
    if(eventName != null)
      rsEvent.setEventName(eventName);
    if(keyword != null)
      rsEvent.setKeyword(keyword);
    return ResponseEntity.created(null).header("index",String.valueOf(rsList.size())).build();
  }

  @RequestMapping(value = "/rs/delete",method = RequestMethod.DELETE)
  public ResponseEntity<Object> deleteRsEvent(@RequestParam int id){
    rsList.remove(id-1);
    return ResponseEntity.created(null).header("index",String.valueOf(rsList.size())).build();
  }
}

