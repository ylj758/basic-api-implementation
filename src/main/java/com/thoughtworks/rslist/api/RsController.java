package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.exceptions.CommentError;
import com.thoughtworks.rslist.exceptions.InvalidIndexException;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {
    UserService userService;
    RsEventService rsEventService;
    VoteService voteService;
    @Autowired
    public RsController(UserService userService, RsEventService rsEventService, VoteService voteService){
        this.userService = userService;
        this.rsEventService = rsEventService;
        this.voteService = voteService;
    }

    @PostMapping("/rs/event")
    public ResponseEntity<Object> addRsEvent(@Valid @RequestBody RsEvent rsEvent) {
        List<UserEntity> userEntityList = userService.findAll();
        if (!userService.existsById(rsEvent.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        rsEventService.save(rsEvent);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity<String> getRsEventByIndex(@PathVariable int index) throws InvalidIndexException {
        int size = rsEventService.findAll().size();
        if (index < 0) {
            throw new InvalidIndexException();
        }

        Optional<RsEventEntity> rsEventEntityOptional = rsEventService.findById(index);
        RsEventEntity rsEventEntity = rsEventEntityOptional.get();
        String eventName = rsEventEntity.getEventName();
        String keyword = rsEventEntity.getKeyword();
        int rsEventId = rsEventEntity.getId();
        List<VoteEntity> voteEntityList = voteService.findAllByRsEventId(index);
        int voteNumSum = voteEntityList.stream().mapToInt(VoteEntity::getVoteNum).sum();

        String result = "eventName: \""+eventName+"\", keyword: \""+keyword+"\", id: "+ rsEventId + ", voteNum: "+voteNumSum;

        return ResponseEntity.ok(result);
    }

  @JsonView(RsEvent.RsEventDetail.class)
  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEventEntity>> getRsEventByRange(@RequestParam(required = false) Integer start,
                              @RequestParam(required = false) Integer end) throws JsonProcessingException {
    List<RsEventEntity> rsList = rsEventService.findAll();
    if(start == null || end == null){
      return ResponseEntity.ok(rsList);
    }
    return ResponseEntity.ok(rsList.subList(start-1,end));
  }


    @PutMapping("/patch/rs/{rsEventId}")
    public ResponseEntity<Object> updateRsEvent(@PathVariable Integer rsEventId,
                                                @RequestParam Integer userId,
                                                @RequestParam(required = false) String eventName,
                                                @RequestParam(required = false) String keyword) {

        Optional<RsEventEntity> rsEventEntityOptional = rsEventService.findById(rsEventId);
        RsEventEntity rsEventEntity = rsEventEntityOptional.get();
        if (rsEventEntity.getUserEntity().getId() != userId)
            return ResponseEntity.badRequest().build();
        String newEventName = eventName;
        String newKeyword = keyword;
        if (eventName == null || eventName.isEmpty()) {
            newEventName = rsEventEntity.getEventName();
        }
        if (keyword == null || keyword.isEmpty()) {
            newKeyword = rsEventEntity.getKeyword();
        }
        RsEventEntity newRsEventEntity = RsEventEntity.builder()
                .id(rsEventId)
                .eventName(newEventName)
                .keyword(newKeyword)
                .userEntity(rsEventEntity.getUserEntity())
                .build();
        rsEventService.update(newRsEventEntity);
        return ResponseEntity.created(null).build();
    }


    @RequestMapping(value = "/rs/delete-user",method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteRsEvent(@RequestParam int id){
        rsEventService.deleteById(id);
        return ResponseEntity.created(null).build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CommentError> handleIndexOutOfBoundsException(Exception ex) {
        CommentError commentError = new CommentError();
        commentError.setError("invalid param");
        return ResponseEntity.status(400).body(commentError);
    }
}

