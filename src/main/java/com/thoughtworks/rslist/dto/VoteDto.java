package com.thoughtworks.rslist.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {
    @NotNull
    private int rsEventId;
    @NotNull
    private int userId;
    private Date voteTime;
    @NotNull
    private int voteNum;
}
