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

    //    接口要求：如果用户剩的票数大于等于voteNum，则能成功给rsEventId对应的热搜事件投票
//    如果用户剩的票数小于voteNum,则投票失败，返回400
//    考虑到以后需要查询投票记录的需求（根据userId查询他投过票的所有热搜事件，票数和投票时间，根据rsEventId查询所有给他投过票的用户，票数和投票时间），
//    创建一个Vote表是一个明智的选择
//            目前不用考虑给热搜事件列表排序的问题

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity<Object> addRsEvent(@PathVariable Integer rsEventId,
                                             @Valid @RequestBody VoteDto voteDto) {
        Optional<UserEntity> userEntityOptional = userService.findById(voteDto.getUserId());
        UserEntity userEntity = userEntityOptional.get();
        if(userEntity.getVote() < voteDto.getVoteNum()){
            return ResponseEntity.badRequest().build();
        }


        return ResponseEntity.created(null).build();
    }
}
