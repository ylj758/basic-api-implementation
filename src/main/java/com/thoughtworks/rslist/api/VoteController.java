package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
class VoteController {
    @Autowired
    VoteService voteService;
    @Autowired
    UserService userService;

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity<Object> addRsEvent(@PathVariable Integer rsEventId,
                                             @Valid @RequestBody VoteDto voteDto) {
        Optional<UserEntity> userEntityOptional = userService.findById(voteDto.getUserId());
        UserEntity userEntity = userEntityOptional.get();
        if(userEntity.getVote() < voteDto.getVoteNum()){
            return ResponseEntity.badRequest().build();
        }
        voteService.save(voteDto);
        return ResponseEntity.created(null).build();
    }
}
