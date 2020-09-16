package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    private String eventName;
    private String keyword;
    private UserDto userDto;

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }
}
