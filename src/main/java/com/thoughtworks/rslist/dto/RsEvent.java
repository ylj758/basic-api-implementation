package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    public interface RsEventDetail{}

    @NotEmpty
    @JsonView(RsEventDetail.class)//或者在userDto字段上使用JsonIgnore
    private String eventName;
    @NotEmpty
    @JsonView(RsEventDetail.class)
    private String keyword;
    @NotNull
    @Valid
    private UserDto userDto;

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }
}
