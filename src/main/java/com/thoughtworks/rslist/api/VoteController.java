package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
class VoteController {
    @Autowired
    VoteService voteService;
    @Autowired
    UserService userService;


    @GetMapping("/vote/time")
    public ResponseEntity<List<VoteDto>> getVoteTimeBetween(@RequestParam String start,
                                            @RequestParam String end) {
        String strToDatePattern = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern(strToDatePattern));
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern(strToDatePattern));
        if(startDate.isAfter(endDate)){
            return ResponseEntity.badRequest().build();
        }
        List<VoteEntity> voteEntities = voteService.findAllByVoteTimeBetween(startDate, endDate);
        return ResponseEntity.ok(voteEntityListConvertVoteDtoList(voteEntities));
    }

    @GetMapping("/vote")
    public List<VoteDto> getVoteByUserIdAndrsEventId(@RequestParam int userId,
                                                     @RequestParam int rsEventId,
                                                     @RequestParam(defaultValue = "1") int pageIndex) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        List<VoteEntity> voteEntities = voteService.findAllByUserIdAndRsEventId(userId, rsEventId, pageable);
        return voteEntityListConvertVoteDtoList(voteEntities);
    }

    @PostMapping("/rs/vote/{rsEventId}")
    @Transactional
    public ResponseEntity<Object> addVote(@PathVariable Integer rsEventId,
                                             @Valid @RequestBody VoteDto voteDto) {
        Optional<UserEntity> userEntityOptional = userService.findById(voteDto.getUserId());
        UserEntity userEntity = userEntityOptional.get();
        if(userEntity.getVote() < voteDto.getVoteNum()){
            return ResponseEntity.badRequest().build();
        }
        voteService.save(voteDto);
        userService.updateLeftVoteNum(voteDto.getUserId(), userEntity.getVote()-voteDto.getVoteNum());
        return ResponseEntity.created(null).build();
    }


    public List<VoteDto> voteEntityListConvertVoteDtoList(List<VoteEntity> voteEntities){
        return voteEntities.stream()
                .map(voteEntity -> VoteDto.builder()
                        .userId(voteEntity.getUserId())
                        .rsEventId(voteEntity.getRsEventId())
                        .voteNum(voteEntity.getVoteNum())
                        .voteTime(voteEntity.getVoteTime())
                        .build())
                .collect(Collectors.toList());
    }
}
